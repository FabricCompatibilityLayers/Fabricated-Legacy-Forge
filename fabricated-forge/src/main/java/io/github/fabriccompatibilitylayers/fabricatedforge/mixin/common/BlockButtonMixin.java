/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ForgedBlock;
import net.minecraft.src.Block;
import net.minecraft.src.BlockButton;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockButton.class)
public abstract class BlockButtonMixin extends Block implements BlockExtension {
    public BlockButtonMixin(int par1, Material par2Material) {
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
                (dir == WEST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) ||
                (dir == EAST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return (((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST)) ||
                (((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) ||
                (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
                (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH));
    }

    @Inject(method = "updateBlockMetadata", at = @At("HEAD"))
    private void forge$computeDirection(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, CallbackInfo ci,
                                        @Share(namespace = "forge", value = "dir")LocalRef<ForgeDirection> dirRef) {
        dirRef.set(ForgeDirection.getOrientation(par5));
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"))
    private boolean forge$isBlockSolidOnSide$1(World instance, int x, int y, int z,
                                               @Local(argsOnly = true, ordinal = 0) int origX,
                                               @Local(argsOnly = true, ordinal = 1) int origY,
                                               @Local(argsOnly = true, ordinal = 2) int origZ,
                                               @Share(namespace = "forge", value = "dir")LocalRef<ForgeDirection> dirRef) {
        ForgeDirection expectedDir = dirRef.get();
        return expectedDir == ForgedBlock.findDirection(x, y, z, origX, origY, origZ) && ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, expectedDir);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private int getOrientation(World par1World, int par2, int par3, int par4)
    {
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST)) return 1;
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) return 2;
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) return 3;
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) return 4;
        return 1;
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"))
    private boolean forge$isBlockSolidOnSide$2(World instance, int x, int y, int z,
                                               @Local(argsOnly = true, ordinal = 0) int origX,
                                               @Local(argsOnly = true, ordinal = 1) int origY,
                                               @Local(argsOnly = true, ordinal = 2) int origZ) {
        return ((WorldExtension) instance).isBlockSolidOnSide(x, y, z, ForgedBlock.findDirection(x, y, z, origX, origY, origZ));
    }
}
