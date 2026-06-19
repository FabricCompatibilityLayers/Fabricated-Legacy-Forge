/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.Constants;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.FMLMappingsConfig;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.*;
import net.fabricmc.api.EnvType;

import java.util.Collections;
import java.util.List;

public class ForgeModsRemapper implements ModRemapper {
    @Override
    public String getContextId() {
        return Constants.CONTEXT_ID;
    }

    @Override
    public void init(CacheHandler cacheHandler) {

    }

    @Override
    public List<ModDiscovererConfig> getModDiscoverers() {
        return Collections.emptyList();
    }

    @Override
    public List<ModRemapper> collectSubRemappers(List<ModCandidate> list) {
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

    }

    @Override
    public void afterAllRemappings() {

    }

    @Override
    public void addRemappingLibraries(List<RemapLibrary> list, EnvType envType) {

    }

    @Override
    public void registerAdditionalMappings(MappingBuilder mappingBuilder) {

    }

    @Override
    public void registerPreVisitors(VisitorInfos visitorInfos) {

    }

    @Override
    public void registerPostVisitors(VisitorInfos visitorInfos) {

    }
}
