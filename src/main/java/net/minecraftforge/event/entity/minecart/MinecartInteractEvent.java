/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.minecart;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class MinecartInteractEvent extends MinecartEvent {
    public final PlayerEntity player;

    public MinecartInteractEvent(AbstractMinecartEntity minecart, PlayerEntity player) {
        super(minecart);
        this.player = player;
    }
}
