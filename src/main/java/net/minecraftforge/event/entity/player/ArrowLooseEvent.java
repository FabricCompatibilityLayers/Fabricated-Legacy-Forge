/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class ArrowLooseEvent extends PlayerEvent {
    public final ItemStack bow;
    public int charge;

    public ArrowLooseEvent(PlayerEntity player, ItemStack bow, int charge) {
        super(player);
        this.bow = bow;
        this.charge = charge;
    }
}
