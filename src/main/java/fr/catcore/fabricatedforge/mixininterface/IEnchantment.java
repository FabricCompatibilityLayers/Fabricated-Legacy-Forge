package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface IEnchantment {
    boolean canApplyAtEnchantingTable(ItemStack stack);
}
