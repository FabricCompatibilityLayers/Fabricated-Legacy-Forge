/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper.discoverer;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.candidate.ForgeModCandidate;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.ModCandidate;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.ModDiscovererConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ForgeModsDiscoverer implements ModDiscovererConfig.Collector {
    @Override
    public List<ModCandidate> collect(ModDiscovererConfig modDiscovererConfig, Path path, List<String> entries) {
        boolean fabric = false;
        boolean hasClass = false;

        for(String entry : entries) {
            if (entry.endsWith("fabric.mod.json") || entry.endsWith("quilt.mod.json") || entry.endsWith("quilt.mod.json5")) {
                fabric = true;
                break;
            }

            if (entry.endsWith(".class")) {
                hasClass = true;
            }
        }

        List<ModCandidate> list = new ArrayList();
        if (hasClass && !fabric) {
            list.add(new ForgeModCandidate(path, modDiscovererConfig));
        }

        return list;
    }
}
