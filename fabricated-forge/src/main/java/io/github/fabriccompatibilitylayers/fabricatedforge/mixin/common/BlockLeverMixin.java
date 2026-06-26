/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ForgedBlock;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLever;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockLever.class)
public abstract class BlockLeverMixin extends Block implements BlockExtension {
    public BlockLeverMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(par5);
        return (dir == DOWN  && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 + 1, par4, DOWN )) ||
                (dir == UP    && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 - 1, par4, UP   )) ||
                (dir == NORTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) ||
                (dir == SOUTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
                (dir == WEST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST )) ||
                (dir == EAST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST ));
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
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 - 1, par4, UP   ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 + 1, par4, DOWN );
    }

    @WrapOperation(method = {"updateBlockMetadata", "onNeighborBlockChange"}, at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/World;doesBlockHaveSolidTopSurface(III)Z")
    })
    private boolean forge$isBlockSolidOnSide(World instance, int x, int y, int z, Operation<Boolean> original,
                                             @Local(argsOnly = true, ordinal = 0) int origX,
                                             @Local(argsOnly = true, ordinal = 1) int origY,
                                             @Local(argsOnly = true, ordinal = 2) int origZ) {
        return ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, ForgedBlock.findDirection(x, y, z, origX, origY, origZ));
    }
}
