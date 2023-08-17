package fr.catcore.fabricatedforge.compat.nei;

import java.util.HashMap;
import java.util.Map;

public class NEIFixer {
    public static Map<String, String> FIX_CLASSES = new HashMap<>();
    public static Map<String, String> FIX_METHOD_NAMES = new HashMap<>();
    public static Map<String, String> FIX_METHOD_ARGS = new HashMap<>();

    static {
        FIX_CLASSES.put("net.minecraft.src.GuiContainer", "net.minecraft.class_409");
        FIX_CLASSES.put("net.minecraft.src.GuiScreen", "net.minecraft.class_388");
        FIX_CLASSES.put("net.minecraft.src.BlockMobSpawner", "net.minecraft.class_159");
        FIX_CLASSES.put("net.minecraft.src.Block", "net.minecraft.class_197");

        FIX_METHOD_NAMES.put("updateScreen", "method_1033");
        FIX_METHOD_NAMES.put("onBlockPlacedBy", "method_419");

        FIX_METHOD_ARGS.put("(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityLiving;)V", "(Lnet/minecraft/class_1150;IIILnet/minecraft/class_871;)V");
    }
}
