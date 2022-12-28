package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface ISmeltingRecipeRegistry {
    @Deprecated
    void addSmelting(int itemID, int metadata, ItemStack itemstack);
    void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience);
    ItemStack getSmeltingResult(ItemStack item);
    float getExperience(ItemStack item);
}
