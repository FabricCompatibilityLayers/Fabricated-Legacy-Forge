package net.minecraftforge.event.world;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.Event;

public class ChunkWatchEvent extends Event {
    public final ChunkPos chunk;
    public final ServerPlayerEntity player;

    public ChunkWatchEvent(ChunkPos chunk, ServerPlayerEntity player) {
        this.chunk = chunk;
        this.player = player;
    }

    public static class UnWatch extends ChunkWatchEvent {
        public UnWatch(ChunkPos chunkLocation, ServerPlayerEntity player) {
            super(chunkLocation, player);
        }
    }

    public static class Watch extends ChunkWatchEvent {
        public Watch(ChunkPos chunk, ServerPlayerEntity player) {
            super(chunk, player);
        }
    }
}
