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
import io.github.fabriccompatibilitylayers.fabricatedfml.utils.PortingHelper;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FMLRemapper implements ModRemapper {
    private static final String FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/1.4.0-5.0.0.326/forge-1.4.0-5.0.0.326-universal.zip";
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
        list.add(
                RemapLibrary.builder("forge.zip")
                        .url(FORGE_URL)
                        .mergeWithMainJar(true)
                        .build()
        );
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
            // Moved in Guava 14.0
            mappingBuilder.addMapping("com/google/common/collect/AbstractLinkedIterator", "com/google/common/collect/AbstractSequentialIterator");

            // Moved in Guava 16.0
            mappingBuilder.addMapping("com/google/common/hash/HashFunction")
                    .method("hashString", "hashUnencodedChars", "(Ljava/lang/CharSequence;)Lcom/google/common/hash/HashCode;");
            mappingBuilder.addMapping("com/google/common/hash/Hasher")
                    .method("putString", "putUnencodedChars", "(Ljava/lang/CharSequence;)Lcom/google/common/hash/Hasher;");
            mappingBuilder.addMapping("com/google/common/hash/PrimitiveSink")
                    .method("putString", "putUnencodedChars", "(Ljava/lang/CharSequence;)Lcom/google/common/hash/PrimitiveSink;");
            mappingBuilder.addMapping("com/google/common/hash/Funnels")
                    .method("stringFunnel", "unencodedCharsFunnel", "()Lcom/google/common/hash/Funnel;");
        }
    }

    @Override
    public void registerPreVisitors(VisitorInfos visitorInfos) {
        String[] dimensionMapping = PortingHelper.getMapping("fabricated-fml", "net/minecraft/src/MapData.field_76200_c").split("\\.");

        visitorInfos.registerFieldRef(
                dimensionMapping[0],
                dimensionMapping[1],
                "",
                VisitorInfos.classMember(
                        dimensionMapping[0],
                        "dimensionId",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                PortingHelper.getMapping("fabricated-fml", "net/minecraft/src/WorldType"),
                "base11Biomes",
                "",
                VisitorInfos.classMember(
                        "io/github/fabriccompatibilitylayers/fabricatedfml/forged/ForgedWorldType",
                        "base11Biomes",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                PortingHelper.getMapping("fabricated-fml", "net/minecraft/src/WorldType"),
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

        if (!runningLegacyFabric) {
            // Removed in Guava 13.0
            visitorInfos.registerMethodInvocation(
                    "com/google/common/io/Files",
                    "getDigest",
                    "(Ljava/io/File;Ljava/security/MessageDigest;)[B",
                    VisitorInfos.classMember(
                            "io/github/fabriccompatibilitylayers/fabricatedfml/compat/guava/g13/GuavaStubs",
                            "io_Files_getDigest",
                            "(Ljava/io/File;Ljava/security/MessageDigest;)[B",
                            true
                    )
            );
            visitorInfos.registerMethodInvocation(
                    "com/google/common/io/ByteStreams",
                    "getDigest",
                    "(Lcom/google/common/io/InputSupplier;Ljava/security/MessageDigest;)[B",
                    VisitorInfos.classMember(
                            "io/github/fabriccompatibilitylayers/fabricatedfml/compat/guava/g13/GuavaStubs",
                            "io_ByteStreams_getDigest",
                            "(Lcom/google/common/io/InputSupplier;Ljava/security/MessageDigest;)[B",
                            true
                    )
            );

            // Moved in Guava 14.0
            visitorInfos.registerMethodInvocation(
                    "com/google/common/base/Equivalences",
                    "equals",
                    "()Lcom/google/common/base/Equivalence;",
                    VisitorInfos.classMember(
                            "com/google/common/base/Equivalence",
                            "equals",
                            "()Lcom/google/common/base/Equivalence;",
                            true
                    )
            );
            visitorInfos.registerMethodInvocation(
                    "com/google/common/base/Equivalences",
                    "identity",
                    "()Lcom/google/common/base/Equivalence;",
                    VisitorInfos.classMember(
                            "com/google/common/base/Equivalence",
                            "identity",
                            "()Lcom/google/common/base/Equivalence;",
                            true
                    )
            );

            // Moved in Guava 16.0
            visitorInfos.registerMethodInvocation(
                    "com/google/common/hash/HashCodes",
                    "fromBytes",
                    "([B)Lcom/google/common/hash/HashCode;",
                    VisitorInfos.classMember(
                            "com/google/common/hash/HashCode",
                            "fromBytes",
                            "([B)Lcom/google/common/hash/HashCode;",
                            true
                    )
            );
            visitorInfos.registerMethodInvocation(
                    "com/google/common/hash/HashCodes",
                    "fromInt",
                    "(I)Lcom/google/common/hash/HashCode;",
                    VisitorInfos.classMember(
                            "com/google/common/hash/HashCode",
                            "fromInt",
                            "(I)Lcom/google/common/hash/HashCode;",
                            true
                    )
            );
            visitorInfos.registerMethodInvocation(
                    "com/google/common/hash/HashCodes",
                    "fromLong",
                    "(J)Lcom/google/common/hash/HashCode;",
                    VisitorInfos.classMember(
                            "com/google/common/hash/HashCode",
                            "fromLong",
                            "(J)Lcom/google/common/hash/HashCode;",
                            true
                    )
            );
        }
    }
}
