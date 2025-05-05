package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Random;

public interface BlockExtension {
    int getLightValue(IBlockAccess world, int x, int y, int z);

    boolean isLadder(World world, int x, int y, int z);

    boolean isBlockNormalCube(World world, int x, int y, int z);

    boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side);

    boolean isBlockReplaceable(World world, int x, int y, int z);

    boolean isBlockBurning(World world, int x, int y, int z);

    boolean isAirBlock(World world, int x, int y, int z);

    boolean canHarvestBlock(EntityPlayer player, int meta);

    boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z);

    void addCreativeItems(ArrayList itemList);

    int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face);

    boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face);

    int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face);

    boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side);

    boolean hasTileEntity(int metadata);

    TileEntity createTileEntity(World world, int metadata);

    int quantityDropped(int meta, int fortune, Random random);

    ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune);

    boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata);

    boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z);

    boolean isBed(World world, int x, int y, int z, EntityLiving player);

    ChunkCoordinates getBedSpawnPosition(World world, int x, int y, int z, EntityPlayer player);

    void setBedOccupied(World world, int x, int y, int z, EntityPlayer player, boolean occupied);

    int getBedDirection(IBlockAccess world, int x, int y, int z);

    boolean isBedFoot(IBlockAccess world, int x, int y, int z);

    void beginLeavesDecay(World world, int x, int y, int z);

    boolean canSustainLeaves(World world, int x, int y, int z);

    boolean isLeaves(World world, int x, int y, int z);

    boolean canBeReplacedByLeaves(World world, int x, int y, int z);

    boolean isWood(World world, int x, int y, int z);

    boolean isGenMineableReplaceable(World world, int x, int y, int z);

    String getTextureFile();

    void setTextureFile(String texture);

    float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ);

    boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side);

    boolean canPlaceTorchOnTop(World world, int x, int y, int z);

    boolean canRenderInPass(int pass);

    ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z);

    boolean isBlockFoliage(World world, int x, int y, int z);

    @Environment(EnvType.CLIENT)
    boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer);

    @Environment(EnvType.CLIENT)
    boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer);

    boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant);

    boolean isFertile(World world, int x, int y, int z);

    int getLightOpacity(World world, int x, int y, int z);

    boolean canDragonDestroy(World world, int x, int y, int z);
}
