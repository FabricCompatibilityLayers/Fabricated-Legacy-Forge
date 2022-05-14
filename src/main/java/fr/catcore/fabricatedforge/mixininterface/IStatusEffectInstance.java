package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IStatusEffectInstance {
    public List<ItemStack> getCurativeItems();

    public boolean isCurativeItem(ItemStack stack);

    public void setCurativeItems(List<ItemStack> curativeItems);

    public void addCurativeItem(ItemStack stack);
}
