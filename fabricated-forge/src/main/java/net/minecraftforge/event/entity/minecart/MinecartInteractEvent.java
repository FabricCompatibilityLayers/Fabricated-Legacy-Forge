/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.minecart;

import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class MinecartInteractEvent extends MinecartEvent
{
    public final EntityPlayer player;

    public MinecartInteractEvent(EntityMinecart minecart, EntityPlayer player)
    {
        super(minecart);
        this.player = player;
    }
}
