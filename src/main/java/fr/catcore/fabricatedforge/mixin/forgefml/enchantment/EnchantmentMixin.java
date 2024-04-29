package fr.catcore.fabricatedforge.mixin.forgefml.enchantment;

import com.google.common.collect.ObjectArrays;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.fabricatedforge.mixininterface.IEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements IEnchantment {

    @Shadow public EnchantmentTarget target;

    @Shadow @Final public static Enchantment[] field_5457;

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return this.target.method_3518(stack.getItem());
    }

    @Public
    private static void addToBookList(Enchantment enchantment) {
        ObjectArrays.concat(field_5457, enchantment);
    }
}
