package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.FMLLog;
import fr.catcore.cursedmixinextensions.annotations.Public;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(RenderEngine.class)
public abstract class RenderEngineMixin {
    @Shadow protected abstract int[] func_78348_b(BufferedImage p_78348_1_);

    @Shadow private BufferedImage field_78364_l;
    @Shadow private HashMap field_78359_d;

    @Shadow public List field_78367_h;
    @Shadow private GameSettings field_78365_j;
    @Shadow private ByteBuffer field_78358_g;
    @Shadow public TexturePackList field_78366_k;
    @Shadow private IntHashMap field_78360_e;

    @Shadow public abstract void func_78351_a(BufferedImage p_78351_1_, int p_78351_2_);

    @Shadow private Map field_78368_i;
    @Shadow private HashMap field_78362_c;

    @Shadow protected abstract BufferedImage func_78354_c(BufferedImage p_78354_1_);

    @Shadow protected abstract BufferedImage func_78345_a(InputStream p_78345_1_);

    @Shadow public boolean field_78363_a;
    @Shadow public boolean field_78361_b;

    @Shadow protected abstract int[] func_78340_a(BufferedImage p_78340_1_, int[] p_78340_2_);

    @Public
    private static Logger log = FMLLog.getLogger();

    @Inject(method = "func_78346_a", at = @At(value = "INVOKE", target = "Ljava/io/IOException;printStackTrace()V", remap = false))
    private void fml$logError(String p_78346_1_, CallbackInfoReturnable<int[]> cir, @Local(ordinal = 0) IOException var6) {
        log.log(Level.INFO, String.format("An error occured reading texture file %s (getTexture)", p_78346_1_), var6);
    }

    @WrapMethod(method = "func_78346_a")
    private int[] fml$catchOtherErrors(String p_78346_1_, Operation<int[]> original) {
        try {
            return original.call(p_78346_1_);
        } catch (Exception var6) {
            log.log(Level.INFO, String.format("An error occured reading texture file %s (getTexture)", p_78346_1_), var6);
            var6.printStackTrace();
            int[] var5 = this.func_78348_b(this.field_78364_l);
            this.field_78359_d.put(p_78346_1_, var5);
            return var5;
        }
    }

    @WrapOperation(method = "func_78351_a", at = @At(value = "INVOKE", target = "Ljava/awt/image/BufferedImage;getHeight()I", remap = false))
    private int fml$setTextureDimensions(BufferedImage instance, Operation<Integer> original, @Local(ordinal = 0, argsOnly = true) int p_78351_2_, @Local(ordinal = 1) int var3) {
        int var4 = original.call(instance);
        TextureFXManager.instance().setTextureDimensions(p_78351_2_, var3, var4, (List<TextureFX>)field_78367_h);
        return var4;
    }

    @Inject(method = "func_78355_a", at = @At("HEAD"))
    private void fml$onPreRegisterEffect(TextureFX p_78355_1_, CallbackInfo ci) {
        TextureFXManager.instance().onPreRegisterEffect(p_78355_1_);
    }

    /**
     * @author cpw?
     * @reason changes to loop with extra jumps and operations
     */
    @Overwrite
    public void func_78343_a() {
        int var1 = -1;

        for (Object o : this.field_78367_h) {
            TextureFX var3 = (TextureFX) o;
            var3.field_76851_c = this.field_78365_j.field_74337_g;
            if (!TextureFXManager.instance().onUpdateTextureEffect(var3)) {
                continue;
            }

            Dimension dim = TextureFXManager.instance().getTextureDimensions(var3);
            int tWidth = dim.width >> 4;
            int tHeight = dim.height >> 4;
            int tLen = tWidth * tHeight << 2;

            if (var3.field_76852_a.length == tLen) {
                this.field_78358_g.clear();
                this.field_78358_g.put(var3.field_76852_a);
                this.field_78358_g.position(0).limit(var3.field_76852_a.length);
            } else {
                TextureFXManager.instance().scaleTextureFXData(var3.field_76852_a, field_78358_g, tWidth, tLen);
            }

            if (var3.field_76850_b != var1) {
                var3.func_76845_a((RenderEngine) (Object) this);
                var1 = var3.field_76850_b;
            }

            for (int var4 = 0; var4 < var3.field_76849_e; ++var4) {
                int xOffset = var3.field_76850_b % 16 * tWidth + var4 * tWidth;

                for (int var5 = 0; var5 < var3.field_76849_e; ++var5) {
                    int yOffset = var3.field_76850_b / 16 * tHeight + var5 * tHeight;
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, xOffset, yOffset, tWidth, tHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.field_78358_g);
                }
            }
        }

    }

    /**
     * @author cpw?
     * @reason Catch more exceptions and log them
     */
    @Overwrite
    public void func_78352_b() {
        TexturePackBase var1 = this.field_78366_k.func_77292_e();

        for(int var3 : (Set<Integer>)this.field_78360_e.func_76039_d()) {
            BufferedImage var4 = (BufferedImage)this.field_78360_e.func_76041_a(var3);
            this.func_78351_a(var4, var3);
        }

        for(ThreadDownloadImageData var11 : (Collection<ThreadDownloadImageData>)this.field_78368_i.values()) {
            var11.field_78459_d = false;
        }

        for(String var12 : ((Set<String>) this.field_78362_c.keySet())) {
            try {
                BufferedImage var14;
                if (var12.startsWith("##")) {
                    var14 = this.func_78354_c(this.func_78345_a(var1.func_77532_a(var12.substring(2))));
                } else if (var12.startsWith("%clamp%")) {
                    this.field_78363_a = true;
                    var14 = this.func_78345_a(var1.func_77532_a(var12.substring(7)));
                } else if (var12.startsWith("%blur%")) {
                    this.field_78361_b = true;
                    var14 = this.func_78345_a(var1.func_77532_a(var12.substring(6)));
                } else if (var12.startsWith("%blurclamp%")) {
                    this.field_78361_b = true;
                    this.field_78363_a = true;
                    var14 = this.func_78345_a(var1.func_77532_a(var12.substring(11)));
                } else {
                    var14 = this.func_78345_a(var1.func_77532_a(var12));
                }

                int var5 = (Integer)this.field_78362_c.get(var12);
                this.func_78351_a(var14, var5);
                this.field_78361_b = false;
                this.field_78363_a = false;
            } catch (Exception var7) {
                log.log(Level.INFO,String.format("An error occured reading texture file %s (refreshTexture)", var12),var7);
                var7.printStackTrace();
            }
        }

        for(String var13 : ((Set<String>) this.field_78359_d.keySet())) {
            try {
                BufferedImage var15;
                if (var13.startsWith("##")) {
                    var15 = this.func_78354_c(this.func_78345_a(var1.func_77532_a(var13.substring(2))));
                } else if (var13.startsWith("%clamp%")) {
                    this.field_78363_a = true;
                    var15 = this.func_78345_a(var1.func_77532_a(var13.substring(7)));
                } else if (var13.startsWith("%blur%")) {
                    this.field_78361_b = true;
                    var15 = this.func_78345_a(var1.func_77532_a(var13.substring(6)));
                } else {
                    var15 = this.func_78345_a(var1.func_77532_a(var13));
                }

                this.func_78340_a(var15, (int[])this.field_78359_d.get(var13));
                this.field_78361_b = false;
                this.field_78363_a = false;
            } catch (Exception var6) {
                log.log(Level.INFO,String.format("An error occured reading texture file data %s (refreshTexture)", var13),var6);
                var6.printStackTrace();
            }
        }

    }
}
