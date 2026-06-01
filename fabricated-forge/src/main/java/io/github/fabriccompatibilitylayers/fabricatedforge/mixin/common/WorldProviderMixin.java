/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldTypeExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.client.SkyProvider;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldProvider.class)
public class WorldProviderMixin implements WorldProviderExtension {
    @Shadow public int dimensionId;

    @Shadow public World worldObj;

    @Shadow public WorldType terrainType;

    @Shadow public boolean hasNoSky;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static WorldProvider getProviderForDimension(int par0)
    {
        return DimensionManager.createProviderFor(par0);
    }

    /*======================================= Forge Start =========================================*/
    private SkyProvider skyProvider = null;
    /**
     * Sets the providers current dimension ID, used in default getSaveFolder()
     * Added to allow default providers to be registered for multiple dimensions.
     *
     * @param dim Dimension ID
     */
    @Override
    public void setDimension(int dim)
    {
        this.dimensionId = dim;
    }

    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to.
     * EXA: DIM1, DIM-1
     * @return The sub-folder name to save this world's chunks to.
     */
    @Override
    public String getSaveFolder()
    {
        return (dimensionId == 0 ? null : "DIM" + dimensionId);
    }

    /**
     * A message to display to the user when they transfer to this dimension.
     *
     * @return The message to be displayed
     */
    @Override
    public String getWelcomeMessage()
    {
        if ((Object) this instanceof WorldProviderEnd)
        {
            return "Entering the End";
        }
        else if ((Object) this instanceof WorldProviderHell)
        {
            return "Entering the Nether";
        }
        return null;
    }

    /**
     * A Message to display to the user when they transfer out of this dismension.
     *
     * @return The message to be displayed
     */
    @Override
    public String getDepartMessage()
    {
        if ((Object) this instanceof WorldProviderEnd)
        {
            return "Leaving the End";
        }
        else if ((Object) this instanceof WorldProviderHell)
        {
            return "Leaving the Nether";
        }
        return null;
    }

    /**
     * The dimensions movement factor. Relative to normal overworld.
     * It is applied to the players position when they transfer dimensions.
     * Exa: Nether movement is 8.0
     * @return The movement factor
     */
    @Override
    public double getMovementFactor()
    {
        if ((Object) this instanceof WorldProviderHell)
        {
            return 8.0;
        }
        return 1.0;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public SkyProvider getSkyProvider()
    {
        return this.skyProvider;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setSkyProvider(SkyProvider skyProvider)
    {
        this.skyProvider = skyProvider;
    }

    @Override
    public ChunkCoordinates getRandomizedSpawnPoint()
    {
        ChunkCoordinates var5 = new ChunkCoordinates(this.worldObj.getSpawnPoint());

        boolean isAdventure = worldObj.getWorldInfo().getGameType() != EnumGameType.ADVENTURE;
        int spawnFuzz = ((WorldTypeExtension) terrainType).getSpawnFuzz();
        int spawnFuzzHalf = spawnFuzz / 2;

        if (!this.hasNoSky && !isAdventure)
        {
            var5.posX += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
            var5.posZ += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
            var5.posY = this.worldObj.getTopSolidOrLiquidBlock(var5.posX, var5.posZ);
        }

        return var5;
    }

    /*======================================= Start Moved From World =========================================*/

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z)
    {
        return ((WorldExtension) worldObj).getBiomeGenForCoordsBody(x, z);
    }

    @Override
    public boolean isDaytime()
    {
        return worldObj.skylightSubtracted < 4;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
    {
        return ((WorldExtension) worldObj).getSkyColorBody(cameraEntity, partialTicks);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3 drawClouds(float partialTicks)
    {
        return ((WorldExtension) worldObj).drawCloudsBody(partialTicks);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getStarBrightness(float par1)
    {
        return ((WorldExtension) worldObj).getStarBrightnessBody(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        worldObj.spawnHostileMobs = allowHostile;
        worldObj.spawnPeacefulMobs = allowPeaceful;
    }

    @Override
    public void calculateInitialWeather()
    {
        ((WorldExtension) worldObj).calculateInitialWeatherBody();
    }

    @Override
    public void updateWeather()
    {
        ((WorldExtension) worldObj).updateWeatherBody();
    }

    @Override
    public void toggleRain()
    {
        worldObj.getWorldInfo().setRainTime(1);
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
    {
        return ((WorldExtension) worldObj).canBlockFreezeBody(x, y, z, byWater);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z)
    {
        return ((WorldExtension) worldObj).canSnowAtBody(x, y, z);
    }

    @Override
    public void setWorldTime(long time)
    {
        worldObj.getWorldInfo().setWorldTime(time);
    }

    @Override
    public long getSeed()
    {
        return worldObj.getWorldInfo().getSeed();
    }

    @Override
    public long getWorldTime()
    {
        return worldObj.getWorldInfo().getWorldTime();
    }

    @Override
    public ChunkCoordinates getSpawnPoint()
    {
        WorldInfo info = worldObj.getWorldInfo();
        return new ChunkCoordinates(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ());
    }

    @Override
    public void setSpawnPoint(int x, int y, int z)
    {
        worldObj.getWorldInfo().setSpawnPosition(x, y, z);
    }

    @Override
    public boolean canMineBlock(EntityPlayer player, int x, int y, int z)
    {
        return ((WorldExtension) worldObj).canMineBlockBody(player, x, y, z);
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z)
    {
        return worldObj.getBiomeGenForCoords(x, z).isHighHumidity();
    }

    @Override
    public int getHeight()
    {
        return 256;
    }

    @Override
    public int getActualHeight()
    {
        return hasNoSky ? 128 : 256;
    }

    @Override
    public double getHorizon()
    {
        return ((WorldTypeExtension) worldObj.getWorldInfo().getTerrainType()).getHorizon(worldObj);
    }

    @Override
    public void resetRainAndThunder()
    {
        worldObj.getWorldInfo().setRainTime(0);
        worldObj.getWorldInfo().setRaining(false);
        worldObj.getWorldInfo().setThunderTime(0);
        worldObj.getWorldInfo().setThundering(false);
    }

    @Override
    public boolean canDoLightning(Chunk chunk)
    {
        return true;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return true;
    }
}
