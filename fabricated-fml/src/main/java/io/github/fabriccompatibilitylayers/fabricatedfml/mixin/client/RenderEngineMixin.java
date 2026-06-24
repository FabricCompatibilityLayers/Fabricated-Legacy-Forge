/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
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
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

@Mixin(RenderEngine.class)
public abstract class RenderEngineMixin {
    @Shadow public List field_78367_h;
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

    @Inject(method = "func_78343_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;func_82772_a(Lnet/minecraft/src/TextureFX;I)I"))
    private void fml$onUpdateTextureEffect(CallbackInfo ci,
                                           @Local(ordinal = 0)LocalIntRef var1,
                                           @Local TextureFX var3) {
        if (TextureFXManager.instance().onUpdateTextureEffect(var3)) {
            var1.set(this.func_82772_a(var3, var1.get()));
        }
    }

    /**
     * @author CatCore
     * @reason logic change
     */
    @Overwrite
    public int func_82772_a(TextureFX p_82772_1_, int p_82772_2_) {
        Dimension dim = TextureFXManager.instance().getTextureDimensions(p_82772_1_);
        int tWidth  = dim.width >> 4;
        int tHeight = dim.height >> 4;
        int tLen = tWidth * tHeight << 2;

        if (p_82772_1_.field_76852_a.length == tLen)
        {
            this.field_78358_g.clear();
            this.field_78358_g.put(p_82772_1_.field_76852_a);
            this.field_78358_g.position(0).limit(p_82772_1_.field_76852_a.length);
        }
        else
        {
            TextureFXManager.instance().scaleTextureFXData(p_82772_1_.field_76852_a, field_78358_g, tWidth, tLen);
        }

        if (p_82772_1_.field_76850_b != p_82772_2_) {
            p_82772_1_.func_76845_a((RenderEngine) (Object) this);
            p_82772_2_ = p_82772_1_.field_76850_b;
        }

        for(int var3 = 0; var3 < p_82772_1_.field_76849_e; ++var3) {
            int xOffset = p_82772_1_.field_76850_b % 16 * tWidth + var3 * tWidth;
            for(int var4 = 0; var4 < p_82772_1_.field_76849_e; ++var4) {
                int yOffset = p_82772_1_.field_76850_b / 16 * tHeight + var4 * tHeight;
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, xOffset, yOffset, tWidth, tHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.field_78358_g);
            }
        }

        return p_82772_2_;
    }
}
