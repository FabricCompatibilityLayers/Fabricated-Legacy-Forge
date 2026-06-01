/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public class GameSettingsMixin {
    @Inject(method = "func_74303_b", at = @At("HEAD"), cancellable = true)
    private void fml$isClientLoading(CallbackInfo ci) {
        if (FMLClientHandler.instance().isLoading()) ci.cancel();
    }
}
