/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class ArrowNockEvent extends PlayerEvent {
    public ItemStack result;

    public ArrowNockEvent(PlayerEntity player, ItemStack result) {
        super(player);
        this.result = result;
    }
}
