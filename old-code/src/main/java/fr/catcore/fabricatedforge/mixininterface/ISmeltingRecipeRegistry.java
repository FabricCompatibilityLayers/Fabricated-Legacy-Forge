package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface ISmeltingRecipeRegistry {
    void addSmelting(int itemID, int metadata, ItemStack itemstack);

    ItemStack getSmeltingResult(ItemStack item);
}
