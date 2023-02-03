package fr.catcore.fabricatedforge.mixin.forgefml.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.inventory.slot.class_1019;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_1019.class)
public class class_1019Mixin extends Slot {
    @Shadow @Final
    int field_4131;

    public class_1019Mixin(Inventory inventory, int invSlot, int x, int y) {
        super(inventory, invSlot, x, y);
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public boolean canInsert(ItemStack par1ItemStack) {
        Item item = par1ItemStack == null ? null : par1ItemStack.getItem();
        return item != null && item.isValidArmor(par1ItemStack, this.field_4131);
    }
}
