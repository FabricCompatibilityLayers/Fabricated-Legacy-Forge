/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ForgedBlock;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLadder;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockLadder.class)
public abstract class BlockLadderMixin extends Block implements BlockExtension {
    public BlockLadderMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH);
    }

    @Redirect(method = {"updateBlockMetadata", "onNeighborBlockChange"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"))
    private boolean forge$isBlockSolidOnSide(World instance, int x, int y, int z,
                                               @Local(argsOnly = true, ordinal = 0) int origX,
                                               @Local(argsOnly = true, ordinal = 1) int origY,
                                               @Local(argsOnly = true, ordinal = 2) int origZ) {
        return ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, ForgedBlock.findDirection(x, y, z, origX, origY, origZ));
    }

    @Override
    public boolean isLadder(World world, int x, int y, int z)
    {
        return true;
    }
}
