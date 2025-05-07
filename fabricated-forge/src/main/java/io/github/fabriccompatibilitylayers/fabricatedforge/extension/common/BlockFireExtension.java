package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

public interface BlockFireExtension extends BlockExtension {
    void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7, ForgeDirection face);

    boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face);

    int getChanceToEncourageFire(World world, int x, int y, int z, int oldChance, ForgeDirection face);
}
