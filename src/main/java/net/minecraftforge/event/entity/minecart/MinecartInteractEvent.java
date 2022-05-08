package net.minecraftforge.event.entity.minecart;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

@Cancelable
public class MinecartInteractEvent extends MinecartEvent {
    public final PlayerEntity player;

    public MinecartInteractEvent(AbstractMinecartEntity minecart, PlayerEntity player) {
        super(minecart);
        this.player = player;
    }
}
