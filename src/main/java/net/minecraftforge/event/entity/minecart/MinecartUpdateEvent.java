/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.minecart;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;

public class MinecartUpdateEvent extends MinecartEvent {
    public final float x;
    public final float y;
    public final float z;

    public MinecartUpdateEvent(AbstractMinecartEntity minecart, float x, float y, float z) {
        super(minecart);
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
