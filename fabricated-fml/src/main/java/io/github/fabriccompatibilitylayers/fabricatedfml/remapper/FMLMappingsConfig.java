/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper;

import fr.catcore.wfvaio.FabricVariants;
import fr.catcore.wfvaio.WhichFabricVariantAmIOn;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.MappingsConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FMLMappingsConfig implements MappingsConfig {
    @Override
    public @Nullable String getSourceNamespace() {
        return "official";
    }

    @Override
    public @Nullable Supplier<String> getExtraMappings() {
        return null;
    }

    @Override
    public Map<String, String> getRenamingMap() {
        return new HashMap<>();
    }

    @Override
    public @Nullable String getDefaultPackage() {
        String defaultPackage = "net/minecraft/";

        if (WhichFabricVariantAmIOn.getVariant() == FabricVariants.ORNITHE_V1 || WhichFabricVariantAmIOn.getVariant() == FabricVariants.ORNITHE_V2) {
            defaultPackage += "unmapped/";
        }

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            defaultPackage += "src/";
        }

        return defaultPackage;
    }
}
