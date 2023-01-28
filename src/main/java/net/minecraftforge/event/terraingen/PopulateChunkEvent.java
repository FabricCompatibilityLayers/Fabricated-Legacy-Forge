package net.minecraftforge.event.terraingen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;

import java.util.Random;

public class PopulateChunkEvent extends ChunkProviderEvent {
    public final World world;
    public final Random rand;
    public final int chunkX;
    public final int chunkZ;
    public final boolean hasVillageGenerated;

    public PopulateChunkEvent(ChunkProvider chunkProvider, World world, Random rand, int chunkX, int chunkZ, boolean hasVillageGenerated) {
        super(chunkProvider);
        this.world = world;
        this.rand = rand;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.hasVillageGenerated = hasVillageGenerated;
    }

    @HasResult
    public static class Populate extends PopulateChunkEvent {
        public final PopulateChunkEvent.Populate.EventType type;

        public Populate(
                ChunkProvider chunkProvider,
                World world,
                Random rand,
                int chunkX,
                int chunkZ,
                boolean hasVillageGenerated,
                PopulateChunkEvent.Populate.EventType type
        ) {
            super(chunkProvider, world, rand, chunkX, chunkZ, hasVillageGenerated);
            this.type = type;
        }

        public static enum EventType {
            DUNGEON,
            FIRE,
            GLOWSTONE,
            ICE,
            LAKE,
            LAVA,
            NETHER_LAVA,
            CUSTOM;

            private EventType() {
            }
        }
    }

    public static class Post extends PopulateChunkEvent {
        public Post(ChunkProvider chunkProvider, World world, Random rand, int chunkX, int chunkZ, boolean hasVillageGenerated) {
            super(chunkProvider, world, rand, chunkX, chunkZ, hasVillageGenerated);
        }
    }

    public static class Pre extends PopulateChunkEvent {
        public Pre(ChunkProvider chunkProvider, World world, Random rand, int chunkX, int chunkZ, boolean hasVillageGenerated) {
            super(chunkProvider, world, rand, chunkX, chunkZ, hasVillageGenerated);
        }
    }
}
