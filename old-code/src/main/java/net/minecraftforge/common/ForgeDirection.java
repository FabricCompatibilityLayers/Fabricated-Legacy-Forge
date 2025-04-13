/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

public enum ForgeDirection {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    UNKNOWN(0, 0, 0);

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;
    public final int flag;
    public static final int[] opposite = new int[]{1, 0, 3, 2, 5, 4, 6};

    private ForgeDirection(int x, int y, int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.flag = 1 << this.ordinal();
    }

    public static ForgeDirection getOrientation(int id) {
        return id >= 0 && id < values().length ? values()[id] : UNKNOWN;
    }

    public ForgeDirection getOpposite() {
        return getOrientation(opposite[this.ordinal()]);
    }
}
