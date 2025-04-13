package fr.catcore.fabricatedforge.mixin.forgefml.client.render;

import com.mojang.blaze3d.platform.GLX;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.fabricatedforge.mixininterface.ITessellator;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

@Mixin(Tessellator.class)
public abstract class TessellatorMixin implements ITessellator {
    @Shadow public boolean tesselating;
    @Shadow private int count;
    @Shadow public int format;
    @Shadow private static boolean field_1946;
    @Shadow private IntBuffer bufferInt;
    @Shadow private int[] array;
    @Shadow private ByteBuffer buffer;
    @Shadow private boolean field_1940;
    @Shadow private int field_1942;
    @Shadow private int field_1943;
    @Shadow private IntBuffer vboBuffer;
    @Shadow private boolean hasTexture;
    @Shadow private FloatBuffer bufferFloat;
    @Shadow private boolean hasLight;
    @Shadow private ShortBuffer bufferShort;
    @Shadow private boolean hasColor;
    @Shadow private boolean hasNormal;
    @Shadow private int arrayIdx;

    @Shadow protected abstract void reset();

    @Shadow private int vertexCount;
    @Shadow private double u;
    @Shadow private double v;
    @Shadow private int light;
    @Shadow private int color;
    @Shadow private int normal;
    @Shadow public double offsetX;
    @Shadow public double offsetY;
    @Shadow public double offsetZ;
    @Shadow public static Tessellator INSTANCE;
    private static int nativeBufferSize = 0x200000;
    private static int trivertsInBuffer = nativeBufferSize / 48 * 6;
    // Should be public
    @Public
    private static boolean renderingWorldRenderer = false;
    public boolean defaultTexture = false;
    private int rawBufferSize = 0;
    public int textureID = 0;
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/GlAllocationUtils;allocateByteBuffer(I)Ljava/nio/ByteBuffer;"))
    private void ctr(int par1, CallbackInfo ci) {
        this.defaultTexture = true;
        this.field_1943 = 10;
        this.field_1940 = false;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ctr2(int par1, CallbackInfo ci) {
        this.array = null;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int end() {
        if (!this.tesselating) {
            throw new IllegalStateException("Not tesselating!");
        } else {
            this.tesselating = false;
            int offs = 0;

            int vtc;
            while(offs < this.count) {
                if (this.format == 7 && field_1946) {
                    vtc = Math.min(this.count - offs, trivertsInBuffer);
                } else {
                    vtc = Math.min(this.count - offs, nativeBufferSize >> 5);
                }

                bufferInt.clear();
                bufferInt.put(this.array, offs * 8, vtc * 8);
                buffer.position(0);
                buffer.limit(vtc * 32);
                offs += vtc;
                if (field_1940) {
                    this.field_1942 = (this.field_1942 + 1) % field_1943;
                    ARBVertexBufferObject.glBindBufferARB(34962, vboBuffer.get(this.field_1942));
                    ARBVertexBufferObject.glBufferDataARB(34962, buffer, 35040);
                }

                if (this.hasTexture) {
                    if (field_1940) {
                        GL11.glTexCoordPointer(2, 5126, 32, 12L);
                    } else {
                        bufferFloat.position(3);
                        GL11.glTexCoordPointer(2, 32, bufferFloat);
                    }

                    GL11.glEnableClientState(32888);
                }

                if (this.hasLight) {
                    GLX.gl13ClientActiveTexture(GLX.lightmapTextureUnit);
                    if (field_1940) {
                        GL11.glTexCoordPointer(2, 5122, 32, 28L);
                    } else {
                        bufferShort.position(14);
                        GL11.glTexCoordPointer(2, 32, bufferShort);
                    }

                    GL11.glEnableClientState(32888);
                    GLX.gl13ClientActiveTexture(GLX.textureUnit);
                }

                if (this.hasColor) {
                    if (field_1940) {
                        GL11.glColorPointer(4, 5121, 32, 20L);
                    } else {
                        buffer.position(20);
                        GL11.glColorPointer(4, true, 32, buffer);
                    }

                    GL11.glEnableClientState(32886);
                }

                if (this.hasNormal) {
                    if (field_1940) {
                        GL11.glNormalPointer(5121, 32, 24L);
                    } else {
                        buffer.position(24);
                        GL11.glNormalPointer(32, buffer);
                    }

                    GL11.glEnableClientState(32885);
                }

                if (field_1940) {
                    GL11.glVertexPointer(3, 5126, 32, 0L);
                } else {
                    bufferFloat.position(0);
                    GL11.glVertexPointer(3, 32, bufferFloat);
                }

                GL11.glEnableClientState(32884);
                if (this.format == 7 && field_1946) {
                    GL11.glDrawArrays(4, 0, vtc);
                } else {
                    GL11.glDrawArrays(this.format, 0, vtc);
                }

                GL11.glDisableClientState(32884);
                if (this.hasTexture) {
                    GL11.glDisableClientState(32888);
                }

                if (this.hasLight) {
                    GLX.gl13ClientActiveTexture(GLX.lightmapTextureUnit);
                    GL11.glDisableClientState(32888);
                    GLX.gl13ClientActiveTexture(GLX.textureUnit);
                }

                if (this.hasColor) {
                    GL11.glDisableClientState(32886);
                }

                if (this.hasNormal) {
                    GL11.glDisableClientState(32885);
                }
            }

            if (this.rawBufferSize > 131072 && this.arrayIdx < this.rawBufferSize << 3) {
                this.rawBufferSize = 0;
                this.array = null;
            }

            vtc = this.arrayIdx * 4;
            this.reset();
            return vtc;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void vertex(double par1, double par3, double par5) {
        if (this.arrayIdx >= this.rawBufferSize - 32) {
            if (this.rawBufferSize == 0) {
                this.rawBufferSize = 65536;
                this.array = new int[this.rawBufferSize];
            } else {
                this.rawBufferSize *= 2;
                this.array = Arrays.copyOf(this.array, this.rawBufferSize);
            }
        }

        ++this.vertexCount;
        if (this.format == 7 && field_1946 && this.vertexCount % 4 == 0) {
            for(int var7 = 0; var7 < 2; ++var7) {
                int var8 = 8 * (3 - var7);
                if (this.hasTexture) {
                    this.array[this.arrayIdx + 3] = this.array[this.arrayIdx - var8 + 3];
                    this.array[this.arrayIdx + 4] = this.array[this.arrayIdx - var8 + 4];
                }

                if (this.hasLight) {
                    this.array[this.arrayIdx + 7] = this.array[this.arrayIdx - var8 + 7];
                }

                if (this.hasColor) {
                    this.array[this.arrayIdx + 5] = this.array[this.arrayIdx - var8 + 5];
                }

                this.array[this.arrayIdx + 0] = this.array[this.arrayIdx - var8 + 0];
                this.array[this.arrayIdx + 1] = this.array[this.arrayIdx - var8 + 1];
                this.array[this.arrayIdx + 2] = this.array[this.arrayIdx - var8 + 2];
                ++this.count;
                this.arrayIdx += 8;
            }
        }

        if (this.hasTexture) {
            this.array[this.arrayIdx + 3] = Float.floatToRawIntBits((float)this.u);
            this.array[this.arrayIdx + 4] = Float.floatToRawIntBits((float)this.v);
        }

        if (this.hasLight) {
            this.array[this.arrayIdx + 7] = this.light;
        }

        if (this.hasColor) {
            this.array[this.arrayIdx + 5] = this.color;
        }

        if (this.hasNormal) {
            this.array[this.arrayIdx + 6] = this.normal;
        }

        this.array[this.arrayIdx + 0] = Float.floatToRawIntBits((float)(par1 + this.offsetX));
        this.array[this.arrayIdx + 1] = Float.floatToRawIntBits((float)(par3 + this.offsetY));
        this.array[this.arrayIdx + 2] = Float.floatToRawIntBits((float)(par5 + this.offsetZ));
        this.arrayIdx += 8;
        ++this.count;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void addOffset(float par1, float par2, float par3) {
        this.offsetX += (double)par1;
        this.offsetY += (double)par2;
        this.offsetZ += (double)par3;
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void classInit(CallbackInfo ci) {

    }

    @Override
    public boolean defaultTexture() {
        return this.defaultTexture;
    }

    @Override
    public boolean renderingWorldRenderer() {
        return renderingWorldRenderer;
    }

    @Override
    public void renderingWorldRenderer(boolean bool) {
        renderingWorldRenderer = bool;
    }

    @Override
    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    @Override
    public int getTextureID() {
        return this.textureID;
    }
}
