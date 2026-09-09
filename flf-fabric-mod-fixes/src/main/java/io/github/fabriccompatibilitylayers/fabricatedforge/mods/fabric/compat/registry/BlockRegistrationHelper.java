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
import net.minecraft.src.NBTTagCompound;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class BlockRegistrationHelper {
    public static boolean ready = false;
    private static final Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();

    private static final Map<Block, String> blockIdentifiers = Maps.newHashMap();
    private static boolean identifiersSanitized = false;

    public static void registerBlock(Block block, ModContainer mc) {
        String modId = mc.getModId().toLowerCase(Locale.ENGLISH).replace("|", "__");
        String blockType = MappingsHelper.unmapClass(block.getClass().getName()).toLowerCase(Locale.ENGLISH);

        if (!modOrdinals.containsKey(modId)) {
            modOrdinals.put(modId, HashMultiset.create());
        }

        int ordinal = modOrdinals.getOrDefault(modId, HashMultiset.create()).add(blockType, 1);

        String blockIdentifier = modId + ":" + blockType + "_" + ordinal;
        blockIdentifiers.put(block, blockIdentifier);

        if (FabricLoader.getInstance().isModLoaded("osl-blocks")) {
            NamespacedIdentifier identifier = NamespacedIdentifiers.parse(blockIdentifier);
            BlockRegistry.register(identifier, block);
        }
    }

    public static String getId(Block block) {
        return blockIdentifiers.get(block);
    }

    public static void sanitize() {
        if (identifiersSanitized) return;

        identifiersSanitized = true;

        Set<Block> blocks = Sets.newHashSet(Block.blocksList);

        blockIdentifiers.entrySet().removeIf(entry -> !blocks.contains(entry.getKey()));
    }

    public static void write(NBTTagCompound nbt) {
        sanitize();

        for (Map.Entry<Block, String> entry : blockIdentifiers.entrySet()) {
            if (entry.getKey() == null || entry.getKey().blockID == 0 || entry.getValue() == null || entry.getValue().isEmpty()) continue;

            nbt.setInteger(entry.getValue(), entry.getKey().blockID);
        }
    }
}
