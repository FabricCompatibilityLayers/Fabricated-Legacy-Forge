package fr.catcore.fabricatedforge;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.Objects;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.5-6.4.2.448/forge-1.4.5-6.4.2.448-universal.zip";

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
        if (argDesc != null && !argDesc.endsWith(")")) {
            String[] parts = argDesc.split("\\)");

            if (parts[1].startsWith("L") && !argDesc.endsWith(";")) {
                argDesc += ";";
            }
        }

        return MappingUtils.mapMethodFromRemappedClass(owner.replace(".", "/"), methodName, argDesc);
    }

    public static String mapClass(String className) {
        return MappingUtils.mapClass(className.replace(".", "/"));
    }

    public static MappingUtils.ClassMember mapField(String owner, String fieldName) {
        return MappingUtils.mapField(owner.replace(".", "/"), fieldName, null);
    }

    public static MappingUtils.ClassMember mapFieldFromRemappedClass(String owner, String fieldName, String fieldDesc) {
        return MappingUtils.mapFieldFromRemappedClass(owner.replace(".", "/"), fieldName, fieldDesc);
    }

    public static String mapDescriptor(String desc) {
        return MappingUtils.mapDescriptor(desc);
    }

    public static String unmapClass(String className) {
        return MappingUtils.unmapClass(className.replace(".", "/"));
    }
}
