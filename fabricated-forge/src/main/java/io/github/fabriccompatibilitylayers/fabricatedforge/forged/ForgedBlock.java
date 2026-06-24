/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.forged;


import net.minecraftforge.common.ForgeDirection;

public class ForgedBlock {
    public static int[] blockFireSpreadSpeed = new int[4096];
    public static int[] blockFlammability = new int[4096];

    public static ForgeDirection findDirection(int x, int y, int z, int origX, int origY, int origZ) {
        int diffX = origX - x;
        int diffY = origY - y;
        int diffZ = origZ - z;

        for (ForgeDirection direction : ForgeDirection.values()) {
            if (direction.offsetX == diffX && direction.offsetY == diffY && direction.offsetZ == diffZ) {
                return direction;
            }
        }
        return null;
    }
}
