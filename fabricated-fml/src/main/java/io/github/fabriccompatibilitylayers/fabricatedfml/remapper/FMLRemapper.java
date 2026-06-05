/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper;

import fr.catcore.wfvaio.FabricVariants;
import fr.catcore.wfvaio.WhichFabricVariantAmIOn;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.discoverer.CoremodsDiscoverer;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.discoverer.ForgeModsDiscoverer;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FMLRemapper implements ModRemapper {
    private static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.3.2-4.3.5.318/forge-1.3.2-4.3.5.318-universal.zip";
    private static final boolean runningLegacyFabric = WhichFabricVariantAmIOn.getVariant() == FabricVariants.LEGACY_FABRIC_V1;

    @Override
    public String getContextId() {
        return Constants.CONTEXT_ID;
    }

    @Override
    public void init(CacheHandler cacheHandler) {

    }

    @Override
    public List<ModDiscovererConfig> getModDiscoverers() {
        return Arrays.asList(
            ModDiscovererConfig.builder("coremods")
                    .candidateCollector(new CoremodsDiscoverer())
                    .build(),
                ModDiscovererConfig.builder("mods")
                        .fileNameMatcher("(.+).(jar|zip)$")
                        .allowDirectoryMods(true)
                        .candidateCollector(new ForgeModsDiscoverer())
                        .build()
        );
    }

    private List<ModCandidate> modCandidates;

    @Override
    public List<ModRemapper> collectSubRemappers(List<ModCandidate> list) {
        modCandidates = list;
        return Collections.emptyList();
    }

    @Override
    public MappingsConfig getMappingsConfig() {
        return new FMLMappingsConfig();
    }

    @Override
    public List<RemappingFlags> getRemappingFlags() {
        return Collections.emptyList();
    }

    @Override
    public void afterRemapping() {
        for (ModCandidate candidate : modCandidates) {
            FabricLauncherBase.getLauncher().addToClassPath(candidate.getDestination());
        }
    }

    @Override
    public void afterAllRemappings() {

    }

    @Override
    public void addRemappingLibraries(List<RemapLibrary> list, EnvType envType) {
        list.add(RemapLibrary.of(FORGE_URL, "forge.zip", FORGE_EXCLUSIONS));
    }

    @Override
    public void registerAdditionalMappings(MappingBuilder mappingBuilder) {
        MappingsConfig config = getMappingsConfig();

        // ModLoader mappings
        mappingBuilder.addMapping("BaseMod", config.getDefaultPackage() + "BaseMod");
        mappingBuilder.addMapping("EntityRendererProxy", config.getDefaultPackage() + "EntityRendererProxy");
        mappingBuilder.addMapping("FMLRendererAccessLibrary", config.getDefaultPackage() + "FMLRendererAccessLibrary");
        mappingBuilder.addMapping("MLProp", config.getDefaultPackage() + "MLProp");
        mappingBuilder.addMapping("ModLoader", config.getDefaultPackage() + "ModLoader");
        mappingBuilder.addMapping("ModTextureAnimation", config.getDefaultPackage() + "ModTextureAnimation");
        mappingBuilder.addMapping("ModTextureStatic", config.getDefaultPackage() + "ModTextureStatic");
        mappingBuilder.addMapping("TradeEntry", config.getDefaultPackage() + "TradeEntry");

        // Guava backward compatibility
        if (!runningLegacyFabric) {
            mappingBuilder.addMapping("com/google/common/base/Equivalences", "io/github/fabriccompatibilitylayers/fabricatedfml/compat/guava/Equivalences");
        }
    }

    @Override
    public void registerPreVisitors(VisitorInfos visitorInfos) {
        visitorInfos.registerFieldRef(
                "adt",
                "c",
                "",
                VisitorInfos.classMember(
                        "adt",
                        "dimensionId",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "va",
                "base11Biomes",
                "",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/forged/ForgedWorldType",
                        "base11Biomes",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "va",
                "base12Biomes",
                "",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/forged/ForgedWorldType",
                        "base12Biomes",
                        null
                )
        );
    }

    @Override
    public void registerPostVisitors(VisitorInfos visitorInfos) {
        // Reflection Remappers
        visitorInfos.registerInstantiation(
                "org/objectweb/asm/tree/FieldInsnNode",
                "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/BetterFieldInsnNode"
        );
        visitorInfos.registerInstantiation(
                "org/objectweb/asm/tree/MethodInsnNode",
                "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/BetterMethodInsnNode"
        );
        visitorInfos.registerInstantiation(
                "org/objectweb/asm/ClassWriter",
                "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/BetterClassWriter"
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getDeclaredMethod",
                "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/RemapAwareClass",
                        "getDeclaredMethod",
                        "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getDeclaredField",
                "(Ljava/lang/String;)Ljava/lang/reflect/Field;",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/RemapAwareClass",
                        "getDeclaredField",
                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getMethod",
                "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/RemapAwareClass",
                        "getMethod",
                        "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getField",
                "(Ljava/lang/String;)Ljava/lang/reflect/Field;",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/RemapAwareClass",
                        "getField",
                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "forName",
                "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/RemapAwareClass",
                        "forName",
                        "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "forName",
                "(Ljava/lang/String;)Ljava/lang/Class;",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/remapper/asm/RemapAwareClass",
                        "forName",
                        "(Ljava/lang/String;)Ljava/lang/Class;",
                        true
                )
        );
    }

    private static final List<String> FORGE_EXCLUSIONS = Arrays.asList(
            "a",
            "aad",
            "aae",
            "aan",
            "aar",
            "aaw",
            "abk",
            "abu",
            "acv",
            "adt",
            "adx",
            "aeb",
            "aed",
            "aez",
            "afa",
            "afb",
            "afe",
            "afj",
            "afp",
            "afq",
            "afu",
            "afv",
            "afy",
            "agb",
            "agj",
            "agk",
            "agm",
            "agv",
            "agx",
            "agy",
            "ahh",
            "ahi",
            "ahl",
            "aho",
            "ahy",
            "aic",
            "aig",
            "aig$1",
            "ail",
            "aim",
            "aio",
            "aip",
            "aiq",
            "ais",
            "aiy",
            "ajd",
            "aji",
            "ajj",
            "ajq",
            "ak",
            "amx",
            "anz",
            "aon",
            "aoo",
            "aou",
            "aow",
            "app",
            "apz",
            "aqn",
            "art",
            "arw",
            "ash",
            "aso",
            "ast",
            "asv",
            "atc",
            "atd",
            "aub",
            "aum",
            "aus",
            "auw",
            "av",
            "ava",
            "avb",
            "ave",
            "avf",
            "avg",
            "avy",
            "awg",
            "awh",
            "awr",
            "awv",
            "axc",
            "axd",
            "axf",
            "axg",
            "axh",
            "axi",
            "axj",
            "axk",
            "axp",
            "axs",
            "axv",
            "axy",
            "ayq",
            "ayr",
            "ays",
            "ba",
            "bb",
            "cn",
            "cp",
            "cs",
            "db",
            "dc",
            "el",
            "et",
            "ft",
            "fy",
            "ge",
            "gm",
            "gp",
            "gq",
            "gr",
            "gu",
            "gv",
            "gw",
            "gx",
            "gz",
            "ha",
            "hu",
            "it",
            "jj",
            "jn",
            "jw",
            "lc",
            "mr",
            "ms",
            "mu",
            "nd",
            "nj",
            "nk",
            "ny",
            "o",
            "od",
            "og",
            "pg",
            "ph",
            "pq",
            "pz",
            "qb",
            "qg",
            "qt",
            "qv",
            "rg",
            "rh",
            "rl",
            "rm",
            "ro",
            "rz",
            "sa",
            "si",
            "tb",
            "td",
            "ts",
            "tu",
            "um",
            "up",
            "va",
            "vc",
            "ve",
            "wl",
            "wy",
            "xc",
            "xr",
            "xw",
            "ya",
            "yf",
            "yi",
            "yj",
            "yk",
            "yl",
            "yr",
            "ys",
            "yt",
            "yu",
            "za"
    );
}
