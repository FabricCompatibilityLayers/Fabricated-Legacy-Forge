/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityLivingExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucketMilk;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemBucketMilk.class)
public abstract class ItemBucketMilkMixin extends Item implements ItemExtension {
    protected ItemBucketMilkMixin(int par1) {
        super(par1);
    }

    @Redirect(method = "onFoodEaten", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayer;clearActivePotions()V"))
    private void forge$curePotionEffects(EntityPlayer instance,
                                         @Local(argsOnly = true) ItemStack par1ItemStack) {
        ((EntityLivingExtension) instance).curePotionEffects(par1ItemStack);
    }
}
