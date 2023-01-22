/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraftforge.common.ForgeDirection;

public interface ITankContainer {
    int fill(ForgeDirection forgeDirection, LiquidStack liquidStack, boolean bl);

    int fill(int i, LiquidStack liquidStack, boolean bl);

    LiquidStack drain(ForgeDirection forgeDirection, int i, boolean bl);

    LiquidStack drain(int i, int j, boolean bl);

    ILiquidTank[] getTanks(ForgeDirection forgeDirection);

    ILiquidTank getTank(ForgeDirection forgeDirection, LiquidStack liquidStack);
}
