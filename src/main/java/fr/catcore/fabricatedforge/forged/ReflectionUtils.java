package fr.catcore.fabricatedforge.forged;

import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;

public class ReflectionUtils {


    public static void Block_setBurnProperties(int id, int encouragement, int flammability) {
        if (Block_blockFireSpreadSpeed == null) {
            Block_blockFireSpreadSpeed = new int[4096];
        }

        if (Block_blockFlammability == null) {
            Block_blockFlammability = new int[4096];
        }

        Block_blockFireSpreadSpeed[id] = encouragement;
        Block_blockFlammability[id] = flammability;
    }

    public static ArrayList<Biome> StrongholdStructure_allowedBiomes = new ArrayList<>(
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

    public static ArrayList<Biome> LayeredBiomeSource_allowedBiomes = new ArrayList<>(
            Arrays.asList(Biome.FOREST, Biome.PLAINS, Biome.TAIGA, Biome.TAIGA_HILLS, Biome.FOREST_HILLS, Biome.JUNGLE_HILLS)
    );

    public static float NAME_TAG_RANGE = 64.0F;
    public static float NAME_TAG_RANGE_SNEAK = 32.0F;
}
