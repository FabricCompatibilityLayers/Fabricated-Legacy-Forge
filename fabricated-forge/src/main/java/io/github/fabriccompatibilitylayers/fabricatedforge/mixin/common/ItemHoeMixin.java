package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.UseHoeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemHoe.class)
public abstract class ItemHoeMixin extends Item implements ItemExtension {
    protected ItemHoeMixin(int par1) {
        super(par1);
    }

    @Inject(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 0), cancellable = true)
    private void forge$postUseHoeEvent(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10, CallbackInfoReturnable<Boolean> cir) {
        UseHoeEvent event = new UseHoeEvent(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6);
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            cir.setReturnValue(false);
            return;
        }
        if (event.isHandeled())
        {
            par1ItemStack.damageItem(1, par2EntityPlayer);
            cir.setReturnValue(true);
        }
    }
}
