package net.minecraftforge.event.terraingen;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.Event;

import java.util.Random;

public class OreGenEvent extends Event {
    public final World world;
    public final Random rand;
    public final int worldX;
    public final int worldZ;

    public OreGenEvent(World world, Random rand, int worldX, int worldZ) {
        this.world = world;
        this.rand = rand;
        this.worldX = worldX;
        this.worldZ = worldZ;
    }

    @HasResult
    public static class GenerateMinable extends OreGenEvent {
        public final OreGenEvent.GenerateMinable.EventType type;
        public final Feature generator;

        public GenerateMinable(World world, Random rand, Feature generator, int worldX, int worldZ, OreGenEvent.GenerateMinable.EventType type) {
            super(world, rand, worldX, worldZ);
            this.generator = generator;
            this.type = type;
        }

        public static enum EventType {
            COAL,
            DIAMOND,
            DIRT,
            GOLD,
            GRAVEL,
            IRON,
            LAPIS,
            REDSTONE,
            CUSTOM;

            private EventType() {
            }
        }
    }

    public static class Post extends OreGenEvent {
        public Post(World world, Random rand, int worldX, int worldZ) {
            super(world, rand, worldX, worldZ);
        }
    }

    public static class Pre extends OreGenEvent {
        public Pre(World world, Random rand, int worldX, int worldZ) {
            super(world, rand, worldX, worldZ);
        }
    }
}
