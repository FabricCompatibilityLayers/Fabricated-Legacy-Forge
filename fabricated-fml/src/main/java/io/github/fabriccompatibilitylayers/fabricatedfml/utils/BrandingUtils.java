package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

public class BrandingUtils {
    private static final String MOD_VERSION = FabricLoader.getInstance().getModContainer("fabricated-fml").get().getMetadata().getVersion().getFriendlyString();
    private static final boolean HAS_FORGE = FabricLoader.getInstance().isModLoaded("fabricated-forge");
    private static final String FABRIC_VERSION = FabricLoader.getInstance().getModContainer("fabricloader").get().getMetadata().getVersion().getFriendlyString();
    private static final int MOD_COUNT = FabricLoader.getInstance().getAllMods().size();

    public static List<String> getBrandingInfo() {
        return Arrays.asList(
            String.format(HAS_FORGE ? "Fabricated Legacy Forge %s" : "Fabricated FML %s", MOD_VERSION),
            String.format("Fabric Loader %s (%d Mods)", FABRIC_VERSION, MOD_COUNT)
        );
    }
}
