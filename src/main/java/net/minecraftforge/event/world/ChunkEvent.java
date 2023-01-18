/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.world;

import net.minecraft.world.chunk.Chunk;

public class ChunkEvent extends WorldEvent {
    private final Chunk chunk;

    public ChunkEvent(Chunk chunk) {
        super(chunk.world);
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public static class Unload extends ChunkEvent {
        public Unload(Chunk chunk) {
            super(chunk);
        }
    }

    public static class Load extends ChunkEvent {
        public Load(Chunk chunk) {
            super(chunk);
        }
    }
}
