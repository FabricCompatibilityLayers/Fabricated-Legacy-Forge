package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IMobEntity {
    void curePotionEffects(ItemStack curativeItem);

    boolean shouldRiderFaceForward(PlayerEntity player);

    float getField_3344();

    void setField_3344(float field_3344);
}
