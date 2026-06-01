/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumStatus;

public class PlayerSleepInBedEvent extends PlayerEvent
{
    public EnumStatus result = null;
    public final int x;
    public final int y;
    public final int z;

    public PlayerSleepInBedEvent(EntityPlayer player, int x, int y, int z)
    {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
