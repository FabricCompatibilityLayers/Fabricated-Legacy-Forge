package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public interface WorldExtension {
    @Environment(EnvType.CLIENT)
    Vec3 drawCloudsBody(float par1);
    boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4);
    boolean canSnowAtBody(int par1, int par2, int par3);
    boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4);
    void updateWeatherBody();
    void calculateInitialWeatherBody();
    @Environment(EnvType.CLIENT)
    float getStarBrightnessBody(float par1);
    @Environment(EnvType.CLIENT)
    Vec3 getSkyColorBody(Entity par1Entity, float par2);
    @Environment(EnvType.CLIENT)
    void finishSetup();
    BiomeGenBase getBiomeGenForCoordsBody(int par1, int par2);
    void addTileEntity(TileEntity entity);
    boolean isBlockSolidOnSide(int X, int Y, int Z, ForgeDirection side);
    boolean isBlockSolidOnSide(int X, int Y, int Z, ForgeDirection side, boolean _default);
}
