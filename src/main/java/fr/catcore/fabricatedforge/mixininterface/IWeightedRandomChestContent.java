package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface IWeightedRandomChestContent {
    ItemStack getItemStack();

    void setItemStack(ItemStack stack);
}
