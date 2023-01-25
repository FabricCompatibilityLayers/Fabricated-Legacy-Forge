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
            Item var11 = Item.ITEMS[var4.id];
            int var13 = var11.getMaxDamage() - var4.getDamage();
            int var8 = var11.getMaxDamage() - var5.getDamage();
            int var9 = var13 + var8 + var11.getMaxDamage() * 5 / 100;
            int var10 = var11.getMaxDamage() - var9;
            if (var10 < 0) {
                var10 = 0;
            }

            return new ItemStack(var4.id, 1, var10);
        } else {
            for(int var121 = 0; var121 < this.recipes.size(); ++var121) {
                RecipeType var12x = (RecipeType)this.recipes.get(var121);
                if (var12x.matches(par1InventoryCrafting, par2World)) {
                    return var12x.getResult(par1InventoryCrafting);
                }
            }

            return null;
        }
    }
}
