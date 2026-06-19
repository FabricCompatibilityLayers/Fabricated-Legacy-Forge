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
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenForest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenForest.class)
public class WorldGenForestMixin {
    @IfModAbsent("osl-blocks")
    @Expression("? != 0")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$NonNull(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] != null;
    }

    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 0))
    private int forge$capturePos0(World instance, int par2, int par3, int i, Operation<Integer> original,
                                  @Share(namespace = "fabricated-forge", value = "toleavePos0") LocalRef<ChunkCoordinates> posRef) {
        posRef.set(new ChunkCoordinates(par2, par3, i));

        return original.call(instance, par2, par3, i);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? != leaves.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isntLeaves(int var12, int right, Operation<Boolean> original,
                                     @Local(argsOnly = true) World par1World,
                                     @Share(namespace = "fabricated-forge", value = "toleavePos0") LocalRef<ChunkCoordinates> posRef) {
        Block block = Block.blocksList[var12];
        ChunkCoordinates pos = posRef.get();

        return !((BlockExtension) block).isLeaves(par1World, pos.posX,  pos.posY, pos.posZ);
    }

    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 2))
    private int forge$capturePos1(World instance, int par2, int par3, int i, Operation<Integer> original,
                                  @Share(namespace = "fabricated-forge", value = "toleavePos") LocalRef<ChunkCoordinates> posRef) {
        posRef.set(new ChunkCoordinates(par2, par3, i));

        return original.call(instance, par2, par3, i);
    }

    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$canBeReplacedByLeaves(boolean[] array, int blockId, Operation<Boolean> original,
                                                @Local(argsOnly = true) World par1World,
                                                @Share(namespace = "fabricated-forge", value = "toleavePos") LocalRef<ChunkCoordinates> posRef) {
        Block block = Block.blocksList[blockId];
        ChunkCoordinates pos = posRef.get();

        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, pos.posX, pos.posY, pos.posZ);
    }

    @IfModAbsent("osl-blocks")
    @Expression("? == 0")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$Null(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] == null;
    }

    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 3))
    private int forge$captureLeavePos2(World instance, int par2, int par3, int i, Operation<Integer> original,
                                       @Share(namespace = "fabricated-forge", value = "leavePos")LocalRef<ChunkCoordinates> posRef) {
        posRef.set(new ChunkCoordinates(par2, par3, i));
        return original.call(instance, par2, par3, i);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$isLeaves(int var20, int right, Operation<Boolean> original,
                                   @Local(argsOnly = true) World par1World,
                                   @Share(namespace = "fabricated-forge", value = "leavePos")LocalRef<ChunkCoordinates> posRef) {
        Block block = Block.blocksList[var20];
        ChunkCoordinates pos = posRef.get();

        return !((BlockExtension) block).isLeaves(par1World, pos.posX, pos.posY, pos.posZ);
    }
}
