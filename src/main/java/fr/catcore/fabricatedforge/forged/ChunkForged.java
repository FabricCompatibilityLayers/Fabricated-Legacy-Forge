package fr.catcore.fabricatedforge.forged;

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
                        if (this.getChunkSection(var10) == null) {
                            this.setChunkSection(var10, new ChunkSection(var10 << 4));
                        }

                        this.getChunkSection(var10).setBlock(x, y & 15, z, id);
                        this.getChunkSection(var10).setBlockData(x, y & 15, z, meta);
                    }
                }
            }
        }
    }
}
