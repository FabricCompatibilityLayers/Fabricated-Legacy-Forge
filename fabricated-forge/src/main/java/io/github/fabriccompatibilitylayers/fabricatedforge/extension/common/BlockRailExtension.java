/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.EntityMinecart;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public interface BlockRailExtension extends BlockExtension {
    void setRenderType(int value);

    boolean isFlexibleRail(World world, int y, int x, int z);

    boolean canMakeSlopes(World world, int x, int y, int z);

    int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z);

    float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z);

    void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z);

    boolean hasPowerBit(World world, int x, int y, int z);
}
