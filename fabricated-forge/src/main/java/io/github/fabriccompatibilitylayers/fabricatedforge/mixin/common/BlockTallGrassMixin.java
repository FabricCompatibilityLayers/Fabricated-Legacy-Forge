/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.IPlantableBlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.Random;

@Mixin(BlockTallGrass.class)
public abstract class BlockTallGrassMixin extends BlockFlower implements IShearable, IPlantableBlockExtension {
    protected BlockTallGrassMixin(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return -1;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();
        if (world.rand.nextInt(8) != 0)
        {
            return ret;
        }

        ItemStack item = ForgeHooks.getGrassSeed(world);
        if (item != null)
        {
            ret.add(item);
        }
        return ret;
    }

    @Override
    public boolean isShearable(ItemStack item, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
        return ret;
    }
}
