/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.PotionEffectExtension;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PotionEffect.class)
public class PotionEffectMixin implements PotionEffectExtension {
    /** List of ItemStack that can cure the potion effect **/
    private List<ItemStack> curativeItems;

    @Inject(method = "<init>(IIIZ)V", at = @At("RETURN"))
    private void forge$setupCurativeItems(int par1, int par2, int par3, boolean par4, CallbackInfo ci) {
        this.curativeItems = new ArrayList<>();
        this.curativeItems.add(new ItemStack(Item.bucketMilk));
    }

    @Inject(method = "<init>(Lnet/minecraft/src/PotionEffect;)V", at = @At("RETURN"))
    private void forge$getCurativeItems(PotionEffect par1PotionEffect, CallbackInfo ci) {
        this.curativeItems = ((PotionEffectExtension) par1PotionEffect).getCurativeItems();
    }

    /***
     * Returns a list of curative items for the potion effect
     * @return The list (ItemStack) of curative items for the potion effect
     */
    @Override
    public List<ItemStack> getCurativeItems()
    {
        return this.curativeItems;
    }

    /***
     * Checks the given ItemStack to see if it is in the list of curative items for the potion effect
     * @param stack The ItemStack being checked against the list of curative items for the potion effect
     * @return true if the given ItemStack is in the list of curative items for the potion effect, false otherwise
     */
    @Override
    public boolean isCurativeItem(ItemStack stack)
    {
        boolean found = false;
        for (ItemStack curativeItem : this.curativeItems)
        {
            if (curativeItem.isItemEqual(stack))
            {
                found = true;
            }
        }

        return found;
    }

    /***
     * Sets the array of curative items for the potion effect
     * @param curativeItems The list of ItemStacks being set to the potion effect
     */
    @Override
    public void setCurativeItems(List<ItemStack> curativeItems)
    {
        this.curativeItems = curativeItems;
    }

    /***
     * Adds the given stack to list of curative items for the potion effect
     * @param stack The ItemStack being added to the curative item list
     */
    @Override
    public void addCurativeItem(ItemStack stack)
    {
        boolean found = false;
        for (ItemStack curativeItem : this.curativeItems)
        {
            if (curativeItem.isItemEqual(stack))
            {
                found = true;
            }
        }
        if (!found)
        {
            this.curativeItems.add(stack);
        }
    }
}
