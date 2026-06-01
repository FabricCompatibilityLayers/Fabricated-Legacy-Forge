/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class LivingDeathEvent extends LivingEvent
{
    public final DamageSource source;
    public LivingDeathEvent(EntityLiving entity, DamageSource source)
    {
        super(entity);
        this.source = source;
    }

}
