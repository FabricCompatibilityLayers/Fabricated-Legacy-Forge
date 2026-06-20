/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import fr.catcore.cursedmixinextensions.annotations.NewConstructor;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import io.github.fabriccompatibilitylayers.fabricatedfml.utils.MakeStatic;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.*;
import java.util.Arrays;

@Mixin(Tessellator.class)
public abstract class TessellatorMixin {

    // -------------------------------------------------------------------------
    // New fields added by the Forge patch
    // -------------------------------------------------------------------------

    // Pattern M (@Public): new public static members must be private + @Public — Mixin rejects new non-private statics.
    private static int nativeBufferSize = 0x200000;
    private static int trivertsInBuffer = (nativeBufferSize / 48) * 6;
    @Public
    private static boolean renderingWorldRenderer = false;
    public boolean defaultTexture = false;
    private int rawBufferSize = 0;
    public int textureID = 0;

    // -------------------------------------------------------------------------
    // @Shadow declarations
    // -------------------------------------------------------------------------

    @Shadow public static Tessellator instance;

    // Pattern I + @MakeStatic (two-annotation, two-step):
    //   • @Shadow is declared NON-static so Mixin's createContextFor validation passes
    //     (the target fields are instance fields in vanilla and staticness is verified before preApply).
    //   • @MakeStatic is the second annotation; it drives a two-step promotion:
    //       Step 1 (preApply, after validation): sets ACC_STATIC on the matching target field.
    //       Step 2 (postApply, after @Overwrite injection): rewrites every GETFIELD/PUTFIELD that
    //       references these now-static fields to GETSTATIC/PUTSTATIC in all target methods,
    //       including the freshly-injected @Overwrite bodies.
    @Shadow @MakeStatic private ByteBuffer byteBuffer;
    @Shadow @MakeStatic private IntBuffer intBuffer;
    @Shadow @MakeStatic private FloatBuffer floatBuffer;
    @Shadow @MakeStatic private ShortBuffer shortBuffer;

    // Same two-step approach. vboCount's initializer (= 10) lives in the vanilla constructor;
    // postApply's PUTFIELD→PUTSTATIC rewrite picks it up there too.
    @Shadow @MakeStatic private boolean useVBO;
    @Shadow @MakeStatic private IntBuffer vertexBuffers;
    @Shadow @MakeStatic private int vboCount;

    @Shadow private static boolean convertQuadsToTriangles;
    @Shadow public boolean isDrawing;
    @Shadow public int drawMode;
    @Shadow private int vertexCount;
    @Shadow private int rawBufferIndex;
    @Shadow private int[] rawBuffer;
    @Shadow private int vboIndex;
    @Shadow private boolean hasTexture;
    @Shadow private boolean hasBrightness;
    @Shadow private boolean hasColor;
    @Shadow private boolean hasNormals;
    @Shadow private int addedVertices;
    @Shadow private double textureU;
    @Shadow private double textureV;
    @Shadow private int brightness;
    @Shadow private int color;
    @Shadow private int normal;
    @Shadow public double xOffset;
    @Shadow public double yOffset;
    @Shadow public double zOffset;
    @Shadow protected abstract void reset();

    @Shadow
    private static boolean tryVBO;

    @ShadowSuperConstructor
    private void superConstructor() {}

    @ReplaceConstructor
    public void constructor(int arg) {
        superConstructor();
    }

    @NewConstructor
    public void constructor() {
        superConstructor();
    }

    // -------------------------------------------------------------------------
    // Static initializer hook
    // -------------------------------------------------------------------------

    // Pattern A (@Inject at RETURN on <clinit>): matches the patch's static block literally —
    // runs after all static field initializers (including `instance = new Tessellator(2097152)`).
    //
    // The buffer/@MakeStatic fields are non-static @Shadows (matching vanilla), so they cannot be
    // referenced directly from this static method.  We access them via the singleton `instance` cast
    // to TessellatorMixin.  postApply's fixStaticFieldAccess will rewrite those GETFIELD/PUTFIELD
    // instructions to GETSTATIC/PUTSTATIC once preApply has promoted the fields to static.
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void forge$classInit(CallbackInfo ci) {
        TessellatorMixin tess = (TessellatorMixin) (Object) instance;
        tess.byteBuffer = GLAllocation.createDirectByteBuffer(nativeBufferSize * 4);
        tess.intBuffer = tess.byteBuffer.asIntBuffer();
        tess.floatBuffer = tess.byteBuffer.asFloatBuffer();
        tess.shortBuffer = tess.byteBuffer.asShortBuffer();
        tess.vboCount = 10;
        tess.defaultTexture = true;
        tess.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;

        if (tess.useVBO) {
            tess.vertexBuffers = GLAllocation.createDirectIntBuffer(tess.vboCount);
            ARBVertexBufferObject.glGenBuffersARB(tess.vertexBuffers);
        }
    }

    // -------------------------------------------------------------------------
    // draw()
    // -------------------------------------------------------------------------

