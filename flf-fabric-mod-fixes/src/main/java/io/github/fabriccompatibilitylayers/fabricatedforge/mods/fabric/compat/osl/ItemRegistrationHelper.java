/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.blocks.api.block.Blocks;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.api.ItemRegistry;

import java.util.Locale;
import java.util.Map;

public class ItemRegistrationHelper {
    public static boolean ready = false;
    private static final Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();

    public static void registerItem(Item item, ModContainer mc) {
        String modId = mc.getModId().toLowerCase(Locale.ENGLISH).replace("|", "__");
        String itemType = MappingsHelper.unmapClass(item.getClass().getName()).toLowerCase(Locale.ENGLISH);

        if (!modOrdinals.containsKey(modId)) {
            modOrdinals.put(modId, HashMultiset.create());
        }

        int ordinal = modOrdinals.getOrDefault(modId, HashMultiset.create()).add(itemType, 1);
        NamespacedIdentifier identifier = NamespacedIdentifiers.from(modId, itemType + "_" + ordinal);

        ItemRegistry.register(identifier, item);
    }

    public static void registerItemBlock(ItemBlock item, ModContainer mc) {
        Block block = BlockRegistry.getBlock(item.getBlockID());

        if (block == null || block == Blocks.AIR) {
            registerItem(item, mc);
        } else {
            ItemRegistry.register(block, item);
        }
    }
}
