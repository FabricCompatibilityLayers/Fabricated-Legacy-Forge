package net.minecraftforge.event.entity.living;

import net.minecraft.entity.mob.MobEntity;

public class LivingEvent extends EntityEvent {
    public final MobEntity entityLiving;

    public LivingEvent(MobEntity entity) {
        super(entity);
        this.entityLiving = entity;
    }

    public static class LivingJumpEvent extends net.minecraft.net.minecraftforge.event.entity.living.LivingEvent {
        public LivingJumpEvent(MobEntity e) {
            super(e);
        }
    }

    @Cancelable
    public static class LivingUpdateEvent extends net.minecraft.net.minecraftforge.event.entity.living.LivingEvent {
        public LivingUpdateEvent(MobEntity e) {
            super(e);
        }
    }
}
