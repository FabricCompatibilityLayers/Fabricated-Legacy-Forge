/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.IdUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.NBTTagCompound;
import net.ornithemc.osl.biomes.api.BiomeRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class BiomeRegistrationHelper {
    public static boolean ready = false;
    private static final Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();

    private static final Map<BiomeGenBase, String> biomeIdentifiers = Maps.newHashMap();
    private static boolean identifiersSanitized = false;

    public static void registerBiome(BiomeGenBase biome) {
        String modId = IdUtils.getModId();
        String biomeType = MappingsHelper.unmapClass(biome.getClass().getName()).toLowerCase(Locale.ENGLISH);

        if (!modOrdinals.containsKey(modId)) {
            modOrdinals.put(modId, HashMultiset.create());
        }

        int ordinal = modOrdinals.getOrDefault(modId, HashMultiset.create()).add(biomeType, 1);

        String biomeIdentifier = modId + ":" + biomeType + "_" + ordinal;
        biomeIdentifiers.put(biome, biomeIdentifier);

        if (FabricLoader.getInstance().isModLoaded("osl-biomes")) {
            NamespacedIdentifier identifier = NamespacedIdentifiers.parse(biomeIdentifier);
            BiomeRegistry.register(identifier, biome);
        }
    }


    public static String getId(BiomeGenBase biome) {
        return biomeIdentifiers.get(biome);
    }

    public static void sanitize() {
        if (identifiersSanitized) return;

        identifiersSanitized = true;

        Set<BiomeGenBase> biomes = Sets.newHashSet(BiomeGenBase.biomeList);

        biomeIdentifiers.entrySet().removeIf(entry -> !biomes.contains(entry.getKey()));
    }

    public static void write(NBTTagCompound nbt) {
        sanitize();

        for (Map.Entry<BiomeGenBase, String> entry : biomeIdentifiers.entrySet()) {
            if (entry.getKey() == null || entry.getKey().biomeID == 0 || entry.getValue() == null || entry.getValue().isEmpty()) continue;

            nbt.setInteger(entry.getValue(), entry.getKey().biomeID);
        }
    }
}
