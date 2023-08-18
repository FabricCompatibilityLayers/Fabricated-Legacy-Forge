package fr.catcore.fabricatedforge.forged;

import com.google.common.collect.ObjectArrays;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;

public class ReflectionUtils {
    public static double World_MAX_ENTITY_RADIUS = 2.0;

    public static final Biome[] LevelGeneratorType_base11Biomes = new Biome[]{Biome.DESERT, Biome.FOREST, Biome.EXTREME_HILLS, Biome.SWAMPLAND, Biome.PLAINS, Biome.TAIGA};
    public static final Biome[] LevelGeneratorType_base12Biomes = ObjectArrays.concat(LevelGeneratorType_base11Biomes, Biome.JUNGLE);

    public static byte class_469_connectionCompatibilityLevel;

    public static int[] Block_blockFireSpreadSpeed = new int[4096];

    public static int[] Block_blockFlammability = new int[4096];

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

    public static final String PERSISTED_NBT_TAG = "PlayerPersisted";
}
