/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class AttackEntityEvent extends PlayerEvent
{
    public final Entity target;
    public AttackEntityEvent(EntityPlayer player, Entity target)
    {
        super(player);
        this.target = target;
    }
}
