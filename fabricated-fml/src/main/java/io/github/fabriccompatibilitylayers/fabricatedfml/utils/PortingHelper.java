/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.CustomValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PortingHelper {
    private static final Set<String> PARSED_MODIDS = new HashSet<>();
    private static final Map<String, String> MAPPINGS = new HashMap<>();

    public static String getMapping(String modid, String name) {
        if (!PARSED_MODIDS.contains(modid)) {
            parseMod(modid);
        }

        return MAPPINGS.getOrDefault(name, name);
    }

    private static void parseMod(String modid) {
        CustomValue.CvObject object = FabricLoader.getInstance().getModContainer(modid).get().getMetadata()
                .getCustomValue("flf:mappings").getAsObject();

        for (Map.Entry<String, CustomValue> entry : object) {
            MAPPINGS.put(entry.getKey(), entry.getValue().getAsString());
        }

        PARSED_MODIDS.add(modid);
    }
}
