/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.item;

import net.minecraft.entity.ItemEntity;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class ItemExpireEvent extends ItemEvent {
    public int extraLife;

    public ItemExpireEvent(ItemEntity entityItem, int extraLife) {
        super(entityItem);
        this.extraLife = extraLife;
    }
}
