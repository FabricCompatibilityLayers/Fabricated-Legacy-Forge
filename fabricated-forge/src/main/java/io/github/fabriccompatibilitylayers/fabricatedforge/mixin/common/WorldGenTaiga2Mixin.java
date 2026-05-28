package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenTaiga2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenTaiga2.class)
public class WorldGenTaiga2Mixin {
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? != leaves.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isntLeaves(int blockId, int right, Operation<Boolean> original,
                                     @Local(argsOnly = true) World par1World,
                                     @Local(index = 11) int var11,
                                     @Local(index = 13) int var13,
                                     @Local(index = 14) int var14) {
        Block block = Block.blocksList[blockId];

        return block != null && !((BlockExtension) block).isLeaves(par1World, var13, var11, var14);
    }

    @Definition(id = "opaqueCubeLookup", field = "Lnet/minecraft/src/Block;opaqueCubeLookup:[Z")
    @Expression("opaqueCubeLookup[?]")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canBeReplacedByLeaves(boolean[] array, int blockId, Operation<Boolean> original,
                                                @Local(argsOnly = true) World par1World,
                                                @Local(index = 16) int var16,
                                                @Local(index = 17) int var17,
                                                @Local(index = 19) int var19) {
        Block block = Block.blocksList[blockId];

        return block != null && !((BlockExtension) block).canBeReplacedByLeaves(par1World, var17, var16, var19);
    }

    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 3))
    private int forge$captureLeavePos(World instance, int par2, int par3, int i, Operation<Integer> original,
                                      @Share(namespace = "fabricated-forge", value = "leavePos")LocalRef<ChunkCoordinates> posRef) {
        posRef.set(new ChunkCoordinates(par2, par3, i));
        return original.call(instance, par2, par3, i);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$isLeaves1(int blockId, int right, Operation<Boolean> original,
                                    @Local(argsOnly = true) World par1World,
                                    @Share(namespace = "fabricated-forge", value = "leavePos")LocalRef<ChunkCoordinates> posRef) {
        Block block = Block.blocksList[blockId];
        ChunkCoordinates pos = posRef.get();

        return block != null && ((BlockExtension) block).isLeaves(par1World, pos.posX, pos.posY, pos.posZ);
    }
}
