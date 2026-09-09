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
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.NBTTagCompound;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.api.ItemRegistry;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ItemRegistrationHelper {
    public static boolean ready = false;
    private static final Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();

    private static final Map<Item, String> itemIdentifiers = Maps.newHashMap();
    private static boolean identifiersSanitized = false;

    public static void registerItem(Item item, ModContainer mc) {
        String modId = mc.getModId().toLowerCase(Locale.ENGLISH).replace("|", "__");
        String itemType = MappingsHelper.unmapClass(item.getClass().getName()).toLowerCase(Locale.ENGLISH);

        if (!modOrdinals.containsKey(modId)) {
            modOrdinals.put(modId, HashMultiset.create());
        }

        int ordinal = modOrdinals.getOrDefault(modId, HashMultiset.create()).add(itemType, 1);

        String itemIdentifier = modId + ":" + itemType + "_" + ordinal;
        itemIdentifiers.put(item, itemIdentifier);

        if (FabricLoader.getInstance().isModLoaded("osl-items")) {
            NamespacedIdentifier identifier = NamespacedIdentifiers.parse(itemIdentifier);
            ItemRegistry.register(identifier, item);
        }
    }

    public static void registerItemBlock(ItemBlock item, ModContainer mc) {
        Block block;

        if (FabricLoader.getInstance().isModLoaded("osl-items")) {
            block = BlockRegistry.getBlock(item.getBlockID());
        } else {
            block = Block.blocksList[item.getBlockID()];
        }

        if (block == null || block.blockID == 0) {
            registerItem(item, mc);
        } else {
            String blockIdentifier = BlockRegistrationHelper.getId(block);
            itemIdentifiers.put(item, blockIdentifier);

            if (FabricLoader.getInstance().isModLoaded("osl-items")) {
                ItemRegistry.register(block, item);
            }
        }
    }

    public static void sanitize() {
        if (identifiersSanitized) return;

        identifiersSanitized = true;

        Set<Item> items = Sets.newHashSet(Item.itemsList);

        itemIdentifiers.entrySet().removeIf(entry -> !items.contains(entry.getKey()));
    }

    public static void write(NBTTagCompound nbt) {
        sanitize();

        for (Map.Entry<Item, String> entry : itemIdentifiers.entrySet()) {
            if (entry.getKey() == null || entry.getKey().shiftedIndex == 0 || entry.getValue() == null || entry.getValue().isEmpty()) continue;

            nbt.setInteger(entry.getValue(), entry.getKey().shiftedIndex);
        }
    }
}
