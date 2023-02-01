package fr.catcore.fabricatedforge.mixin.forgefml.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static Map method_3528(int par0, ItemStack par1ItemStack) {
        Item var2 = par1ItemStack.getItem();
        HashMap var3 = null;
        boolean var4 = par1ItemStack.id == Item.BOOK.id;

        for(Enchantment var8 : Enchantment.ALL_ENCHANTMENTS) {
            if (var8 != null && (var8.canApplyAtEnchantingTable(par1ItemStack) || var4)) {
                for(int var9 = var8.getMinimumLevel(); var9 <= var8.getMaximumLevel(); ++var9) {
                    if (par0 >= var8.getMinimumPower(var9) && par0 <= var8.getMaximumPower(var9)) {
                        if (var3 == null) {
                            var3 = new HashMap();
                        }

                        var3.put(var8.id, new EnchantmentLevelEntry(var8, var9));
                    }
                }
            }
        }

        return var3;
    }
}
