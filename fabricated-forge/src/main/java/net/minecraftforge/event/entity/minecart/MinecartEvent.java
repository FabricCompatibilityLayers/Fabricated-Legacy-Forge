/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.minecart;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;
import net.minecraftforge.event.entity.EntityEvent;

public class MinecartEvent extends EntityEvent
{
    public final EntityMinecart minecart;

    public MinecartEvent(EntityMinecart minecart)
    {
        super(minecart);
        this.minecart = minecart;
    }
}
