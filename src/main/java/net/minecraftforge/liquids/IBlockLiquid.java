/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.nbt.NbtCompound;

public interface IBlockLiquid extends ILiquid {
    boolean willGenerateSources();

    int getFlowDistance();

    byte[] getLiquidRGB();

    String getLiquidBlockTextureFile();

    NbtCompound getLiquidProperties();

    public static enum BlockType {
        NONE,
        VANILLA,
        FINITE;

        private BlockType() {
        }
    }
}
