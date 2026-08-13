/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.blocks;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import net.minecraft.src.Block;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

import java.util.Locale;
import java.util.Map;

public class BlockRegistrationHelper {
    public static boolean ready = false;
    private static final Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();

    public static void registerBlock(Block block, ModContainer mc) {
        String modId = mc.getModId().toLowerCase(Locale.ENGLISH).replace("|", "__");
        String blockType = MappingsHelper.unmapClass(block.getClass().getName()).toLowerCase(Locale.ENGLISH);

        if (!modOrdinals.containsKey(modId)) {
            modOrdinals.put(modId, HashMultiset.create());
        }

        int ordinal = modOrdinals.getOrDefault(modId, HashMultiset.create()).add(blockType, 1);
        NamespacedIdentifier identifier = NamespacedIdentifiers.from(modId, blockType + "_" + ordinal);
        BlockRegistry.register(identifier, block);
    }
}
