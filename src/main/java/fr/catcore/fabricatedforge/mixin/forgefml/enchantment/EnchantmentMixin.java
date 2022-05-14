package fr.catcore.fabricatedforge.mixin.forgefml.enchantment;

import fr.catcore.fabricatedforge.mixininterface.IEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements IEnchantment {

    @Shadow public EnchantmentTarget target;

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return this.target.method_3518(item.getItem());
    }
}
