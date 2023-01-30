package fr.catcore.fabricatedforge;

import java.io.File;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.5-6.4.2.448/forge-1.4.5-6.4.2.448-universal.zip";

    public static final File MODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "mods");
    public static final File COREMODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "coremods");

    static {
        MODS_FOLDER.mkdirs();
        COREMODS_FOLDER.mkdirs();
    }
}
