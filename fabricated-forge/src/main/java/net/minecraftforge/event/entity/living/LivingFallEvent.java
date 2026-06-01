/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.src.EntityLiving;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class LivingFallEvent extends LivingEvent
{
    public float distance;
    public LivingFallEvent(EntityLiving entity, float distance)
    {
        super(entity);
        this.distance = distance;
    }
}
