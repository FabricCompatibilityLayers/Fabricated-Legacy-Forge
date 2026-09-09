/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.blueprint;

import buildcraft.api.blueprints.BptBlock;
import buildcraft.api.bptblocks.*;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Conditional(modLoaded = {@Mod("osl-items"), @Mod("osl-blocks")})
@Mixin(value = {
        BptBlock.class,
        BptBlockIgnore.class,
        BptBlockIgnoreMeta.class,
        BptBlockLever.class,
        BptBlockPumpkin.class,
        BptBlockRotateMeta.class,
        BptBlockStairs.class,
        BptBlockWallSide.class
})
public class BptBlockFixStackMixin {
    @Definition(id = "ItemStack", type = ItemStack.class)
    @Expression("new ItemStack(?, ?, ?)")
    @WrapOperation(method = "addRequirements", at = @At("MIXINEXTRAS:EXPRESSION"))
    private ItemStack osl$fixItemStack(int par2, int par3, int i, Operation<ItemStack> original) {
        return new ItemStack(Block.blocksList[par2], par3, i);
    }
}
