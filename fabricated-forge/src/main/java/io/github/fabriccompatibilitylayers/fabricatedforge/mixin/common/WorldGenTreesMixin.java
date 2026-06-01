/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
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
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenTrees;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenTrees.class)
public class WorldGenTreesMixin {

    // Pattern E+H (@WrapOperation on INVOKE + @Share):
    // The canplace coords (var10=x, var8=y, var11=z) are captured once at the getBlockId callsite
    // (ordinal 0 = first call in the method, inside the canplace triple-nested loop) into @Share slots.
    // The two EXPRESSION handlers then read those slots, avoiding @Local(index) inside expression
    // evaluators for loop variables.
    @WrapOperation(method = "generate",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 0))
    private int forge$captureCanplaceCoords(World instance, int x, int y, int z, Operation<Integer> original,
                                             @Share(namespace = "fabricated-forge", value = "canplaceX") LocalIntRef xRef,
                                             @Share(namespace = "fabricated-forge", value = "canplaceY") LocalIntRef yRef,
                                             @Share(namespace = "fabricated-forge", value = "canplaceZ") LocalIntRef zRef) {
        xRef.set(x);
        yRef.set(y);
        zRef.set(z);
        return original.call(instance, x, y, z);
    }

    // Logic delta: patch introduces `Block block = Block.blocksList[var12]` then calls block.isLeaves();
    // here we do the same lookup inline since the @Share already carries the coordinates.
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? != leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$isntLeaves(int var12, int right, Operation<Boolean> original,
                                      @Local(argsOnly = true) World par1World,
                                      @Share(namespace = "fabricated-forge", value = "canplaceX") LocalIntRef xRef,
                                      @Share(namespace = "fabricated-forge", value = "canplaceY") LocalIntRef yRef,
                                      @Share(namespace = "fabricated-forge", value = "canplaceZ") LocalIntRef zRef) {
        Block block = Block.blocksList[var12];
        return !((BlockExtension) block).isLeaves(par1World, xRef.get(), yRef.get(), zRef.get());
    }

    @Definition(id = "wood", field = "Lnet/minecraft/src/Block;wood:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("? != wood.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isntWood(int var12, int right, Operation<Boolean> original,
                                    @Local(argsOnly = true) World par1World,
                                    @Share(namespace = "fabricated-forge", value = "canplaceX") LocalIntRef xRef,
                                    @Share(namespace = "fabricated-forge", value = "canplaceY") LocalIntRef yRef,
                                    @Share(namespace = "fabricated-forge", value = "canplaceZ") LocalIntRef zRef) {
        Block block = Block.blocksList[var12];
        return !((BlockExtension) block).isWood(par1World, xRef.get(), yRef.get(), zRef.get());
    }

    // Pattern E+H: capture leaves-placement loop coords at getBlockId (ordinal 2) into @Share,
    // then use them in the opaqueCubeLookup expression handler.
    @WrapOperation(method = "generate",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 2))
    private int forge$captureLeavesCoords(World instance, int x, int y, int z, Operation<Integer> original,
                                           @Share(namespace = "fabricated-forge", value = "leavesX") LocalIntRef xRef,
                                           @Share(namespace = "fabricated-forge", value = "leavesY") LocalIntRef yRef,
                                           @Share(namespace = "fabricated-forge", value = "leavesZ") LocalIntRef zRef) {
        xRef.set(x);
        yRef.set(y);
        zRef.set(z);
        return original.call(instance, x, y, z);
    }

    // Logic delta: WrapOperation intercepts `opaqueCubeLookup[blockId]` (the boolean array read) and
    // returns `block != null && !block.canBeReplacedByLeaves(...)` — the surrounding `!` in the
    // if-condition then gives `block == null || block.canBeReplacedByLeaves(...)`, matching the patch.
    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$canBeReplacedByLeaves(boolean[] array, int blockId, Operation<Boolean> original,
                                                 @Local(argsOnly = true) World par1World,
                                                 @Share(namespace = "fabricated-forge", value = "leavesX") LocalIntRef xRef,
                                                 @Share(namespace = "fabricated-forge", value = "leavesY") LocalIntRef yRef,
                                                 @Share(namespace = "fabricated-forge", value = "leavesZ") LocalIntRef zRef) {
        Block block = Block.blocksList[blockId];
        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, xRef.get(), yRef.get(), zRef.get());
    }

    // Pattern E+H: capture trunk-loop getBlockId coords (ordinal 3) into @Share.
    @WrapOperation(method = "generate",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 3))
    private int forge$captureTrunkCoords(World instance, int x, int y, int z, Operation<Integer> original,
                                          @Share(namespace = "fabricated-forge", value = "trunkX") LocalIntRef xRef,
                                          @Share(namespace = "fabricated-forge", value = "trunkY") LocalIntRef yRef,
                                          @Share(namespace = "fabricated-forge", value = "trunkZ") LocalIntRef zRef) {
        xRef.set(x);
        yRef.set(y);
        zRef.set(z);
        return original.call(instance, x, y, z);
    }

    // Logic delta: patch adds `block == null ||` as a null-safety guard before `block.isLeaves(...)`
    // (for unregistered block IDs); the `var26 == 0` air check is unchanged and not intercepted here.
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$isLeavesForTrunk(int var26, int right, Operation<Boolean> original,
                                            @Local(argsOnly = true) World par1World,
                                            @Share(namespace = "fabricated-forge", value = "trunkX") LocalIntRef xRef,
                                            @Share(namespace = "fabricated-forge", value = "trunkY") LocalIntRef yRef,
                                            @Share(namespace = "fabricated-forge", value = "trunkZ") LocalIntRef zRef) {
        Block block = Block.blocksList[var26];
        return block == null || ((BlockExtension) block).isLeaves(par1World, xRef.get(), yRef.get(), zRef.get());
    }

    // Pattern E+H: capture vine-loop getBlockId coords (ordinal 4) into @Share.
    @WrapOperation(method = "generate",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 4))
    private int forge$captureVineCoords(World instance, int x, int y, int z, Operation<Integer> original,
                                         @Share(namespace = "fabricated-forge", value = "vineX") LocalIntRef xRef,
                                         @Share(namespace = "fabricated-forge", value = "vineY") LocalIntRef yRef,
                                         @Share(namespace = "fabricated-forge", value = "vineZ") LocalIntRef zRef) {
        xRef.set(x);
        yRef.set(y);
        zRef.set(z);
        return original.call(instance, x, y, z);
    }

    // Logic delta: hunk 3 returned `block == null || isLeaves(...)` (allows null); here the patch
    // requires `block != null && isLeaves(...)` — vines only sprout on confirmed leaf blocks.
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$isLeavesForVines(int blockId, int right, Operation<Boolean> original,
                                            @Local(argsOnly = true) World par1World,
                                            @Share(namespace = "fabricated-forge", value = "vineX") LocalIntRef xRef,
                                            @Share(namespace = "fabricated-forge", value = "vineY") LocalIntRef yRef,
                                            @Share(namespace = "fabricated-forge", value = "vineZ") LocalIntRef zRef) {
        Block block = Block.blocksList[blockId];
        return block != null && ((BlockExtension) block).isLeaves(par1World, xRef.get(), yRef.get(), zRef.get());
    }
}