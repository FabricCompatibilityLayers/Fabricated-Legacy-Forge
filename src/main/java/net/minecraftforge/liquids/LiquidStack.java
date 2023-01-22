/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class LiquidStack {
    public int itemID;
    public int amount;
    public int itemMeta;

    private LiquidStack() {
    }

    public LiquidStack(int itemID, int amount) {
        this(itemID, amount, 0);
    }

    public LiquidStack(Item item, int amount) {
        this(item.id, amount, 0);
    }

    public LiquidStack(Block block, int amount) {
        this(block.id, amount, 0);
    }

    public LiquidStack(int itemID, int amount, int itemDamage) {
        this.itemID = itemID;
        this.amount = amount;
        this.itemMeta = itemDamage;
    }

    public NbtCompound writeToNBT(NbtCompound nbttagcompound) {
        nbttagcompound.putShort("Id", (short)this.itemID);
        nbttagcompound.putInt("Amount", this.amount);
        nbttagcompound.putShort("Meta", (short)this.itemMeta);
        return nbttagcompound;
    }

    public void readFromNBT(NbtCompound nbttagcompound) {
        this.itemID = nbttagcompound.getShort("Id");
        this.amount = nbttagcompound.getInt("Amount");
        this.itemMeta = nbttagcompound.getShort("Meta");
    }

    public LiquidStack copy() {
        return new LiquidStack(this.itemID, this.amount, this.itemMeta);
    }

    public boolean isLiquidEqual(LiquidStack other) {
        if (other == null) {
            return false;
        } else {
            return this.itemID == other.itemID && this.itemMeta == other.itemMeta;
        }
    }

    public boolean containsLiquid(LiquidStack other) {
        if (!this.isLiquidEqual(other)) {
            return false;
        } else {
            return this.amount >= other.amount;
        }
    }

    public boolean isLiquidEqual(ItemStack other) {
        if (other == null) {
            return false;
        } else {
            return this.itemID == other.id && this.itemMeta == other.getData();
        }
    }

    public ItemStack asItemStack() {
        return new ItemStack(this.itemID, 1, this.itemMeta);
    }

    public static LiquidStack loadLiquidStackFromNBT(NbtCompound nbttagcompound) {
        LiquidStack liquidstack = new LiquidStack();
        liquidstack.readFromNBT(nbttagcompound);
        return liquidstack.itemID == 0 ? null : liquidstack;
    }
}
