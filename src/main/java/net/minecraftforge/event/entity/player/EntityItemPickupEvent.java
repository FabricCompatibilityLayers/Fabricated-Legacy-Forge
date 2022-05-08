package net.minecraftforge.event.entity.player;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

@Cancelable
public class EntityItemPickupEvent extends PlayerEvent {
    public final ItemEntity item;
    private boolean handled = false;

    public EntityItemPickupEvent(PlayerEntity player, ItemEntity item) {
        super(player);
        this.item = item;
    }

    public boolean isHandled() {
        return this.handled;
    }

    public void setHandled() {
        this.handled = true;
    }
}
