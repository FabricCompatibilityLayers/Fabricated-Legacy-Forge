/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

public interface ILiquidTank {
    LiquidStack getLiquid();

    @Deprecated
    void setLiquid(LiquidStack liquidStack);

    @Deprecated
    void setCapacity(int i);

    int getCapacity();

    int fill(LiquidStack liquidStack, boolean bl);

    LiquidStack drain(int i, boolean bl);

    int getTankPressure();
}
