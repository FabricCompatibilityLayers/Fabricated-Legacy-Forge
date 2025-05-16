package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigMushroom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenBigMushroom.class)
public class WorldGenBigMushroomMixin {
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? != leaves.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isLeaves(int var13, int right, Operation<Boolean> original,
                                   @Local(argsOnly = true) World par1World,
                                   @Local(index = 9) int var9,
                                   @Local(index = 11) int var11,
                                   @Local(index = 12) int var12) {
        Block block = Block.blocksList[var13];
        return block != null && !((BlockExtension) block).isLeaves(par1World, var11, var9, var12);
    }

    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$canBeReplacedByLeaves1(boolean[] array, int blockId, Operation<Boolean> original,
                                                 @Local(argsOnly = true) World par1World,
                                                 @Local(index = 11) int var11,
                                                 @Local(index = 13) int var13,
                                                 @Local(index = 14) int var14) {
        Block block = Block.blocksList[blockId];

        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, var13, var11, var14);
    }

    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$canBeReplacedByLeaves2(boolean[] array, int blockId, Operation<Boolean> original,
                                                 @Local(argsOnly = true) World par1World,
                                                 @Local(argsOnly = true, ordinal = 0) int par3,
                                                 @Local(argsOnly = true, ordinal = 1) int par4,
                                                 @Local(argsOnly = true, ordinal = 2) int par5,
                                                 @Local(index = 11) int var11) {
        Block block = Block.blocksList[blockId];

        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, par3, par4 + var11, par5);
    }
}
