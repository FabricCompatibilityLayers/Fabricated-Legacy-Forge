/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenShrub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenShrub.class)
public class WorldGenShrubMixin {
    @Expression("? == 0")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$Null(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] == null;
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isLeaves(int blockId, int right, Operation<Boolean> original,
                                   @Local(argsOnly = true) World par1World,
                                   @Local(ordinal = 0, argsOnly = true) int par3,
                                   @Local(ordinal = 1, argsOnly = true) int par4,
                                   @Local(ordinal = 2, argsOnly = true) int par5) {
        return ((BlockExtension) Block.blocksList[blockId]).isLeaves(par1World, par3, par4, par5);
    }

    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canBeReplacedByLeaves(boolean[] array, int blockId, Operation<Boolean> original,
                                                @Local(argsOnly = true) World par1World,
                                                @Local(index = 8) int var8,
                                                @Local(index = 11) int var11,
                                                @Local(index = 13) int var13) {
        Block block = Block.blocksList[blockId];

        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, var11, var8, var13);
    }
}
