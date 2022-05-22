package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.world.chunk.ChunkSection;

public interface IChunk {
    void cleanChunkBlockTileEntity(int x, int y, int z);

    ChunkSection getChunkSection(int index);

    void setChunkSection(int index, ChunkSection section);
}
