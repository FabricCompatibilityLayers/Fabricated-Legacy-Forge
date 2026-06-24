/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.client.TextureFXManager;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

@Mixin(RenderEngine.class)
public abstract class RenderEngineMixin {
    @Shadow public List field_78367_h;
    @Shadow private GameSettings field_78365_j;
    @Shadow private ByteBuffer field_78358_g;

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
                ((Buffer) this.field_78358_g).clear();
                this.field_78358_g.put(var3.field_76852_a);
                ((Buffer) this.field_78358_g).position(0).limit(var3.field_76852_a.length);
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
}
