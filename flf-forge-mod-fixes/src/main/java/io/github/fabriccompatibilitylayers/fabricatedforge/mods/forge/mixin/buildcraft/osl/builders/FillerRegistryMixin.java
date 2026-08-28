/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.builders;

import buildcraft.builders.FillerRegistry;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.FLFFixableRecipe;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.FLFRecipesMapper;
import net.minecraft.src.ItemStack;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Pseudo
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(FillerRegistry.class)
public class FillerRegistryMixin {

    @Shadow
    static LinkedList<Object> recipes;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("buildcraft", "filler_registry/recipes"),
                FLFRecipesMapper.of(recipes));
    }

    @Pseudo
    @Conditional(modLoaded = @Mod("osl-items"))
    @Mixin(targets = "buildcraft.builders.FillerRegistry$ShapedPatternRecipe")
    public static class ShapedPatternRecipeMixin implements FLFFixableRecipe {

        @Shadow
        private ItemStack[] recipeItems;

        @Override
        public boolean flf$osl$canFixRecipe(ItemMapper var1) {
            for (ItemStack stack : recipeItems) {
                if (!var1.canFixItem(stack)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public void flf$osl$fixRecipe(ItemMapper var1) {
            for (ItemStack stack : recipeItems) {
                var1.fixItem(stack);
            }
        }
    }
}
