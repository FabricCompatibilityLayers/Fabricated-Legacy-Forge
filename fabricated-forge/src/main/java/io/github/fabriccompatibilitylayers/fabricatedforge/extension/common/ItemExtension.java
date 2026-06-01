/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.*;

public interface ItemExtension {
    boolean onDroppedByPlayer(ItemStack item, EntityPlayer player);

    boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);

    @Deprecated
    boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side);

    float getStrVsBlock(ItemStack itemstack, Block block, int metadata);

    boolean isRepairable();

    Item setNoRepair();

    boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player);

    void onUsingItemTick(ItemStack stack, EntityPlayer player, int count);

    boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);

    int getIconIndex(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining);

    int getRenderPasses(int metadata);

    String getTextureFile();

    void setTextureFile(String texture);

    ItemStack getContainerItemStack(ItemStack itemStack);

    int getEntityLifespan(ItemStack itemStack, World world);

    boolean hasCustomEntity(ItemStack stack);

    Entity createEntity(World world, Entity location, ItemStack itemstack);

    void setIsDefaultTexture(boolean isDefaultTexture);

    boolean isDefaultTexture();
}
