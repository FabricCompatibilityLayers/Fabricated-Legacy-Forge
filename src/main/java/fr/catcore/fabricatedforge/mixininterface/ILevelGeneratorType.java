package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;

public interface ILevelGeneratorType {

    LayeredBiomeSource getChunkManager(World world);

    ChunkProvider getChunkGenerator(World world);

    int getSeaLevel(World world);

    boolean hasVoidParticles(boolean flag);

    double voidFadeMagnitude();
}
