/**
 * Copyright (C) 2023-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge;

import fr.catcore.cursedmixinextensions.CursedMixinExtensions;
import io.github.fabriccompatibilitylayers.fabricatedfml.utils.PatchConversionHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.ornithemc.conditionalmixin.mixin.ConditionalMixinPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FabricatedForgeMixinPlugin extends ConditionalMixinPlugin {

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    private static List<String> OPTIFINE_OVERRIDES = Arrays.asList(
            "anz",
            "aob",
            "aov",
            "apx",
            "aus",
            "auw",
            "avb",
            "avc",
            "ave",
            "avf",
            "avg",
            "avi",
            "avk",
            "avl",
            "ik",
            "jw"
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
