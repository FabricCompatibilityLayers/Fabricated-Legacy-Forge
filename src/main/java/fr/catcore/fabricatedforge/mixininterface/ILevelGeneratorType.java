package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;

import java.util.Random;

public interface ILevelGeneratorType {
    LayeredBiomeSource getChunkManager(World world);

    ChunkProvider getChunkGenerator(World world, String generatorOptions);

    int getMinimumSpawnHeight(World world);

    double getHorizon(World world);

    boolean hasVoidParticles(boolean var1);

    double voidFadeMagnitude();

    Biome[] getBiomesForWorldType();

    void addNewBiome(Biome biome);

    void removeBiome(Biome biome);

    boolean handleSlimeSpawnReduction(Random random, World world);

    void onGUICreateWorldPress();

    int getSpawnFuzz();
}
