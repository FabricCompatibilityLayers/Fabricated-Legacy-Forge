package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EnchantmentExtension;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentExtension {
    @Shadow public EnumEnchantmentType type;

    /**
     * Called to determine if this enchantment can be applied to a ItemStack
     * @param item The ItemStack that the enchantment might be put on
     * @return True if the item is valid, false otherwise
     */
    @Override
    public boolean canEnchantItem(ItemStack item)
    {
        return type.canEnchantItem(item.getItem());
    }
}
