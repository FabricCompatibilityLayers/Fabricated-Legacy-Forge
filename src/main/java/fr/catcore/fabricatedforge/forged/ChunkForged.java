package fr.catcore.fabricatedforge.forged;

import fr.catcore.fabricatedforge.mixininterface.IChunk;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

public class ChunkForged extends Chunk {
    public ChunkForged(World world, int chunkX, int chunkZ) {
        super(world, chunkX, chunkZ);
    }

    public ChunkForged(World world, byte[] bs, int i, int j) {
        super(world, bs, i, j);
    }

    public ChunkForged(World world, byte[] ids, byte[] metadata, int chunkX, int chunkZ) {
        this(world, chunkX, chunkZ);
        int var5 = ids.length / 256;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                for(int y = 0; y < var5; ++y) {
                    int idx = x << 11 | z << 7 | y;
                    int id = ids[idx] & 255;
                    int meta = metadata[idx];
                    if (id != 0) {
                        int var10 = y >> 4;
                        if (((IChunk)this).getChunkSection(var10) == null) {
                            ((IChunk)this).setChunkSection(var10, new ChunkSection(var10 << 4));
                        }

                        ((IChunk)this).getChunkSection(var10).setBlock(x, y & 15, z, id);
                        ((IChunk)this).getChunkSection(var10).setBlockData(x, y & 15, z, meta);
                    }
                }
            }
        }
    }

    public ChunkForged(World world, short[] ids, byte[] metadata, int chunkX, int chunkZ) {
        this(world, chunkX, chunkZ);
        int max = ids.length / 256;

        for(int y = 0; y < max; ++y) {
            for(int z = 0; z < 16; ++z) {
                for(int x = 0; x < 16; ++x) {
                    int idx = y << 8 | z << 4 | x;
                    int id = ids[idx] & 16777215;
                    int meta = metadata[idx];
                    if (id != 0) {
                        int storageBlock = y >> 4;
                        if (((IChunk)this).getChunkSection(storageBlock) == null) {
                            ((IChunk)this).setChunkSection(storageBlock, new ChunkSection(storageBlock << 4));
                        }

                        ((IChunk)this).getChunkSection(storageBlock).setBlock(x, y & 15, z, id);
                        ((IChunk)this).getChunkSection(storageBlock).setBlockData(x, y & 15, z, meta);
                    }
                }
            }
        }
    }
}
