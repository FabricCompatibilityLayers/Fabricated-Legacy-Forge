/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.recipes;

import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.recipes.RefineryRecipe;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.FLFFixableRecipe;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.FLFRecipesMapper;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Pseudo
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(RefineryRecipe.class)
public class RefineryRecipeMixin implements FLFFixableRecipe {
    @Shadow
    @Final
    public LiquidStack ingredient1;

    @Shadow
    @Final
    public LiquidStack ingredient2;

    @Shadow
    @Final
    public LiquidStack result;

    @Shadow
    private static LinkedList<Object> recipes;

    @Override
    public boolean flf$osl$canFixRecipe(ItemMapper var1) {
        return var1.canFixItem(ingredient1.itemID) && var1.canFixItem(ingredient2.itemID) && var1.canFixItem(result.itemID);
    }

    @Override
    public void flf$osl$fixRecipe(ItemMapper var1) {
        ingredient1.itemID = var1.mapItem(ingredient1.itemID);
        ingredient2.itemID = var1.mapItem(ingredient2.itemID);
        result.itemID = var1.mapItem(result.itemID);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("buildcraft", "refinery_recipe/recipes"),
                FLFRecipesMapper.of(recipes));
    }
}
