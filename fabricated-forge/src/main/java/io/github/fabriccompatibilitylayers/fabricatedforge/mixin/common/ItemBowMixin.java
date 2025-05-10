package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBow.class)
public abstract class ItemBowMixin extends Item implements ItemExtension {
    protected ItemBowMixin(int par1) {
        super(par1);
    }

    @Inject(method = "onPlayerStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void forge$postArrowLooseEvent(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4, CallbackInfo ci,
                                           @Share(value = "itemDuration", namespace = "fabricated-forge") LocalIntRef var6Ref) {
        int var6 = this.getMaxItemUseDuration(par1ItemStack) - par4;

        ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, var6);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
        {
            ci.cancel();
        } else {
            var6Ref.set(event.charge);
        }
    }

    @Redirect(method = "onPlayerStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemBow;getMaxItemUseDuration(Lnet/minecraft/src/ItemStack;)I"))
    private int forge$getMovedVar(ItemBow instance, ItemStack itemStack,
                                  @Local(argsOnly = true) int par4,
                                  @Share(value = "itemDuration", namespace = "fabricated-forge") LocalIntRef var6Ref) {
        return var6Ref.get() + par4;
    }

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    private void forge$postArrowNockEvent(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, CallbackInfoReturnable<ItemStack> cir) {
        ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer, par1ItemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
        {
            cir.setReturnValue(event.result);
        }
    }
}
