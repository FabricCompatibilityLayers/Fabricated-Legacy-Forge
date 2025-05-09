package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.ItemStack;

public interface EntityLivingExtension extends EntityExtension {
    void curePotionEffects(ItemStack curativeItem);
}
