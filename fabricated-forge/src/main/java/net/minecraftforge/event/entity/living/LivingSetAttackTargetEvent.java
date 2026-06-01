/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.src.EntityLiving;

public class LivingSetAttackTargetEvent extends LivingEvent
{

    public final EntityLiving target;
    public LivingSetAttackTargetEvent(EntityLiving entity, EntityLiving target)
    {
        super(entity);
        this.target = target;
    }

}
