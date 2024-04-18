package fr.catcore.fabricatedforge.compat.nei;

import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface NEIHandledScreen {
    List handleTooltip(int mousex, int mousey, List currenttip);

    List handleItemTooltip(ItemStack stack, int mousex, int mousey, List currenttip);

    void drawSlotItem(Slot par1Slot, ItemStack itemstack, int i, int j);

    void refresh();
}
