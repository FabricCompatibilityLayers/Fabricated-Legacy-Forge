/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

public interface ILiquid {
    int stillLiquidId();

    boolean isMetaSensitive();

    int stillLiquidMeta();
}
