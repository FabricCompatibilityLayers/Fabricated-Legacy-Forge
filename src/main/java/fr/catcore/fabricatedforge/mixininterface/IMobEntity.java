package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

public interface IMobEntity {
    void method_2674(int par1);

    void curePotionEffects(ItemStack curativeItem);

    boolean shouldDropHead();
}
