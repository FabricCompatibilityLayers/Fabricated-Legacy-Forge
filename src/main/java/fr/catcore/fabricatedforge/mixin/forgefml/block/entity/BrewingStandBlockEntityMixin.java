package fr.catcore.fabricatedforge.mixin.forgefml.block.entity;

import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.entity.effect.StatusEffectStrings;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends BlockEntity implements Inventory, ISidedInventory {

    @Shadow private ItemStack[] stacks;

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public boolean canBrew() {
        if (this.stacks[3] != null && this.stacks[3].count > 0) {
            ItemStack var1 = this.stacks[3];
            if (!((IItem)Item.ITEMS[var1.id]).isPotionIngredient(var1)) {
                return false;
            } else {
                boolean var2 = false;

                for(int var3 = 0; var3 < 3; ++var3) {
                    if (this.stacks[var3] != null && this.stacks[var3].id == Item.POTION.id) {
                        int var4 = this.stacks[var3].getData();
                        int var5 = this.getBrewEffectData(var4, var1);
                        if (!PotionItem.isThrowable(var4) && PotionItem.isThrowable(var5)) {
                            var2 = true;
                            break;
                        }

                        List var6 = Item.POTION.getPotionEffects(var4);
                        List var7 = Item.POTION.getPotionEffects(var5);
                        if ((var4 <= 0 || var6 != var7) && (var6 == null || !var6.equals(var7) && var7 != null) && var4 != var5) {
                            var2 = true;
                            break;
                        }
                    }
                }

                return var2;
            }
        } else {
            return false;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void brew() {
        if (this.canBrew()) {
            ItemStack var1 = this.stacks[3];

            for(int var2 = 0; var2 < 3; ++var2) {
                if (this.stacks[var2] != null && this.stacks[var2].id == Item.POTION.id) {
                    int var3 = this.stacks[var2].getData();
                    int var4 = this.getBrewEffectData(var3, var1);
                    List var5 = Item.POTION.getPotionEffects(var3);
                    List var6 = Item.POTION.getPotionEffects(var4);
                    if ((var3 <= 0 || var5 != var6) && (var5 == null || !var5.equals(var6) && var6 != null)) {
                        if (var3 != var4) {
                            this.stacks[var2].setDamage(var4);
                        }
                    } else if (!PotionItem.isThrowable(var3) && PotionItem.isThrowable(var4)) {
                        this.stacks[var2].setDamage(var4);
                    }
                }
            }

            if (Item.ITEMS[var1.id].isFood()) {
                this.stacks[3] = ((IItem)Item.ITEMS[var1.id]).getContainerItemStack(this.stacks[3]);
            } else {
                --this.stacks[3].count;
                if (this.stacks[3].count <= 0) {
                    this.stacks[3] = null;
                }
            }
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public int getBrewEffectData(int par1, ItemStack par2ItemStack) {
        return par2ItemStack == null
                ? par1
                : (
                ((IItem)Item.ITEMS[par2ItemStack.id]).isPotionIngredient(par2ItemStack)
                        ? StatusEffectStrings.getStatusEffectData(par1, ((IItem)Item.ITEMS[par2ItemStack.id]).getPotionEffect(par2ItemStack))
                        : par1
        );
    }

    @Override
    public int getStartInventorySide(ForgeDirection side) {
        return side == ForgeDirection.UP ? 3 : 0;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side) {
        return side == ForgeDirection.UP ? 1 : 3;
    }
}
