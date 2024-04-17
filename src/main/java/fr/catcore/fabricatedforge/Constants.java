package fr.catcore.fabricatedforge;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;

import java.io.File;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.3-6.2.1.358/forge-1.4.3-6.2.1.358-universal.zip";

    public static final File MODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "mods");
    public static final File COREMODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "coremods");

    static {
        MODS_FOLDER.mkdirs();
        COREMODS_FOLDER.mkdirs();
    }

    public static MappingUtils.ClassMember mapMethod(String owner, String methodName, String argDesc) {
        return MappingUtils.mapMethod(owner.replace(".", "/"), methodName, argDesc);
    }

    public static MappingUtils.ClassMember mapMethodFromRemappedClass(String owner, String methodName, String argDesc) {
        return MappingUtils.mapMethodFromRemappedClass(owner.replace(".", "/"), methodName, argDesc);
    }

    public static String mapClass(String className) {
        return MappingUtils.mapClass(className.replace(".", "/")).replace("/", ".");
    }

    public static MappingUtils.ClassMember mapField(String owner, String fieldName) {
        return MappingUtils.mapField(owner.replace(".", "/"), fieldName, null);
    }

    public static MappingUtils.ClassMember mapFieldFromRemappedClass(String owner, String fieldName, String fieldDesc) {
        return MappingUtils.mapFieldFromRemappedClass(owner.replace(".", "/"), fieldName, fieldDesc);
    }
}
