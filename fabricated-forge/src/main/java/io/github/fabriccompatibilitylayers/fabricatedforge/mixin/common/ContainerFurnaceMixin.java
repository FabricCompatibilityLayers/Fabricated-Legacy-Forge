/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.FurnaceRecipesExtension;
import net.minecraft.src.ContainerFurnace;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerFurnace.class)
public class ContainerFurnaceMixin {
    @Redirect(method = "func_82846_b", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FurnaceRecipes;getSmeltingResult(I)Lnet/minecraft/src/ItemStack;"))
    private ItemStack forge$getSmeltingResult(FurnaceRecipes instance, int i,
                                              @Local(ordinal = 1) ItemStack var5) {
        return ((FurnaceRecipesExtension) instance).getSmeltingResult(var5);
    }
}
