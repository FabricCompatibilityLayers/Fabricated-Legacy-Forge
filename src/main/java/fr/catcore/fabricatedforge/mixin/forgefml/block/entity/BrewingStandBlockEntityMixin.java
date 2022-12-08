package fr.catcore.fabricatedforge.mixin.forgefml.block.entity;

import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
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

    @Shadow protected abstract boolean method_508();

    @Shadow private ItemStack[] stacks;

    @Shadow protected abstract int method_505(int i, ItemStack itemStack);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private void method_509() {
        if (this.method_508()) {
            ItemStack var1 = this.stacks[3];

            for(int var2 = 0; var2 < 3; ++var2) {
                if (this.stacks[var2] != null && this.stacks[var2].id == Item.POTION.id) {
                    int var3 = this.stacks[var2].getMeta();
                    int var4 = this.method_505(var3, var1);
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
                this.stacks[3] = Item.ITEMS[var1.id].getContainerItemStack(this.stacks[3]);
            } else {
                --this.stacks[3].count;
                if (this.stacks[3].count <= 0) {
                    this.stacks[3] = null;
                }
            }
        }
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
