/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class ItemTossEvent extends ItemEvent {
    public final PlayerEntity player;

    public ItemTossEvent(ItemEntity entityItem, PlayerEntity player) {
        super(entityItem);
        this.player = player;
    }
}
