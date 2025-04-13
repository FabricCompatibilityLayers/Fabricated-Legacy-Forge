package fr.catcore.fabricatedforge.mixininterface;

import com.google.common.collect.SetMultimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.level.LevelProperties;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeDirection;

public interface IWorld {
    Biome getBiomeGenForCoordsBody(int par1, int par2);

    @Environment(EnvType.CLIENT)
    void finishSetup();

    @Environment(EnvType.CLIENT)
    Vec3d getSkyColorBody(Entity par1Entity, float par2);

    @Environment(EnvType.CLIENT)
    Vec3d drawCloudsBody(float par1);

    @Environment(EnvType.CLIENT)
    float getStarBrightnessBody(float par1);

    void calculateInitialWeatherBody();

    void updateWeatherBody();

    boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4);

    boolean canSnowAtBody(int par1, int par2, int par3);

    boolean canMineBlockBody(PlayerEntity par1EntityPlayer, int par2, int par3, int par4);

    void addTileEntity(BlockEntity entity);

    boolean isBlockSolidOnSide(int X, int Y, int Z, ForgeDirection side);

    boolean isBlockSolidOnSide(int X, int Y, int Z, ForgeDirection side, boolean _default);

    SetMultimap<ChunkPos, ForgeChunkManager.Ticket> getPersistentChunks();

    LevelProperties getLevelProperties();

    void setSpawnAnimals(boolean bool);
    void setSpawnMonsters(boolean bool);
}
