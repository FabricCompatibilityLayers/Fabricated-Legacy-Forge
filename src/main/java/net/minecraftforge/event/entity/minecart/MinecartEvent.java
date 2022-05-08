package net.minecraftforge.event.entity.minecart;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;

public class MinecartEvent extends EntityEvent {
    public final AbstractMinecartEntity minecart;

    public MinecartEvent(AbstractMinecartEntity minecart) {
        super(minecart);
        this.minecart = minecart;
    }
}
