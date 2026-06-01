/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SlotCrafting.class)
public class SlotCraftingMixin extends Slot {
    @Shadow private EntityPlayer thePlayer;

    public SlotCraftingMixin(IInventory par1IInventory, int par2, int par3, int par4) {
        super(par1IInventory, par2, par3, par4);
    }

    @Redirect(method = "onPickupFromSlot", at = @At(value = "NEW", target = "(Lnet/minecraft/src/Item;)Lnet/minecraft/src/ItemStack;"))
    private ItemStack forge$postPlayerDestroyItemEvent(Item item,
                                                       @Local(ordinal = 1) ItemStack var3) {
        ItemStack var4 = ((ItemExtension) var3.getItem()).getContainerItemStack(var3);

        if (var4.isItemStackDamageable() && var4.getItemDamage() > var4.getMaxDamage())
        {
            MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, var4));
            var4 = null;
        }

        return var4;
    }

    @WrapOperation(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Item;doesContainerItemLeaveCraftingGrid(Lnet/minecraft/src/ItemStack;)Z"))
    private boolean forge$NonNull1(Item instance, ItemStack itemStack, Operation<Boolean> original,
                                   @Local(ordinal = 2) ItemStack var4) {
        return var4 == null || original.call(instance, itemStack);
    }

    @WrapOperation(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;addItemStackToInventory(Lnet/minecraft/src/ItemStack;)Z"))
    private boolean forge$NonNull2(InventoryPlayer instance, ItemStack itemStack, Operation<Boolean> original) {
        return itemStack == null || original.call(instance, itemStack);
    }
}
