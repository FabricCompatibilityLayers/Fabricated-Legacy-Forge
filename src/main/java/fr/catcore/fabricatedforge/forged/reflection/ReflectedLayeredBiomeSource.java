package fr.catcore.fabricatedforge.forged.reflection;

import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;

public class ReflectedLayeredBiomeSource {
    public static ArrayList<Biome> allowedBiomes = new ArrayList<>(
            Arrays.asList(Biome.FOREST, Biome.PLAINS, Biome.TAIGA, Biome.TAIGA_HILLS, Biome.FOREST_HILLS, Biome.JUNGLE_HILLS)
    );
}
