package fr.catcore.fabricatedforge.mixin;

import fr.catcore.cursedmixinextensions.CursedMixinExtensions;
import fr.catcore.fabricatedforge.Constants;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FabricatedForgeMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    private static List<String> OPTIFINE_OVERRIDES = Arrays.asList(
            "asl",
            "asn",
            "atj",
            "auh",
            "auo",
            "azy",
            "bac",
            "bak",
            "bam",
            "bao",
            "bap",
            "baq",
            "bas",
            "bau",
            "bav",
            "ik",
            "il",
            "kh",
            "md"
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (FabricLoader.getInstance().isModLoaded("optifabric")) {
            if (OPTIFINE_OVERRIDES.contains(FabricLoader.getInstance().getMappingResolver()
                    .unmapClassName("official", targetClassName)) && !mixinClassName.endsWith("Accessor") && !mixinClassName.contains(".optifine.")) {
                System.out.println("[Fabricated-Legacy-Forge] Mixin cancelled for Optifine compatibility: " + mixinClassName);
                return false;
            }

            return true;
        } else {
            return !mixinClassName.contains(".optifine.");
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        CursedMixinExtensions.postApply(targetClass);
    }
}
