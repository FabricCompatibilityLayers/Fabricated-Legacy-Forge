package fr.catcore.fabricatedforge.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.SkyProvider;

public interface IDimension {
    void setDimension(int dim);

    String getSaveFolder();

    String getWelcomeMessage();

    String getDepartMessage();

    double getMovementFactor();

    @Environment(EnvType.CLIENT)
    SkyProvider getSkyProvider();

    @Environment(EnvType.CLIENT)
    void setSkyProvider(SkyProvider skyProvider);

    @Environment(EnvType.CLIENT)
    SkyProvider getCloudRenderer();

    @Environment(EnvType.CLIENT)
    void setCloudRenderer(SkyProvider renderer);

    BlockPos getRandomizedSpawnPoint();

    boolean shouldMapSpin(String entity, double x, double y, double z);

    int getRespawnDimension(ServerPlayerEntity player);

    Biome getBiomeGenForCoords(int x, int z);

    boolean isDaytime();

    @Environment(EnvType.CLIENT)
    Vec3d getSkyColor(Entity cameraEntity, float partialTicks);

    @Environment(EnvType.CLIENT)
    Vec3d drawClouds(float partialTicks);

    @Environment(EnvType.CLIENT)
    float getStarBrightness(float par1);

    void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful);

    void calculateInitialWeather();

    void updateWeather();

    void toggleRain();

    boolean canBlockFreeze(int x, int y, int z, boolean byWater);

    boolean canSnowAt(int x, int y, int z);

    void setWorldTime(long time);

    long getSeed();

    long getWorldTime();

    BlockPos getSpawnPoint();

    void setSpawnPoint(int x, int y, int z);

    boolean canMineBlock(PlayerEntity player, int x, int y, int z);

    boolean isBlockHighHumidity(int x, int y, int z);

    int getHeight();

    int getActualHeight();

    double getHorizon();

    void resetRainAndThunder();

    boolean canDoLightning(Chunk chunk);

    boolean canDoRainSnowIce(Chunk chunk);
}
