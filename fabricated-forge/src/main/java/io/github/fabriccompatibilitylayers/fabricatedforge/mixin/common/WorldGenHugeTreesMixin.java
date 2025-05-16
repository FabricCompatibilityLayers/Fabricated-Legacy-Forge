package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenHugeTrees;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenHugeTrees.class)
public class WorldGenHugeTreesMixin {
    @Expression("? != 0")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$NonNull(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] != null;
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? != leaves.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isLeaves(int blockId, int right, Operation<Boolean> original,
                                   @Local(argsOnly = true) World par1World,
                                   @Local(index = 8) int var8,
                                   @Local(index = 10) int var10,
                                   @Local(index = 11) int var11) {
        Block block = Block.blocksList[blockId];

        return !((BlockExtension) block).isLeaves(par1World, var10, var8, var11);
    }

    @Definition(id = "wood", field = "Lnet/minecraft/src/Block;wood:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("? != wood.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isWood(int blockId, int right, Operation<Boolean> original,
                                   @Local(argsOnly = true) World par1World,
                                   @Local(index = 8) int var8,
                                   @Local(index = 10) int var10,
                                   @Local(index = 11) int var11) {
        Block block = Block.blocksList[blockId];

        return !((BlockExtension) block).isWood(par1World, var10, var8, var11);
    }

    @Expression("? == 0")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$Null(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] == null;
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$isLeaves1(int blockId, int right, Operation<Boolean> original,
                                    @Local(argsOnly = true) World par1World,
                                    @Local(ordinal = 0, argsOnly = true) int par3,
                                    @Local(ordinal = 1, argsOnly = true) int par4,
                                    @Local(ordinal = 2, argsOnly = true) int par5,
                                    @Local(index = 17) int var17) {
        Block block = Block.blocksList[blockId];

        return ((BlockExtension) block).isLeaves(par1World, par3, par4 + var17, par5);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$isLeaves2(int blockId, int right, Operation<Boolean> original,
                                    @Local(argsOnly = true) World par1World,
                                    @Local(ordinal = 0, argsOnly = true) int par3,
                                    @Local(ordinal = 1, argsOnly = true) int par4,
                                    @Local(ordinal = 2, argsOnly = true) int par5,
                                    @Local(index = 17) int var17) {
        Block block = Block.blocksList[blockId];

        return ((BlockExtension) block).isLeaves(par1World, par3 + 1, par4 + var17, par5);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 2))
    private boolean forge$isLeaves3(int blockId, int right, Operation<Boolean> original,
                                    @Local(argsOnly = true) World par1World,
                                    @Local(ordinal = 0, argsOnly = true) int par3,
                                    @Local(ordinal = 1, argsOnly = true) int par4,
                                    @Local(ordinal = 2, argsOnly = true) int par5,
                                    @Local(index = 17) int var17) {
        Block block = Block.blocksList[blockId];

        return ((BlockExtension) block).isLeaves(par1World, par3 + 1, par4 + var17, par5 + 1);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 3))
    private boolean forge$isLeaves4(int blockId, int right, Operation<Boolean> original,
                                    @Local(argsOnly = true) World par1World,
                                    @Local(ordinal = 0, argsOnly = true) int par3,
                                    @Local(ordinal = 1, argsOnly = true) int par4,
                                    @Local(ordinal = 2, argsOnly = true) int par5,
                                    @Local(index = 17) int var17) {
        Block block = Block.blocksList[blockId];

        return ((BlockExtension) block).isLeaves(par1World, par3, par4 + var17, par5 + 1);
    }

    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "growLeaves", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canBeReplacedByLeaves(boolean[] array, int blockId, Operation<Boolean> original,
                                                @Local(argsOnly = true) World par1World,
                                                @Local(index = 8) int var8,
                                                @Local(index = 11) int var11,
                                                @Local(index = 13) int var13) {
        Block block = Block.blocksList[blockId];

        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, var11, var8, var13);
    }
}
