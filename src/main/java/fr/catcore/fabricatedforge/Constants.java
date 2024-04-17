package fr.catcore.fabricatedforge;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;

import java.io.File;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.6-6.5.0.489/forge-1.4.6-6.5.0.489-universal.zip";

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

    public static Pair<String, String> getRemappedField(Class<?> cl, String fieldName, String argDesc) {
        MappingTreeView.ClassMappingView classView = MappingsUtils.getMinecraftMappings().getClass(cl.getName().replace(".", "/"), getTargetNamespace());

        if (classView != null) {
            MappingTreeView.FieldMappingView methodView = classView.getField(fieldName, argDesc, getSrcNamespace());

            if (methodView != null) {
                return Pair.of(methodView.getName(getTargetNamespace()), methodView.getDesc(getTargetNamespace()));
            }

            for (MappingTreeView.FieldMappingView fieldViews : classView.getFields()) {
                if (fieldViews.getName(getSrcNamespace()).equals(fieldName)) {
                    if (fieldViews.getDesc(getSrcNamespace()).equals(argDesc)) {
                        return Pair.of(fieldViews.getName(getTargetNamespace()), fieldViews.getDesc(getTargetNamespace()));
                    }
                }
            }
        }

        if (cl.getSuperclass() != null) {
            Pair<String, String> result = getRemappedField(cl.getSuperclass(), fieldName, argDesc);

            if (!fieldName.equals(result.first()) || !argDesc.equals(result.second())) {
                return result;
            }
        }

        return Pair.of(fieldName, argDesc);
    }

    public static String getRemappedField(Class<?> cl, String fieldName) {
        MappingTreeView.ClassMappingView classView = MappingsUtils.getMinecraftMappings().getClass(cl.getName().replace(".", "/"), getTargetNamespace());

        if (classView != null) {
            for (MappingTreeView.FieldMappingView fieldViews : classView.getFields()) {
                if (fieldViews.getName(getSrcNamespace()).equals(fieldName)) {
                    return fieldViews.getName(getTargetNamespace());
                }
            }
        }

        if (cl.getSuperclass() != null) {
            String result = getRemappedField(cl.getSuperclass(), fieldName);

            if (!fieldName.equals(result)) {
                return result;
            }
        }

        return fieldName;
    }

    public static String remapMethodDescriptor(String desc) {
        Type methodDescType = Type.getType(desc);

        Type[] argTypes = methodDescType.getArgumentTypes();
        Type returnType = methodDescType.getReturnType();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        for (Type type : argTypes) {
            stringBuilder.append(remapIndividualType(type.getDescriptor()));
        }

        stringBuilder.append(")");

        stringBuilder.append(remapIndividualType(returnType.getDescriptor()));

        return stringBuilder.toString();
    }

    public static String remapIndividualType(String desc) {
        Type type = Type.getType(desc);

        if (type.getSort() == Type.OBJECT || type.getSort() == 12) {
            String className = type.getClassName();

            String newClassName = getRemappedClassName(className);

            if (!Objects.equals(className, newClassName)) {
                type = Type.getType("L" + newClassName.replace(".", "/") + ";");
            }
        } else if (type.getSort() == Type.ARRAY) {
            StringBuilder arrayType = new StringBuilder(remapIndividualType(type.getElementType().getDescriptor()));

            for (int i = 1; i < type.getDimensions() + 1; i++) {
                arrayType.insert(0, "[");
            }

            type = Type.getType(arrayType.toString());
        }

        return type.getDescriptor();
    }
}
