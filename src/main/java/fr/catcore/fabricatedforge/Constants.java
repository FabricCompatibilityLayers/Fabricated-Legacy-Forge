package fr.catcore.fabricatedforge;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.Objects;

import static fr.catcore.modremapperapi.remapping.RemapUtil.getNativeNamespace;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.6-6.5.0.489/forge-1.4.6-6.5.0.489-universal.zip";

    public static final File MODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "mods");
    public static final File COREMODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "coremods");

    static {
        MODS_FOLDER.mkdirs();
        COREMODS_FOLDER.mkdirs();
    }

    public static Pair<String, String> getRemappedMethodNameNative(String owner, String methodName, String argDesc) {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        for(ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
            if (def.getName(getNativeNamespace()).replace(".", "/").equals(owner.replace(".", "/"))) {
                for(MethodDef methodDef : def.getMethods()) {
                    if (Objects.equals(methodDef.getName(getNativeNamespace()), methodName)) {
                        String methodDescriptor = methodDef.getDescriptor(getNativeNamespace());
                        if (methodDescriptor.startsWith(argDesc)) {
                            return Pair.of(methodDef.getName(resolver.getCurrentRuntimeNamespace()), methodDef.getDescriptor(resolver.getCurrentRuntimeNamespace()));
                        }
                    }
                }
            }
        }

        return Pair.of(methodName, argDesc);
    }

    public static Pair<String, String> getRemappedMethodName(String owner, String methodName, String argDesc) {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        for(ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
            if (def.getName(resolver.getCurrentRuntimeNamespace()).replace(".", "/").equals(owner.replace(".", "/"))) {
                for(MethodDef methodDef : def.getMethods()) {
                    if (Objects.equals(methodDef.getName(getNativeNamespace()), methodName)) {
                        String methodDescriptor = methodDef.getDescriptor(getNativeNamespace());
                        if (methodDescriptor.startsWith(argDesc)) {
                            return Pair.of(methodDef.getName(resolver.getCurrentRuntimeNamespace()), methodDef.getDescriptor(resolver.getCurrentRuntimeNamespace()));
                        }
                    }
                }
            }
        }

        return Pair.of(methodName, argDesc);
    }

    public static String getRemappedClassName(String name) {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        for(ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
            if (def.getName(getNativeNamespace()).replace(".", "/").equals(name.replace(".", "/"))) {
                return def.getName(resolver.getCurrentRuntimeNamespace()).replace("/", ".");
            }
        }

        return name.replace("/", ".");
    }

    public static String getRemappedFieldNameNative(String owner, String fieldName) {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        for(ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
            if (def.getName(getNativeNamespace()).replace(".", "/").equals(owner.replace(".", "/"))) {
                for(FieldDef fieldDef : def.getFields()) {
                    if (Objects.equals(fieldDef.getName(getNativeNamespace()), fieldName)) {
                        return fieldDef.getName(resolver.getCurrentRuntimeNamespace());
                    }
                }
            }
        }

        return fieldName;
    }

    public static Pair<String, String> getRemappedFieldName(String owner, String fieldName, String fieldDesc) {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        for(ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
            if (def.getName(resolver.getCurrentRuntimeNamespace()).replace(".", "/").equals(owner.replace(".", "/"))) {
                for(FieldDef fieldDef : def.getFields()) {
                    if (Objects.equals(fieldDef.getName(getNativeNamespace()), fieldName)) {
                        return Pair.of(fieldDef.getName(resolver.getCurrentRuntimeNamespace()), fieldDef.getDescriptor(resolver.getCurrentRuntimeNamespace()));
                    }
                }
            }
        }

        return Pair.of(fieldName, fieldDesc);
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
