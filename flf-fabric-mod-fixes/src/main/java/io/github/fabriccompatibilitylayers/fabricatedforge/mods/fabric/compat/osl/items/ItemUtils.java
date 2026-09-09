/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.ornithemc.osl.items.api.ItemRegistry;

public class ItemUtils {
    public static int blockId(ItemStack item) {
        return item.getItem() instanceof ItemBlock ? ((ItemBlock)item.getItem()).getBlockID() : 0;
    }

    public static int itemId(int block) {
        return itemId(Block.blocksList[block]);
    }

    public static int itemId(Block block) {
        Item item = ItemRegistry.getItem(block);
        return item == null ? 0 : item.shiftedIndex;
    }
}
