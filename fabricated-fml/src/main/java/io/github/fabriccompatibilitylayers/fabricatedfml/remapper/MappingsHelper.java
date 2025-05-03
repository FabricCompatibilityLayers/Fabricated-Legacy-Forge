package io.github.fabriccompatibilitylayers.fabricatedfml.remapper;

import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.MappingUtils;

public class MappingsHelper {
    public static MappingUtils.ClassMember mapMethod(String owner, String methodName, String argDesc) {
        return MappingUtils.mapMethod(Constants.CONTEXT_ID, owner.replace(".", "/"), methodName, argDesc);
    }

    public static MappingUtils.ClassMember mapMethodFromRemappedClass(String owner, String methodName, String argDesc) {
        if (argDesc != null && !argDesc.endsWith(")")) {
            String[] parts = argDesc.split("\\)");

            if (parts[1].startsWith("L") && !argDesc.endsWith(";")) {
                argDesc += ";";
            }
        }

        return MappingUtils.mapMethodFromRemappedClass(Constants.CONTEXT_ID, owner.replace(".", "/"), methodName, argDesc);
    }

    public static String mapClass(String className) {
        return MappingUtils.mapClass(Constants.CONTEXT_ID, className.replace(".", "/"));
    }

    public static MappingUtils.ClassMember mapField(String owner, String fieldName) {
        return MappingUtils.mapField(Constants.CONTEXT_ID, owner.replace(".", "/"), fieldName, null);
    }

    public static MappingUtils.ClassMember mapFieldFromRemappedClass(String owner, String fieldName, String fieldDesc) {
        return MappingUtils.mapFieldFromRemappedClass(Constants.CONTEXT_ID, owner.replace(".", "/"), fieldName, fieldDesc);
    }

    public static String mapDescriptor(String desc) {
        return MappingUtils.mapDescriptor(Constants.CONTEXT_ID, desc);
    }
}
