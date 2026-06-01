/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.client.SkyProvider;

public interface WorldProviderExtension {
    void setDimension(int dim);

    String getSaveFolder();

    String getWelcomeMessage();

    String getDepartMessage();

    double getMovementFactor();

    @Environment(EnvType.CLIENT)
    SkyProvider getSkyProvider();

    @Environment(EnvType.CLIENT)
    void setSkyProvider(SkyProvider skyProvider);

    ChunkCoordinates getRandomizedSpawnPoint();

    BiomeGenBase getBiomeGenForCoords(int x, int z);

    boolean isDaytime();

    @Environment(EnvType.CLIENT)
    Vec3 getSkyColor(Entity cameraEntity, float partialTicks);

    @Environment(EnvType.CLIENT)
    Vec3 drawClouds(float partialTicks);

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

    ChunkCoordinates getSpawnPoint();

    void setSpawnPoint(int x, int y, int z);

    boolean canMineBlock(EntityPlayer player, int x, int y, int z);

    boolean isBlockHighHumidity(int x, int y, int z);

    int getHeight();

    int getActualHeight();

    double getHorizon();

    void resetRainAndThunder();

    boolean canDoLightning(Chunk chunk);

    boolean canDoRainSnowIce(Chunk chunk);
}
