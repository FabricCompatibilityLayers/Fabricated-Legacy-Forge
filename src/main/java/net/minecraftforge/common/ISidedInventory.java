package net.minecraftforge.common;

import net.minecraft.inventory.Inventory;

public interface ISidedInventory extends Inventory {
    int getStartInventorySide(ForgeDirection forgeDirection);

    int getSizeInventorySide(ForgeDirection forgeDirection);
}
