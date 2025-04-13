package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface IEnchantment {
    public boolean canEnchantItem(ItemStack item);
}
