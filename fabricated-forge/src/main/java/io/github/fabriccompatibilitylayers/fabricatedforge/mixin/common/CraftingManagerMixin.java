/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin {
    @Redirect(method = "func_82787_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Item;isDamageable()Z"))
    private boolean forge$isRepairable(Item instance) {
        return ((ItemExtension) instance).isRepairable();
    }
}
