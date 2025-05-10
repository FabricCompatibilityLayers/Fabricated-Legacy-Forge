package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.ItemStack;

public interface FurnaceRecipesExtension {
    void addSmelting(int itemID, int metadata, ItemStack itemstack);

    ItemStack getSmeltingResult(ItemStack item);
}
