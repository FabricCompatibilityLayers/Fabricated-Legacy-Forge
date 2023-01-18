/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Box;

public interface IMinecartCollisionHandler {
    void onEntityCollision(AbstractMinecartEntity arg, Entity arg2);

    Box getCollisionBox(AbstractMinecartEntity arg, Entity arg2);

    Box getMinecartCollisionBox(AbstractMinecartEntity arg);

    Box getBoundingBox(AbstractMinecartEntity arg);
}
