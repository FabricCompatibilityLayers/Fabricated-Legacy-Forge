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
import net.minecraft.src.BlockTripWireSource;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockTripWireSource.class)
public abstract class BlockTripWireSourceMixin extends Block implements BlockExtension {
    public BlockTripWireSourceMixin(int par1, Material par2Material) {
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
        return (dir == NORTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) ||
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
        return ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, SOUTH) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, NORTH) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, EAST ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, WEST );
    }

    @Redirect(method = {"updateBlockMetadata"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z"))
    private boolean forge$isBlockSolidOnSide$1(World instance, int x, int y, int z, boolean defaults,
                                             @Local(argsOnly = true, ordinal = 0) int origX,
                                             @Local(argsOnly = true, ordinal = 1) int origY,
                                             @Local(argsOnly = true, ordinal = 2) int origZ) {
        return ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, ForgedBlock.findDirection(x, y, z, origX, origY, origZ), defaults);
    }

    @WrapOperation(method = {"onNeighborBlockChange"}, at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z")
    })
    private boolean forge$isBlockSolidOnSide$2(World instance, int x, int y, int z, Operation<Boolean> original,
                                             @Local(argsOnly = true, ordinal = 0) int origX,
                                             @Local(argsOnly = true, ordinal = 1) int origY,
                                             @Local(argsOnly = true, ordinal = 2) int origZ) {
        return ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, ForgedBlock.findDirection(x, y, z, origX, origY, origZ));
    }

    @Redirect(method = "func_72143_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;doesBlockHaveSolidTopSurface(III)Z"))
    private boolean forge$isBlockSolidOnSide$3(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, WEST);
    }
}
