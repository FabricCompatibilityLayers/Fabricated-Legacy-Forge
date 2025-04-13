/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.entity.mob.MobEntity;

public class LivingSetAttackTargetEvent extends LivingEvent {
    public final MobEntity target;

    public LivingSetAttackTargetEvent(MobEntity entity, MobEntity target) {
        super(entity);
        this.target = target;
    }
}
