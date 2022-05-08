package net.minecraftforge.client;

import net.minecraft.client.options.GameOption;
import net.minecraft.client.util.OperatingSystem;
import net.minecraft.util.Rarity;
import net.minecraft.world.GameMode;
import net.minecraftforge.common.EnumHelper;

public class EnumHelperClient extends EnumHelper {
    private static Class[][] clentTypes;

    public EnumHelperClient() {
    }

    public static GameMode addGameType(String name, int id, String displayName) {
        return (GameMode)addEnum(GameMode.class, name, id, displayName);
    }

    public static GameOption addOptions(String name, String langName, boolean isSlider, boolean isToggle) {
        return (GameOption)addEnum(GameOption.class, name, langName, isSlider, isToggle);
    }

    public static OperatingSystem addOS2(String name) {
        return (OperatingSystem)addEnum(OperatingSystem.class, name);
    }

    public static Rarity addRarity(String name, int color, String displayName) {
        return (Rarity)addEnum(Rarity.class, name, color, displayName);
    }

    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Object... paramValues) {
        return addEnum(clentTypes, enumType, enumName, paramValues);
    }

    static {
        clentTypes = new Class[][]{{GameMode.class, Integer.TYPE, String.class}, {GameOption.class, String.class, Boolean.TYPE, Boolean.TYPE}, {OperatingSystem.class}, {Rarity.class, Integer.TYPE, String.class}};
    }
}
