/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBucket.class)
public abstract class ItemBucketMixin extends Item implements ItemExtension {
    protected ItemBucketMixin(int par1) {
        super(par1);
    }

    @Inject(method = "onItemRightClick", at = @At(value = "FIELD", target = "Lnet/minecraft/src/MovingObjectPosition;typeOfHit:Lnet/minecraft/src/EnumMovingObjectType;"), cancellable = true)
    private void forge$postFillBucketEvent(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, CallbackInfoReturnable<ItemStack> cir,
                                           @Local MovingObjectPosition var12) {
        FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, var12);
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            cir.setReturnValue(par1ItemStack);
            return;
        }

        if (event.getResult() == Event.Result.ALLOW)
        {
            if (par3EntityPlayer.capabilities.isCreativeMode)
            {
                cir.setReturnValue(par1ItemStack);
                return;
            }

            if (--par1ItemStack.stackSize <= 0)
            {
                cir.setReturnValue(event.result);
                return;
            }

            if (!par3EntityPlayer.inventory.addItemStackToInventory(event.result))
            {
                par3EntityPlayer.dropPlayerItem(event.result);
            }

            cir.setReturnValue(par1ItemStack);
        }
    }
}
