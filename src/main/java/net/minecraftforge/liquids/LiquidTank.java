/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.block.entity.BlockEntity;

public class LiquidTank implements ILiquidTank {
    private LiquidStack liquid;
    private int capacity;
    private int tankPressure;
    private BlockEntity tile;

    public LiquidTank(int capacity) {
        this(null, capacity);
    }

    public LiquidTank(int liquidId, int quantity, int capacity) {
        this(new LiquidStack(liquidId, quantity), capacity);
    }

    public LiquidTank(int liquidId, int quantity, int capacity, BlockEntity tile) {
        this(liquidId, quantity, capacity);
        this.tile = tile;
    }

    public LiquidTank(LiquidStack liquid, int capacity) {
        this.liquid = liquid;
        this.capacity = capacity;
    }

    public LiquidTank(LiquidStack liquid, int capacity, BlockEntity tile) {
        this(liquid, capacity);
        this.tile = tile;
    }

    public LiquidStack getLiquid() {
        return this.liquid;
    }

    public void setLiquid(LiquidStack liquid) {
        this.liquid = liquid;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int fill(LiquidStack resource, boolean doFill) {
        if (resource == null || resource.itemID <= 0) {
            return 0;
        } else if (this.liquid != null && this.liquid.itemID > 0) {
            if (!this.liquid.isLiquidEqual(resource)) {
                return 0;
            } else {
                int space = this.capacity - this.liquid.amount;
                if (resource.amount <= space) {
                    if (doFill) {
                        this.liquid.amount += resource.amount;
                    }

                    return resource.amount;
                } else {
                    if (doFill) {
                        this.liquid.amount = this.capacity;
                    }

                    return space;
                }
            }
        } else if (resource.amount <= this.capacity) {
            if (doFill) {
                this.liquid = resource.copy();
            }

            return resource.amount;
        } else {
            if (doFill) {
                this.liquid = resource.copy();
                this.liquid.amount = this.capacity;
                if (this.tile != null) {
                    LiquidEvent.fireEvent(new LiquidEvent.LiquidFillingEvent(this.liquid, this.tile.world, this.tile.x, this.tile.y, this.tile.z, this));
                }
            }

            return this.capacity;
        }
    }

    public LiquidStack drain(int maxDrain, boolean doDrain) {
        if (this.liquid == null || this.liquid.itemID <= 0) {
            return null;
        } else if (this.liquid.amount <= 0) {
            return null;
        } else {
            int used = maxDrain;
            if (this.liquid.amount < maxDrain) {
                used = this.liquid.amount;
            }

            if (doDrain) {
                this.liquid.amount -= used;
            }

            LiquidStack drained = new LiquidStack(this.liquid.itemID, used, this.liquid.itemMeta);
            if (this.liquid.amount <= 0) {
                this.liquid = null;
            }

            if (doDrain && this.tile != null) {
                LiquidEvent.fireEvent(new LiquidEvent.LiquidDrainingEvent(drained, this.tile.world, this.tile.x, this.tile.y, this.tile.z, this));
            }

            return drained;
        }
    }

    public int getTankPressure() {
        return this.tankPressure;
    }

    public void setTankPressure(int pressure) {
        this.tankPressure = pressure;
    }
}
