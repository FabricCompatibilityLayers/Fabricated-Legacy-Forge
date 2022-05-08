package net.minecraftforge.event.entity.item;

import net.minecraft.entity.ItemEntity;

@Cancelable
public class ItemExpireEvent extends ItemEvent {
    public int extraLife;

    public ItemExpireEvent(ItemEntity entityItem, int extraLife) {
        super(entityItem);
        this.extraLife = extraLife;
    }
}
