package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.world.chunk.Chunk;

public interface IServerChunkProvider {
    Chunk getOrGenerateChunk(int par1, int par2);
}
