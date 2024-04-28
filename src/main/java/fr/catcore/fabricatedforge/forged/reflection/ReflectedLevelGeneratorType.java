package fr.catcore.fabricatedforge.forged.reflection;

import com.google.common.collect.ObjectArrays;
import net.minecraft.world.biome.Biome;

public class ReflectedLevelGeneratorType {
    public static final Biome[] base11Biomes = new Biome[]{Biome.DESERT, Biome.FOREST, Biome.EXTREME_HILLS, Biome.SWAMPLAND, Biome.PLAINS, Biome.TAIGA};
    public static final Biome[] base12Biomes = ObjectArrays.concat(base11Biomes, Biome.JUNGLE);
}
