package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface IItemGroup {
    int getTabPage();

    ItemStack getIconItemStack();
}
