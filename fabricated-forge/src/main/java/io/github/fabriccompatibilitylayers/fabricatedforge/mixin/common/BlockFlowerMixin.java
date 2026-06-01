/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.EnumPlantType.*;

@Mixin(BlockFlower.class)
public abstract class BlockFlowerMixin extends Block implements BlockExtension, IPlantable {
    public BlockFlowerMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Redirect(method = "canPlaceBlockAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFlower;canThisPlantGrowOnThisBlockID(I)Z"))
    private boolean forge$canBlockStay(BlockFlower instance, int i, @Local(argsOnly = true) World par1World, @Local(argsOnly = true, ordinal = 0) int par2, @Local(argsOnly = true, ordinal = 1) int par3, @Local(argsOnly = true, ordinal = 2) int par4) {
        return instance.canBlockStay(par1World, par2, par3, par4);
    }

    @Redirect(method = "canBlockStay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFlower;canThisPlantGrowOnThisBlockID(I)Z"))
    private boolean forge$canSustainPlant(BlockFlower instance, int i, @Local(argsOnly = true) World par1World, @Local(argsOnly = true, ordinal = 0) int par2, @Local(argsOnly = true, ordinal = 1) int par3, @Local(argsOnly = true, ordinal = 2) int par4) {
        Block soil = blocksList[par1World.getBlockId(par2, par3 - 1, par4)];
        return soil != null && ((BlockExtension) soil).canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        if (blockID == crops.blockID        ) return Crop;
        if (blockID == deadBush.blockID     ) return Desert;
        if (blockID == waterlily.blockID    ) return Water;
        if (blockID == mushroomRed.blockID  ) return Cave;
        if (blockID == mushroomBrown.blockID) return Cave;
        if (blockID == netherStalk.blockID  ) return Nether;
        if (blockID == sapling.blockID      ) return Plains;
        if (blockID == melonStem.blockID    ) return Crop;
        if (blockID == pumpkinStem.blockID  ) return Crop;
        if (blockID == tallGrass.blockID    ) return Plains;
        return Plains;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z)
    {
        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z);
    }
}
