/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.terraingen;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

import java.util.Random;

public class DecorateBiomeEvent extends Event {
    public final World world;
    public final Random rand;
    public final int chunkX;
    public final int chunkZ;

    public DecorateBiomeEvent(World world, Random rand, int worldX, int worldZ) {
        this.world = world;
        this.rand = rand;
        this.chunkX = worldX;
        this.chunkZ = worldZ;
    }

    @HasResult
    public static class Decorate extends DecorateBiomeEvent {
        public final DecorateBiomeEvent.Decorate.EventType type;

        public Decorate(World world, Random rand, int worldX, int worldZ, DecorateBiomeEvent.Decorate.EventType type) {
            super(world, rand, worldX, worldZ);
            this.type = type;
        }

        public static enum EventType {
            BIG_SHROOM,
            CACTUS,
            CLAY,
            DEAD_BUSH,
            LILYPAD,
            FLOWERS,
            GRASS,
            LAKE,
            PUMPKIN,
            REED,
            SAND,
            SAND_PASS2,
            SHROOM,
            TREE,
            CUSTOM;

            private EventType() {
            }
        }
    }

    public static class Post extends DecorateBiomeEvent {
        public Post(World world, Random rand, int worldX, int worldZ) {
            super(world, rand, worldX, worldZ);
        }
    }

    public static class Pre extends DecorateBiomeEvent {
        public Pre(World world, Random rand, int worldX, int worldZ) {
            super(world, rand, worldX, worldZ);
        }
    }
}
