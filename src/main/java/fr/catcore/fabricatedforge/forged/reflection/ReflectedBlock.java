package fr.catcore.fabricatedforge.forged.reflection;

public class ReflectedBlock {
    public static int[] blockFireSpreadSpeed = new int[4096];
    public static int[] blockFlammability = new int[4096];

    public static void setBurnProperties(int id, int encouragement, int flammability) {
        if (blockFireSpreadSpeed == null) {
            blockFireSpreadSpeed = new int[4096];
        }

        if (blockFlammability == null) {
            blockFlammability = new int[4096];
        }

        blockFireSpreadSpeed[id] = encouragement;
        blockFlammability[id] = flammability;
    }
}
