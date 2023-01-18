/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.entity.mob.MobEntity;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.entity.EntityEvent;

public class LivingEvent extends EntityEvent {
    public final MobEntity entityLiving;

    public LivingEvent(MobEntity entity) {
        super(entity);
        this.entityLiving = entity;
    }

    public static class LivingJumpEvent extends LivingEvent {
        public LivingJumpEvent(MobEntity e) {
            super(e);
        }
    }

    @Cancelable
    public static class LivingUpdateEvent extends LivingEvent {
        public LivingUpdateEvent(MobEntity e) {
            super(e);
        }
    }
}
