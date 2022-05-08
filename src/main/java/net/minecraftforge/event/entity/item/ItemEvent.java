package net.minecraftforge.event.entity.item;

import net.minecraft.entity.ItemEntity;

public class ItemEvent extends EntityEvent {
    public final ItemEntity entityItem;

    public ItemEvent(ItemEntity itemEntity) {
        super(itemEntity);
        this.entityItem = itemEntity;
    }
}
