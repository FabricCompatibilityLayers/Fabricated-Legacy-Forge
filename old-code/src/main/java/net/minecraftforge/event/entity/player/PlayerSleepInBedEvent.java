/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.CanSleepEnum;

public class PlayerSleepInBedEvent extends PlayerEvent {
    public CanSleepEnum result = null;
    public final int x;
    public final int y;
    public final int z;

    public PlayerSleepInBedEvent(PlayerEntity player, int x, int y, int z) {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
