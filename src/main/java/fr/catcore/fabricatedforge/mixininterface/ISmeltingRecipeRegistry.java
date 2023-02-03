package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface ISmeltingRecipeRegistry {
    void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience);
    ItemStack getSmeltingResult(ItemStack item);
    float getExperience(ItemStack item);

    Map<List<Integer>, ItemStack> getMetaSmeltingList();
}
