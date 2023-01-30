/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.terraingen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraftforge.event.Event;

public class ChunkProviderEvent extends Event {
    public final ChunkProvider chunkProvider;

    public ChunkProviderEvent(ChunkProvider chunkProvider) {
        this.chunkProvider = chunkProvider;
    }

    @HasResult
    public static class InitNoiseField extends ChunkProviderEvent {
        public double[] noisefield;
        public final int posX;
        public final int posY;
        public final int posZ;
        public final int sizeX;
        public final int sizeY;
        public final int sizeZ;

        public InitNoiseField(ChunkProvider chunkProvider, double[] noisefield, int posX, int posY, int posZ, int sizeX, int sizeY, int sizeZ) {
            super(chunkProvider);
            this.noisefield = noisefield;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.sizeX = sizeX;
            this.sizeY = sizeX;
            this.sizeZ = sizeZ;
        }
    }

    @HasResult
    public static class ReplaceBiomeBlocks extends ChunkProviderEvent {
        public final int chunkX;
        public final int chunkZ;
        public final byte[] blockArray;
        public final Biome[] biomeArray;

        public ReplaceBiomeBlocks(ChunkProvider chunkProvider, int chunkX, int chunkZ, byte[] blockArray, Biome[] biomeArray) {
            super(chunkProvider);
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.blockArray = blockArray;
            this.biomeArray = biomeArray;
        }
    }
}
