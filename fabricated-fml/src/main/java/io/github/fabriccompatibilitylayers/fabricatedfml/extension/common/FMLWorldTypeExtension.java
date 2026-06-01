/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.extension.common;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

import java.util.Random;

public interface FMLWorldTypeExtension {
    WorldChunkManager getChunkManager(World world);

    IChunkProvider getChunkGenerator(World world);

    int getMinimumSpawnHeight(World world);

    double getHorizon(World world);

    boolean hasVoidParticles(boolean var1);

    double voidFadeMagnitude();

    BiomeGenBase[] getBiomesForWorldType();

    void addNewBiome(BiomeGenBase biome);

    void removeBiome(BiomeGenBase biome);

    boolean handleSlimeSpawnReduction(Random random, World world);

    void onGUICreateWorldPress();
}
