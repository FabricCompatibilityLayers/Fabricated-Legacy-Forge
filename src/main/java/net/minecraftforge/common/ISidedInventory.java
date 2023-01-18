/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import net.minecraft.inventory.Inventory;

public interface ISidedInventory extends Inventory {
    int getStartInventorySide(ForgeDirection forgeDirection);

    int getSizeInventorySide(ForgeDirection forgeDirection);
}
