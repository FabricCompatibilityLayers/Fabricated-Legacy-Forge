package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemDye.class)
public abstract class ItemDyeMixin extends Item implements ItemExtension {
    protected ItemDyeMixin(int par1) {
        super(par1);
    }

    @Inject(method = "onItemUse", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;sapling:Lnet/minecraft/src/Block;", ordinal = 0), cancellable = true)
    private void forge$postBonemealEvent(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10, CallbackInfoReturnable<Boolean> cir,
                                         @Local(ordinal = 4) int var17) {
        BonemealEvent event = new BonemealEvent(par2EntityPlayer, par3World, var17, par4, par5, par6);
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            cir.setReturnValue(false);
            return;
        }

        if (event.isHandeled())
        {
            if (!par3World.isRemote)
            {
                par1ItemStack.stackSize--;
            }
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 5, remap = false))
    private int forge$hackCheck1(Random instance, int i) {
        return 0;
    }

    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFlower;canBlockStay(Lnet/minecraft/src/World;III)Z", ordinal = 1))
    private boolean forge$hackCheck2(BlockFlower instance, World par2, int par3, int par4, int i) {
        return true;
    }

    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockWithNotify(IIII)Z", ordinal = 1))
    private boolean forge$plantGrass(World instance, int par2, int par3, int par4, int i) {
        ForgeHooks.plantGrass(instance, par2, par3, par4);
        return true;
    }
}