    // Pattern D (@Overwrite): hunks 6/7/8 restructure draw() around a while-loop with vtc chunking,
    // change both glDrawArrays sites, and add a post-loop buffer-shrink — too interdependent for
    // surgical injection.
    /**
     * @author MinecraftForge
     * @reason Chunked rendering loop to avoid native buffer overflow; dynamic raw buffer management
     */
    @Overwrite
    public int draw() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not tesselating!");
        } else {
            this.isDrawing = false;
            int offs = 0;
            while (offs < vertexCount) {
                int vtc = 0;
                if (drawMode == 7 && convertQuadsToTriangles)
                {
                    vtc = Math.min(vertexCount - offs, trivertsInBuffer);
                }
                else
                {
                    vtc = Math.min(vertexCount - offs, nativeBufferSize >> 5);
                }

                ((Buffer) intBuffer).clear();
                intBuffer.put(this.rawBuffer, offs * 8, vtc * 8);
                ((Buffer) byteBuffer).position(0);
                ((Buffer) byteBuffer).limit(vtc * 32);
                offs += vtc;

                if (this.useVBO) {
                    this.vboIndex = (this.vboIndex + 1) % this.vboCount;
                    ARBVertexBufferObject.glBindBufferARB(34962, this.vertexBuffers.get(this.vboIndex));
                    ARBVertexBufferObject.glBufferDataARB(34962, this.byteBuffer, 35040);
                }

                if (this.hasTexture) {
                    if (this.useVBO) {
                        GL11.glTexCoordPointer(2, 5126, 32, 12L);
                    } else {
                        ((Buffer) floatBuffer).position(3);
                        GL11.glTexCoordPointer(2, 32, this.floatBuffer);
                    }

                    GL11.glEnableClientState(32888);
                }

                if (this.hasBrightness) {
                    OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
                    if (this.useVBO) {
                        GL11.glTexCoordPointer(2, 5122, 32, 28L);
                    } else {
                        ((Buffer) shortBuffer).position(14);
                        GL11.glTexCoordPointer(2, 32, this.shortBuffer);
                    }

                    GL11.glEnableClientState(32888);
                    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                }

                if (this.hasColor) {
                    if (this.useVBO) {
                        GL11.glColorPointer(4, 5121, 32, 20L);
                    } else {
                        ((Buffer) byteBuffer).position(20);
                        GL11.glColorPointer(4, true, 32, this.byteBuffer);
                    }

                    GL11.glEnableClientState(32886);
                }

                if (this.hasNormals) {
                    if (this.useVBO) {
                        GL11.glNormalPointer(5121, 32, 24L);
                    } else {
                        ((Buffer) byteBuffer).position(24);
                        GL11.glNormalPointer(32, this.byteBuffer);
                    }

                    GL11.glEnableClientState(32885);
                }

                if (this.useVBO) {
                    GL11.glVertexPointer(3, 5126, 32, 0L);
                } else {
                    ((Buffer) floatBuffer).position(0);
                    GL11.glVertexPointer(3, 32, this.floatBuffer);
                }

                GL11.glEnableClientState(32884);
                if (this.drawMode == 7 && convertQuadsToTriangles) {
                    GL11.glDrawArrays(4, 0, vtc);
                } else {
                    GL11.glDrawArrays(this.drawMode, 0, vtc);
                }

                GL11.glDisableClientState(32884);
                if (this.hasTexture) {
                    GL11.glDisableClientState(32888);
                }

                if (this.hasBrightness) {
                    OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
                    GL11.glDisableClientState(32888);
                    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                }

                if (this.hasColor) {
                    GL11.glDisableClientState(32886);
                }

                if (this.hasNormals) {
                    GL11.glDisableClientState(32885);
                }
            }

            if (rawBufferSize > 0x20000 && rawBufferIndex < (rawBufferSize << 3))
            {
                rawBufferSize = 0;
                rawBuffer = null;
            }

            int var1 = this.rawBufferIndex * 4;
            this.reset();
            return var1;
        }
    }

    // -------------------------------------------------------------------------
    // addVertex()
    // -------------------------------------------------------------------------

    // Pattern D (@Overwrite): hunk 9 adds dynamic growth at the top and hunk 10 removes the
    // auto-flush at the bottom — both touch the same method's entry and exit contract,
    // making surgical injection impractical.
    // Logic delta: bufferSize-based flush is gone entirely; rawBuffer grows unboundedly per vertex call.
    /**
     * @author MinecraftForge
     * @reason Dynamic raw buffer growth replaces fixed-size auto-flush strategy
     */
    @Overwrite
    public void addVertex(double par1, double par3, double par5) {
        if (this.rawBufferIndex >= this.rawBufferSize - 32) {
            if (this.rawBufferSize == 0) {
                this.rawBufferSize = 0x10000;
                this.rawBuffer = new int[this.rawBufferSize];
            } else {
                this.rawBufferSize *= 2;
                this.rawBuffer = Arrays.copyOf(this.rawBuffer, this.rawBufferSize);
            }
        }

        ++this.addedVertices;
        if (this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
            for(int var7 = 0; var7 < 2; ++var7) {
                int var8 = 8 * (3 - var7);
                if (this.hasTexture) {
                    this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - var8 + 3];
                    this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - var8 + 4];
                }

                if (this.hasBrightness) {
                    this.rawBuffer[this.rawBufferIndex + 7] = this.rawBuffer[this.rawBufferIndex - var8 + 7];
                }

                if (this.hasColor) {
                    this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - var8 + 5];
                }

                this.rawBuffer[this.rawBufferIndex + 0] = this.rawBuffer[this.rawBufferIndex - var8 + 0];
                this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - var8 + 1];
                this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - var8 + 2];
                ++this.vertexCount;
                this.rawBufferIndex += 8;
            }
        }

        if (this.hasTexture) {
            this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
            this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
        }

        if (this.hasBrightness) {
            this.rawBuffer[this.rawBufferIndex + 7] = this.brightness;
        }

        if (this.hasColor) {
            this.rawBuffer[this.rawBufferIndex + 5] = this.color;
        }

        if (this.hasNormals) {
            this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
        }

        this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(par1 + this.xOffset));
        this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(par3 + this.yOffset));
        this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(par5 + this.zOffset));
        this.rawBufferIndex += 8;
        ++this.vertexCount;
    }

}