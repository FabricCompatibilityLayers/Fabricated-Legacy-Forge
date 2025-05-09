package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EnchantmentExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Redirect(method = "mapEnchantmentData", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EnumEnchantmentType;canEnchantItem(Lnet/minecraft/src/Item;)Z"))
    private static boolean forge$canEnchantItem(EnumEnchantmentType instance, Item item,
                                                @Local(argsOnly = true) ItemStack par1ItemStack,
                                                @Local Enchantment var7) {
        return ((EnchantmentExtension) var7).canEnchantItem(par1ItemStack);
    }
}
