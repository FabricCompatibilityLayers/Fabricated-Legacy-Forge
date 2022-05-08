package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PlayerDestroyItemEvent extends PlayerEvent {
    public final ItemStack original;

    public PlayerDestroyItemEvent(PlayerEntity player, ItemStack original) {
        super(player);
        this.original = original;
    }
}
