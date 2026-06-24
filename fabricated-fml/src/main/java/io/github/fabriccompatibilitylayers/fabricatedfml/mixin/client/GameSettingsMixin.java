/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.GameSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameSettings.class)
public class GameSettingsMixin {
    @WrapMethod(method = "func_74303_b")
    private void fml$isClientLoading(Operation<Void> original) {
        if (!FMLClientHandler.instance().isLoading()) {
            original.call();
        }
    }
}
