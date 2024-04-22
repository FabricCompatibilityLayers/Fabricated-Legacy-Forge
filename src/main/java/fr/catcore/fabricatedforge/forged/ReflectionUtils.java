package fr.catcore.fabricatedforge.forged;

public class ReflectionUtils {

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

    public static float NAME_TAG_RANGE = 64.0F;
    public static float NAME_TAG_RANGE_SNEAK = 32.0F;

    public static final String PERSISTED_NBT_TAG = "PlayerPersisted";
}
