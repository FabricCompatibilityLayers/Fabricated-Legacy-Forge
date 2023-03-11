package fr.catcore.fabricatedforge;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;

import java.io.File;
import java.util.Objects;

import static fr.catcore.modremapperapi.remapping.RemapUtil.getNativeNamespace;

public class Constants {
    public static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.3.2-4.3.5.318/forge-1.3.2-4.3.5.318-universal.zip";

    public static final File MODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "mods");
    public static final File COREMODS_FOLDER = new File(fr.catcore.modremapperapi.utils.Constants.VERSIONED_FOLDER, "coremods");

    static {
        MODS_FOLDER.mkdirs();
        COREMODS_FOLDER.mkdirs();
    }

    public static Pair<String, String> getRemappedMethodName(Class<?> owner, String methodName, String argDesc) {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        for(ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
            if (def.getName(resolver.getCurrentRuntimeNamespace()).replace(".", "/").equals(owner.getName().replace(".", "/"))) {
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

        if (owner.getSuperclass() != null) {
            return getRemappedMethodName(owner.getSuperclass(), methodName, argDesc);
        }

        return Pair.of(methodName, argDesc);
    }
}
