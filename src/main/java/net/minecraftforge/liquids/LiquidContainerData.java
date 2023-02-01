/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LiquidContainerData {
    public final LiquidStack stillLiquid;
    public final ItemStack filled;
    public final ItemStack container;

    public LiquidContainerData(LiquidStack stillLiquid, ItemStack filled, ItemStack container) {
        this.stillLiquid = stillLiquid;
        this.filled = filled;
        this.container = container;
        if (stillLiquid == null || filled == null || container == null) {
            throw new RuntimeException("stillLiquid, filled, or container is null, this is an error");
        }
    }
}
