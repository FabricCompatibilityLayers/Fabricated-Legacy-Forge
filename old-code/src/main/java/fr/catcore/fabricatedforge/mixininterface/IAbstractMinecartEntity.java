package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IAbstractMinecartEntity {
    void dropCartAsItem();

    List<ItemStack> getItemsDropped();

    ItemStack getCartItem();

    boolean isPoweredCart();

    boolean isStorageCart();

    boolean canBeRidden();

    boolean canUseRail();

    void setCanUseRail(boolean use);

    boolean shouldDoRailFunctions();

    int getMinecartType();

    float getMaxSpeedRail();

    void setMaxSpeedRail(float value);

    float getMaxSpeedGround();

    void setMaxSpeedGround(float value);

    float getMaxSpeedAirLateral();

    void setMaxSpeedAirLateral(float value);

    float getMaxSpeedAirVertical();

    void setMaxSpeedAirVertical(float value);

    double getDragAir();

    void setDragAir(double value);
}
