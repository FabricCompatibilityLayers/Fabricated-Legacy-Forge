package net.minecraftforge.event.entity.minecart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

public class MinecartCollisionEvent extends MinecartEvent {
    public final Entity collider;

    public MinecartCollisionEvent(AbstractMinecartEntity minecart, Entity collider) {
        super(minecart);
        this.collider = collider;
    }
}
