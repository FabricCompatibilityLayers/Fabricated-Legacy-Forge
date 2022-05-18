package fr.catcore.fabricatedforge.forged;

import com.google.common.collect.ObjectArrays;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Unique;

public class ReflectionUtils {
    public static double World_MAX_ENTITY_RADIUS = 2.0;

    public static final Biome[] LevelGeneratorType_base11Biomes = new Biome[]{Biome.DESERT, Biome.FOREST, Biome.EXTREME_HILLS, Biome.SWAMPLAND, Biome.PLAINS, Biome.TAIGA};
    public static final Biome[] LevelGeneratorType_base12Biomes = ObjectArrays.concat(LevelGeneratorType_base11Biomes, Biome.JUNGLE);

    public static byte class_469_connectionCompatibilityLevel;

    public static int[] Block_blockFireSpreadSpeed = new int[4096];

    public static int[] Block_blockFlammability = new int[4096];

    public static void Block_setBurnProperties(int id, int encouragement, int flammability) {
        Block_blockFireSpreadSpeed[id] = encouragement;
        Block_blockFlammability[id] = flammability;
    }
}
