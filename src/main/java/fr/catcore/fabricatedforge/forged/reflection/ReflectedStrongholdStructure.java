package fr.catcore.fabricatedforge.forged.reflection;

import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;

public class ReflectedStrongholdStructure {
    public static ArrayList<Biome> allowedBiomes = new ArrayList<>(
            Arrays.asList(
                    Biome.DESERT,
                    Biome.FOREST,
                    Biome.EXTREME_HILLS,
                    Biome.SWAMPLAND,
                    Biome.TAIGA,
                    Biome.ICE_PLAINS,
                    Biome.ICE_MOUNTAINS,
                    Biome.DESERT_HILLS,
                    Biome.FOREST_HILLS,
                    Biome.EXTREME_HILLS_EDGE,
                    Biome.JUNGLE,
                    Biome.JUNGLE_HILLS
            )
    );
}
