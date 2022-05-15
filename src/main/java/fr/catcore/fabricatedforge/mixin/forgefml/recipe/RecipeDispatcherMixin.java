package fr.catcore.fabricatedforge.mixin.forgefml.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(RecipeDispatcher.class)
public class RecipeDispatcherMixin {

    @Shadow private List recipes;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public ItemStack method_3494(CraftingInventory par1InventoryCrafting) {
        int var2 = 0;
        ItemStack var3 = null;
        ItemStack var4 = null;

        for(int var5 = 0; var5 < par1InventoryCrafting.getInvSize(); ++var5) {
            ItemStack var6 = par1InventoryCrafting.getInvStack(var5);
            if (var6 != null) {
                if (var2 == 0) {
                    var3 = var6;
                }

                if (var2 == 1) {
                    var4 = var6;
                }

                ++var2;
            }
        }

        if (var2 == 2 && var3.id == var4.id && var3.count == 1 && var4.count == 1 && Item.ITEMS[var3.id].isRepairable()) {
            Item var10 = Item.ITEMS[var3.id];
            int var12 = var10.getMaxDamage() - var3.getDamage();
            int var7 = var10.getMaxDamage() - var4.getDamage();
            int var8 = var12 + var7 + var10.getMaxDamage() * 10 / 100;
            int var9 = var10.getMaxDamage() - var8;
            if (var9 < 0) {
                var9 = 0;
            }

            return new ItemStack(var3.id, 1, var9);
        } else {
            for (Object recipe : this.recipes) {
                RecipeType var13 = (RecipeType) recipe;
                if (var13.method_3500(par1InventoryCrafting)) {
                    return var13.getResult(par1InventoryCrafting);
                }
            }

            return null;
        }
    }
}
