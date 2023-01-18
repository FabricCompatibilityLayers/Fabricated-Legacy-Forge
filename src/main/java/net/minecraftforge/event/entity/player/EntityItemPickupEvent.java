/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

@Cancelable
@Event.HasResult
public class EntityItemPickupEvent extends PlayerEvent {
    public final ItemEntity item;
    private boolean handled = false;

    public EntityItemPickupEvent(PlayerEntity player, ItemEntity item) {
        super(player);
        this.item = item;
    }
}
