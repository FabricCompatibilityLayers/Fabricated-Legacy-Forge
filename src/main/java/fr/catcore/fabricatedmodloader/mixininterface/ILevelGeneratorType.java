package fr.catcore.fabricatedmodloader.mixininterface;

import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;

public interface ILevelGeneratorType {

    LayeredBiomeSource getChunkManager(World world);

    ChunkProvider getChunkGenerator(World world, String params);

    int getSeaLevel(World world);

    boolean hasVoidParticles(boolean flag);

    double voidFadeMagnitude();
}
