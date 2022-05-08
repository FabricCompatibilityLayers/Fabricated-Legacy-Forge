package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

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
