/**
 * Copyright (C) 2023-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml;

import fr.catcore.cursedmixinextensions.CursedMixinExtensions;
import fr.catcore.wfvaio.FabricVariants;
import fr.catcore.wfvaio.WhichFabricVariantAmIOn;
import io.github.fabriccompatibilitylayers.fabricatedfml.utils.PatchConversionHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.ornithemc.conditionalmixin.mixin.ConditionalMixinPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FabricatedFMLMixinPlugin extends ConditionalMixinPlugin {
    static {
        boolean runningLegacyFabric = WhichFabricVariantAmIOn.getVariant() == FabricVariants.LEGACY_FABRIC_V1;

        if (runningLegacyFabric) {
            Path guavaPath = FabricLoader.getInstance().getGameDir().resolve("lib").resolve("guava-12.0.1.jar");

            if (Files.exists(guavaPath)) {
                FabricLauncherBase.getLauncher().addToClassPath(guavaPath);
            }
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    private static List<String> OPTIFINE_OVERRIDES = Arrays.asList(
            "arc",
            "are",
            "asa",
            "atf",
            "ayo",
            "ays",
            "ayx",
            "ayy",
            "aza",
            "azb",
            "azc",
            "aze",
            "azg",
            "azh",
            "jx",
            "lm"
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (FabricLoader.getInstance().isModLoaded("optifabric")) {
            if (OPTIFINE_OVERRIDES.contains(FabricLoader.getInstance().getMappingResolver()
                    .unmapClassName("official", targetClassName)) && !mixinClassName.endsWith("Accessor") && !mixinClassName.contains(".optifine.")) {
                System.out.println("[Fabricated-Legacy-Forge] Mixin cancelled for Optifine compatibility: " + mixinClassName);
                return false;
            }
        }

        return super.shouldApplyMixin(targetClassName, mixinClassName);
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
        PatchConversionHelper.preApply(mixinInfo.getClassNode(0), targetClass);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        CursedMixinExtensions.postApply(targetClass);
        PatchConversionHelper.postApply(targetClass);
    }
}
