/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LiquidContainerData {
    public final LiquidStack stillLiquid;
    @Deprecated
    public LiquidStack movingLiquid;
    public final ItemStack filled;
    public final ItemStack container;

    @Deprecated
    public LiquidContainerData(int stillLiquidId, int movingLiquidId, Item filled) {
        this(new LiquidStack(stillLiquidId, 1000), new LiquidStack(movingLiquidId, 1000), new ItemStack(filled, 1), new ItemStack(Item.BUCKET));
    }

    @Deprecated
    public LiquidContainerData(int stillLiquidId, int movingLiquidId, ItemStack filled) {
        this(new LiquidStack(stillLiquidId, 1000), new LiquidStack(movingLiquidId, 1000), filled, new ItemStack(Item.BUCKET));
    }

    public LiquidContainerData(LiquidStack stillLiquid, ItemStack filled, ItemStack container) {
        this.stillLiquid = stillLiquid;
        this.filled = filled;
        this.container = container;
        if (stillLiquid == null || filled == null || container == null) {
            throw new RuntimeException("stillLiquid, filled, or container is null, this is an error");
        }
    }

    @Deprecated
    public LiquidContainerData(LiquidStack stillLiquid, LiquidStack movingLiquid, ItemStack filled, ItemStack container) {
        this(stillLiquid, filled, container);
        this.movingLiquid = movingLiquid;
    }
}
