/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.item;

import net.minecraft.entity.ItemEntity;
import net.minecraftforge.event.entity.EntityEvent;

public class ItemEvent extends EntityEvent {
    public final ItemEntity entityItem;

    public ItemEvent(ItemEntity itemEntity) {
        super(itemEntity);
        this.entityItem = itemEntity;
    }
}
