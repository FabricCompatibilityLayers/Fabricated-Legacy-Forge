/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class EntityInteractEvent extends PlayerEvent {
    public final Entity target;

    public EntityInteractEvent(PlayerEntity player, Entity target) {
        super(player);
        this.target = target;
    }
}
