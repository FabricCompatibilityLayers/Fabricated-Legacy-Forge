/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.IPlantableBlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(BlockStem.class)
public abstract class BlockStemMixin extends BlockFlower implements IPlantableBlockExtension {
    @Shadow private Block fruitType;

    protected BlockStemMixin(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
    }

    @Redirect(method = "updateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;blockID:I", ordinal = 4))
    private int forge$isSoil(Block instance,
                             @Local(argsOnly = true) World par1World,
                             @Local(argsOnly = true, ordinal = 1) int par3,
                             @Local(ordinal = 5) int var9,
                             @Local(ordinal = 6) int var10,
                             @Local(ordinal = 7) int var11) {
        boolean isSoil = (blocksList[var11] != null && ((BlockExtension) blocksList[var11]).canSustainPlant(par1World, var9, par3 - 1, var10, ForgeDirection.UP, this));
        return isSoil ? var11 : -2;
    }

    @Redirect(method = "getGrowthModifier", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;blockID:I"))
    private int forge$canSustainPlant(Block instance,
                                      @Local(argsOnly = true) World par1World,
                                      @Local(argsOnly = true, ordinal = 1) int par3,
                                      @Local(ordinal = 11) int var17,
                                      @Local(ordinal = 12) int var18,
                                      @Local(ordinal = 13) int var19) {
        return (blocksList[var19] != null && ((BlockExtension) blocksList[var19]).canSustainPlant(par1World, var17, par3 - 1, var18, ForgeDirection.UP, this))
                ? var19 : -2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            if (world.rand.nextInt(15) <= metadata)
            {
                ret.add(new ItemStack(fruitType == pumpkin ? Item.pumpkinSeeds : Item.melonSeeds));
            }
        }

        return ret;
    }
}
