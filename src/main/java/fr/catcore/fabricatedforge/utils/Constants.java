package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.LogCategory;

import java.io.File;

public class Constants {
    public static final File MAIN_FOLDER = new File(FabricLoader.getInstance().getGameDir().toFile(), "fabricated-forge");
    public static final File VERSIONED_FOLDER = new File(MAIN_FOLDER, FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString());
    public static final File MAPPINGS_FILE = new File(VERSIONED_FOLDER, "mappings.tiny");
    public static final File MOD_MAPPINGS_FILE = new File(VERSIONED_FOLDER, "mods_mappings.tiny");
    public static final File REMAPPED_FOLDER = new File(VERSIONED_FOLDER, "mods");

    public static final LogCategory LOG_CATEGORY = new LogCategory("Mod", "FabricatedForge");
    public static final LogCategory MODLOADER_LOG_CATEGORY = new LogCategory("Mod", "FabricatedForge", "Forge");

    static {
        MAIN_FOLDER.mkdirs();
        VERSIONED_FOLDER.mkdirs();
        REMAPPED_FOLDER.mkdirs();
    }
}
