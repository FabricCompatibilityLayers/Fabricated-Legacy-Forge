/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.terraingen;

import net.minecraft.world.biome.layer.Layer;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraftforge.event.Event;

public class WorldTypeEvent extends Event {
    public final LevelGeneratorType worldType;

    public WorldTypeEvent(LevelGeneratorType worldType) {
        this.worldType = worldType;
    }

    public static class BiomeSize extends WorldTypeEvent {
        public final byte originalSize;
        public byte newSize;

        public BiomeSize(LevelGeneratorType worldType, byte original) {
            super(worldType);
            this.originalSize = original;
            this.newSize = original;
        }
    }

    public static class InitBiomeGens extends WorldTypeEvent {
        public final long seed;
        public final Layer[] originalBiomeGens;
        public Layer[] newBiomeGens;

        public InitBiomeGens(LevelGeneratorType worldType, long seed, Layer[] original) {
            super(worldType);
            this.seed = seed;
            this.originalBiomeGens = original;
            this.newBiomeGens = (Layer[])original.clone();
        }
    }
}
