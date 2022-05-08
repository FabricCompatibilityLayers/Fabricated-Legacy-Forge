package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

@Cancelable
public class FillBucketEvent extends PlayerEvent {
    public final ItemStack current;
    public final World world;
    public final HitResult target;
    public ItemStack result;
    private boolean handeled = false;

    public FillBucketEvent(PlayerEntity player, ItemStack current, World world, HitResult target) {
        super(player);
        this.current = current;
        this.world = world;
        this.target = target;
    }

    public boolean isHandeled() {
        return this.handeled;
    }

    public void setHandeled() {
        this.handeled = true;
    }
}
