package fr.catcore.fabricatedforge.mixin.forgefml.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.inventory.slot.class_1004;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(class_1004.class)
public class class_1004Mixin extends Slot {
    public class_1004Mixin(Inventory inventory, int invSlot, int x, int y) {
        super(inventory, invSlot, x, y);
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public boolean canInsert(ItemStack par1ItemStack) {
        return par1ItemStack != null ? Item.ITEMS[par1ItemStack.id].isPotionIngredient(par1ItemStack) : false;
    }
}
