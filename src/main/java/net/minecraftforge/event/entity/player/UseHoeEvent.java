/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Cancelable
@Event.HasResult
public class UseHoeEvent extends PlayerEvent {
    public final ItemStack current;
    public final World world;
    public final int x;
    public final int y;
    public final int z;
    private boolean handeled = false;

    public UseHoeEvent(PlayerEntity player, ItemStack current, World world, int x, int y, int z) {
        super(player);
        this.current = current;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
