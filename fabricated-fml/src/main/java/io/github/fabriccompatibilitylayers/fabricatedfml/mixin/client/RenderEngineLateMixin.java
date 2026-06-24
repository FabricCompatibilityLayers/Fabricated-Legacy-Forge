/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLLog;
import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedfml.utils.WidenedCatch;
import net.minecraft.src.RenderEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(value = RenderEngine.class, priority = 9999)
public abstract class RenderEngineLateMixin {
    @Shadow
    @WidenedCatch(from = "java/io/IOException", to = "java/lang/Exception")
    public abstract int[] func_78346_a(String par1);

    @Shadow
    @WidenedCatch(from = "java/io/IOException", to = "java/lang/Exception")
    public abstract void func_78352_b();

    @Public
    private static Logger log = FMLLog.getLogger();

    @Inject(method = "func_78346_a", at = @At(value = "INVOKE", target = "Ljava/lang/Exception;printStackTrace()V", remap = false))
    private void fml$logError(String p_78346_1_, CallbackInfoReturnable<int[]> cir, @Local(ordinal = 0) Exception var6) {
        log.log(Level.INFO, String.format("An error occured reading texture file %s (getTexture)", p_78346_1_), var6);
    }

    @Inject(method = "func_78352_b", at = @At(value = "INVOKE", target = "Ljava/lang/Exception;printStackTrace()V", remap = false))
    private void fml$logError1(CallbackInfo cir, @Local(ordinal = 0) String var12, @Local(ordinal = 0) Exception var7) {
        log.log(Level.INFO, String.format("An error occured reading texture file %s (refreshTexture)", var12), var7);
    }
}
