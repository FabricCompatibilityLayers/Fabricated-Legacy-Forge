package fr.catcore.fabricatedforge;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.Objects;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.7-6.6.2.534/forge-1.4.7-6.6.2.534-universal.zip";

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
        return MappingUtils.mapClass(className.replace(".", "/"));
    }

    public static MappingUtils.ClassMember mapField(String owner, String fieldName) {
        return MappingUtils.mapField(owner.replace(".", "/"), fieldName, null);
    }

    public static MappingUtils.ClassMember mapFieldFromRemappedClass(String owner, String fieldName, String fieldDesc) {
        return MappingUtils.mapFieldFromRemappedClass(owner.replace(".", "/"), fieldName, fieldDesc);
    }

    public static String mapMethodDescriptor(String desc) {
        Type methodDescType = Type.getType(desc);

        Type[] argTypes = methodDescType.getArgumentTypes();

        Type returnType = null;

        try {
            returnType = Type.getReturnType(desc);
        } catch (StringIndexOutOfBoundsException e) {}

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        for (Type type : argTypes) {
            stringBuilder.append(mapTypeDescriptor(type.getDescriptor()));
        }

        stringBuilder.append(")");

        if (returnType != null) stringBuilder.append(mapTypeDescriptor(returnType.getDescriptor()));

        return stringBuilder.toString();
    }

    public static String mapTypeDescriptor(String desc) {
        Type type = Type.getType(desc);

        if (type.getSort() == Type.OBJECT || type.getSort() == 12) {
            String className = type.getClassName();

            String newClassName = mapClass(className);

            if (!Objects.equals(className, newClassName)) {
                type = Type.getType("L" + newClassName.replace(".", "/") + ";");
            }
        } else if (type.getSort() == Type.ARRAY) {
            StringBuilder arrayType = new StringBuilder(mapTypeDescriptor(type.getElementType().getDescriptor()));

            for (int i = 1; i < type.getDimensions() + 1; i++) {
                arrayType.insert(0, "[");
            }

            type = Type.getType(arrayType.toString());
        }

        return type.getDescriptor();
    }
}
