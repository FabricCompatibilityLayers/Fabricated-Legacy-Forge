package fr.catcore.fabricatedforge.compat.nei;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface NEIHandledScreen {
    List handleTooltip(int mousex, int mousey, List currenttip);

    List handleItemTooltip(ItemStack stack, int mousex, int mousey, List currenttip);

    void refresh();
}
