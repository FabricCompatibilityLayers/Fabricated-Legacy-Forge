package fr.catcore.fabricatedforge;

import fr.catcore.modremapperapi.impl.lib.mappingio.tree.MappingTreeView;
import fr.catcore.modremapperapi.utils.MappingsUtils;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.Objects;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.6-6.5.0.489/forge-1.4.6-6.5.0.489-universal.zip";

    public static final File MODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "mods");
    public static final File COREMODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "coremods");

    static {
        MODS_FOLDER.mkdirs();
        COREMODS_FOLDER.mkdirs();
    }

    public static Pair<String, String> getRemappedMethodNameNative(String owner, String methodName, String argDesc) {
        return getRemappedMethodName(
                getRemappedClassName(owner),
                methodName, argDesc
        );
    }

    public static Pair<String, String> getRemappedMethodName(String owner, String methodName, String argDesc) {
        if (methodName.equals("<init>") || methodName.length() > 3 || methodName.equals("*")) {
            return Pair.of(methodName, argDesc == null || argDesc.isEmpty() || argDesc.equals("()") ? argDesc
                    : remapMethodDescriptor(argDesc));
        }

        MappingTreeView.ClassMappingView classView = MappingsUtils.getMinecraftMappings().getClass(owner.replace(".", "/"), getTargetNamespace());

        if (classView != null) {
            MappingTreeView.MethodMappingView methodView = classView.getMethod(methodName, argDesc, getSrcNamespace());

            if (methodView != null) {
                return Pair.of(methodView.getName(getTargetNamespace()), methodView.getDesc(getTargetNamespace()));
            }

            for (MappingTreeView.MethodMappingView methodViews : classView.getMethods()) {
                if (methodViews.getName(getSrcNamespace()).equals(methodName)) {
                    if (methodViews.getDesc(getSrcNamespace()).equals(argDesc)) {
                        return Pair.of(methodViews.getName(getTargetNamespace()), methodViews.getDesc(getTargetNamespace()));
                    }
                }
            }
        }

        try {
            return getRemappedMethodName(
                    Class.forName(owner.replace("/", "."), false, Constants.class.getClassLoader()),
                    methodName, argDesc
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Pair.of(methodName, argDesc);
        }
    }

    public static Pair<String, String> getRemappedMethodName(Class<?> cl, String methodName, String argDesc) {
        MappingTreeView.ClassMappingView classView = MappingsUtils.getMinecraftMappings().getClass(cl.getName().replace(".", "/"), getTargetNamespace());

        if (classView != null) {
            MappingTreeView.MethodMappingView methodView = classView.getMethod(methodName, argDesc, getSrcNamespace());

            if (methodView != null) {
                return Pair.of(methodView.getName(getTargetNamespace()), methodView.getDesc(getTargetNamespace()));
            }

            for (MappingTreeView.MethodMappingView methodViews : classView.getMethods()) {
                if (methodViews.getName(getSrcNamespace()).equals(methodName)) {
                    if (methodViews.getDesc(getSrcNamespace()).equals(argDesc)) {
                        return Pair.of(methodViews.getName(getTargetNamespace()), methodViews.getDesc(getTargetNamespace()));
                    }
                }
            }
        }

        if (cl.getSuperclass() != null) {
            Pair<String, String> result = getRemappedMethodName(cl.getSuperclass(), methodName, argDesc);

            if (!methodName.equals(result.first()) || !argDesc.equals(result.second())) {
                return result;
            }
        }

        for (Class<?> interfaces : cl.getInterfaces()) {
            Pair<String, String> result = getRemappedMethodName(interfaces, methodName, argDesc);

            if (!methodName.equals(result.first()) || !argDesc.equals(result.second())) {
                return result;
            }
        }

        return Pair.of(methodName, argDesc);
    }

    public static String getRemappedClassName(String name) {
        return MappingsUtils.getMinecraftMappings()
                .mapClassName(name.replace(".", "/"), getSrcNamespace(), getTargetNamespace())
                .replace("/", ".");
    }

    public static int getSrcNamespace() {
        return MappingsUtils.getMinecraftMappings().getNamespaceId(MappingsUtils.getNativeNamespace());
    }

    public static int getTargetNamespace() {
        return MappingsUtils.getMinecraftMappings().getNamespaceId(MappingsUtils.getTargetNamespace());
    }

    public static String getRemappedFieldNameNative(String owner, String fieldName) {
        return getRemappedFieldName(getRemappedClassName(owner), fieldName);
    }

    public static String getRemappedFieldName(String owner, String fieldName) {
        MappingTreeView.ClassMappingView classView = MappingsUtils.getMinecraftMappings().getClass(owner.replace(".", "/"), getTargetNamespace());

        if (classView != null) {
            for (MappingTreeView.FieldMappingView fieldViews : classView.getFields()) {
                if (fieldViews.getName(getSrcNamespace()).equals(fieldName)) {
                    return fieldViews.getName(getTargetNamespace());
                }
            }
        }

        try {
            return getRemappedField(
                    Class.forName(owner.replace("/", "."), false, Constants.class.getClassLoader()),
                    fieldName
            );
        } catch (Exception e) {
            e.printStackTrace();
            return fieldName;
        }
    }

    public static Pair<String, String> getRemappedFieldName(String owner, String fieldName, String fieldDesc) {
        MappingTreeView.ClassMappingView classView = MappingsUtils.getMinecraftMappings().getClass(owner.replace(".", "/"), getTargetNamespace());

        if (classView != null) {
            MappingTreeView.FieldMappingView methodView = classView.getField(fieldName, fieldDesc, getSrcNamespace());

            if (methodView != null) {
                return Pair.of(methodView.getName(getTargetNamespace()), methodView.getDesc(getTargetNamespace()));
            }

            for (MappingTreeView.FieldMappingView fieldViews : classView.getFields()) {
                if (fieldViews.getName(getSrcNamespace()).equals(fieldName)) {
                    if (fieldViews.getDesc(getSrcNamespace()).equals(fieldDesc)) {
                        return Pair.of(fieldViews.getName(getTargetNamespace()), fieldViews.getDesc(getTargetNamespace()));
                    }
                }
            }
        }

        try {
            return getRemappedField(
                    Class.forName(owner.replace("/", "."), false, Constants.class.getClassLoader()),
                    fieldName, fieldDesc
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Pair.of(fieldName, fieldDesc);
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
