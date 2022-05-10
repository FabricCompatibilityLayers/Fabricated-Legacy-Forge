package net.minecraftforge.event.entity.minecart;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraftforge.event.entity.EntityEvent;

public class MinecartEvent extends EntityEvent {
    public final AbstractMinecartEntity minecart;

    public MinecartEvent(AbstractMinecartEntity minecart) {
        super(minecart);
        this.minecart = minecart;
    }
}
