/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items;

import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ItemData;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.Object2ItemStackMapMapper;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(value = FurnaceRecipes.class, priority = 1010)
public class FurnaceRecipesMixin {
    @Shadow(remap = false)
    private Map<ItemData, ItemStack> metaSmeltingList;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("forge", "furnace_recipes/meta_smelting_list"),
                Object2ItemStackMapMapper.of(metaSmeltingList)
        );
    }
}
