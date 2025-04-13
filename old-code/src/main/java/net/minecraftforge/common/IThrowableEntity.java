/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import net.minecraft.entity.Entity;

public interface IThrowableEntity {
    Entity getThrower();

    void setThrower(Entity arg);
}
