/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import net.minecraft.src.ItemStack;

public interface FLFFixableRecipe {
    boolean flf$osl$canFixRecipe(ItemMapper var1);

    void flf$osl$fixRecipe(ItemMapper var1);

    public interface ItemMapper {
        int mapItem(int var1);

        default boolean canFixItem(int item) {
            return this.mapItem(item) >= 0;
        }

        default boolean canFixItem(ItemStack item) {
            return this.mapItem(item.itemID) >= 0;
        }

        default void fixItem(ItemStack item) {
            item.itemID = this.mapItem(item.itemID);
        }
    }
}
