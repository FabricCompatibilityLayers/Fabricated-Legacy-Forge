/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.src.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {
    @Inject(method = "func_71197_b", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V", ordinal = 1, remap = false))
    private void fml$onServerStart(CallbackInfoReturnable<Boolean> cir) {
        FMLCommonHandler.instance().onServerStart((DedicatedServer)(Object) this);
    }

    @Inject(method = "func_71197_b", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/DedicatedServer;func_71210_a(Lnet/minecraft/src/ServerConfigurationManager;)V"))
    private void fml$onServerStarted(CallbackInfoReturnable<Boolean> cir) {
        FMLCommonHandler.instance().onServerStarted();
    }

    @Inject(method = "func_71197_b", at = @At("RETURN"))
    private void fml$handleServerStarting(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            FMLCommonHandler.instance().handleServerStarting((DedicatedServer)(Object) this);
        }
    }
}
