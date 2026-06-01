/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.ItemStack;

public interface FurnaceRecipesExtension {
    void addSmelting(int itemID, int metadata, ItemStack itemstack);

    ItemStack getSmeltingResult(ItemStack item);
}
