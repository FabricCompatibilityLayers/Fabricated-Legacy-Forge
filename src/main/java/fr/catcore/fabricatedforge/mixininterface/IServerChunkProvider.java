package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStorage;

public interface IServerChunkProvider {

    ChunkStorage getChunkWriter();
}
