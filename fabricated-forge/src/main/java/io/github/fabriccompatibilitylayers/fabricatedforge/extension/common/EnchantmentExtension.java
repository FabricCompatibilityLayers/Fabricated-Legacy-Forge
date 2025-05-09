package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.ItemStack;

public interface EnchantmentExtension {
    boolean canEnchantItem(ItemStack item);
}
