package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.legacyfabric.fabric.api.logger.v1.Logger;

import java.io.File;

public class Constants {

    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.3.2-4.3.5.318/forge-1.3.2-4.3.5.318-universal.zip";
    public static final File MAIN_FOLDER = new File(FabricLoader.getInstance().getGameDir().toFile(), "fabricated-forge");
    public static final File VERSIONED_FOLDER = new File(
            new File(MAIN_FOLDER,
                FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString()
            ), FabricLoader.getInstance().getModContainer("fabricated-forge").get().getMetadata().getVersion().getFriendlyString()
    );
    public static final File MAPPINGS_FILE = new File(VERSIONED_FOLDER, "mappings.tiny");
    public static final File MOD_MAPPINGS_FILE = new File(VERSIONED_FOLDER, "mods_mappings.tiny");
    public static final File REMAPPED_MODS_FOLDER = new File(VERSIONED_FOLDER, "mods");
    public static final File REMAPPED_COREMODS_FOLDER = new File(VERSIONED_FOLDER, "coremods");
    public static final File FORGE_FILE = new File(VERSIONED_FOLDER, "forge.zip");

    public static final Logger MAIN_LOGGER = Logger.get("FabricatedForge");
    public static final Logger FORGE_LOGGER = Logger.get("FabricatedForge", "Forge");

    static {
        MAIN_FOLDER.mkdirs();
        VERSIONED_FOLDER.mkdirs();
        REMAPPED_MODS_FOLDER.mkdirs();
        REMAPPED_COREMODS_FOLDER.mkdirs();
    }
}
