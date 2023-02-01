package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface IItemRenderer {
    boolean shouldSpreadItems();

    boolean shouldBob();

    byte getMiniBlockCountForItemStack(ItemStack stack);

    byte getMiniItemCountForItemStack(ItemStack stack);
}
