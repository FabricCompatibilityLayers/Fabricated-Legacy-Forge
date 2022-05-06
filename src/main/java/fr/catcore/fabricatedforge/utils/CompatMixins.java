package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Map;

public class CompatMixins {
    public static final Map<String, Boolean> MIXINS = new HashMap<>();

    static {
        boolean invTweaks = FabricLoader.getInstance().isModLoaded("invtweaks");
        MIXINS.put("fr.catcore.fabricatedmodloader.mixin.compat.invtweaks.FixInvTweaksLocalization", invTweaks);
        MIXINS.put("fr.catcore.fabricatedmodloader.mixin.compat.invtweaks.FixInvTweaksReflection", invTweaks);
    }
}
