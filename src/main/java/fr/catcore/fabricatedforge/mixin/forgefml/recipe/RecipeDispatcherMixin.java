package fr.catcore.fabricatedforge.mixin.forgefml.recipe;

import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(RecipeDispatcher.class)
public class RecipeDispatcherMixin {

    @Shadow private List<RecipeType> recipes;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ItemStack matches(CraftingInventory par1InventoryCrafting, World par2World) {
        int var3 = 0;
        ItemStack var4 = null;
        ItemStack var5 = null;

        for(int var6 = 0; var6 < par1InventoryCrafting.getInvSize(); ++var6) {
            ItemStack var7 = par1InventoryCrafting.getInvStack(var6);
            if (var7 != null) {
                if (var3 == 0) {
                    var4 = var7;
                }

                if (var3 == 1) {
                    var5 = var7;
                }

                ++var3;
            }
        }

        if (var3 == 2 && var4.id == var5.id && var4.count == 1 && var5.count == 1 && Item.ITEMS[var4.id].isRepairable()) {
            Item var13 = Item.ITEMS[var4.id];
            int var14 = var13.getMaxDamage() - var4.getDamage();
            int var8 = var13.getMaxDamage() - var5.getDamage();
            int var9 = var14 + var8 + var13.getMaxDamage() * 5 / 100;
            int var10 = var13.getMaxDamage() - var9;
            if (var10 < 0) {
                var10 = 0;
            }

            return new ItemStack(var4.id, 1, var10);
        } else {
            for(RecipeType var12 : this.recipes) {
                if (var12.matches(par1InventoryCrafting, par2World)) {
                    return var12.getResult(par1InventoryCrafting);
                }
            }

            return null;
        }
    }
}
