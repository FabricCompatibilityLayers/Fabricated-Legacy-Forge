/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ForgedBlock;
import net.minecraft.src.Block;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockTorch.class)
public abstract class BlockTorchMixin extends Block implements BlockExtension {
    @Shadow protected abstract boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4);

    public BlockTorchMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @ModifyReturnValue(method = "canPlaceTorchOn", at = @At(value = "RETURN", ordinal = 1))
    private boolean forge$canPlaceTorchOnTop(boolean original,
                                             @Local(argsOnly = true) World par1World,
                                             @Local(argsOnly = true, ordinal = 0) int par2,
                                             @Local(argsOnly = true, ordinal = 1) int par3,
                                             @Local(argsOnly = true, ordinal = 2) int par4,
                                             @Local(ordinal = 3) int var5) {
        return (Block.blocksList[var5] != null && ((BlockExtension) Block.blocksList[var5]).canPlaceTorchOnTop(par1World, par2, par3, par4)) || original;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST,  true) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST,  true) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) ||
                canPlaceTorchOn(par1World, par2, par3 - 1, par4);
    }

    @Redirect(method = {"updateBlockMetadata", "onBlockAdded", "onNeighborBlockChange"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z"))
    private boolean forge$isBlockSolidOnSide(World instance, int x, int y, int z, boolean defaults,
                                             @Local(argsOnly = true, ordinal = 0) int origX,
                                             @Local(argsOnly = true, ordinal = 1) int origY,
                                             @Local(argsOnly = true, ordinal = 2) int origZ) {
        return ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, ForgedBlock.findDirection(x, y, z, origX, origY, origZ), defaults);
    }
}
