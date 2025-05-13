package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.ItemStack;

import java.util.List;

public interface PotionEffectExtension {
    List<ItemStack> getCurativeItems();

    boolean isCurativeItem(ItemStack stack);

    void setCurativeItems(List<ItemStack> curativeItems);

    void addCurativeItem(ItemStack stack);
}
