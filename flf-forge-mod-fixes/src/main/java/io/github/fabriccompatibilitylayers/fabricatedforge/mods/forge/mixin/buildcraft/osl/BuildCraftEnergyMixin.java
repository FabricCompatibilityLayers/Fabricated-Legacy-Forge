/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl;

import buildcraft.BuildCraftEnergy;
import buildcraft.api.fuels.IronEngineFuel;
import buildcraft.api.liquids.LiquidStack;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemUtils;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(BuildCraftEnergy.class)
public class BuildCraftEnergyMixin {
    @Definition(id = "LiquidStack", type = LiquidStack.class)
    @Expression("new LiquidStack(?, ?, ?)")
    @WrapOperation(method = "initialize", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private static LiquidStack osl$fixRefineryRecipe(int itemID, int amount, int itemDamage, Operation<LiquidStack> original) {
        return original.call(ItemUtils.itemId(itemID), amount, itemDamage);
    }

    @Definition(id = "IronEngineFuel", type = IronEngineFuel.class)
    @Expression("new IronEngineFuel(?, ?, ?)")
    @WrapOperation(method = "initialize", at = {
            @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0),
            @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1)
    })
    private static IronEngineFuel osl$fixIronEngineFuel(int liquidId, float powerPerCycle, int totalBurningTime, Operation<IronEngineFuel> original) {
        return original.call(ItemUtils.itemId(liquidId), powerPerCycle, totalBurningTime);
    }
}
