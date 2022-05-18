package fr.catcore.fabricatedforge.utils;

import com.google.common.collect.ObjectArrays;
import net.minecraft.world.biome.Biome;

public class ReflectionUtils {
    public static double World_MAX_ENTITY_RADIUS = 2.0;

    public static final Biome[] LevelGeneratorType_base11Biomes = new Biome[]{Biome.DESERT, Biome.FOREST, Biome.EXTREME_HILLS, Biome.SWAMPLAND, Biome.PLAINS, Biome.TAIGA};
    public static final Biome[] LevelGeneratorType_base12Biomes = ObjectArrays.concat(LevelGeneratorType_base11Biomes, Biome.JUNGLE);
}
