package fr.catcore.fabricatedmodloader.utils;

import net.minecraft.world.biome.Biome;

public class SetBaseBiomesLayerData {
    public static Biome[] biomeArray;

    static {
        biomeArray = new Biome[]{Biome.DESERT, Biome.FOREST, Biome.EXTREME_HILLS, Biome.SWAMPLAND, Biome.PLAINS, Biome.TAIGA, Biome.JUNGLE};
    }
}
