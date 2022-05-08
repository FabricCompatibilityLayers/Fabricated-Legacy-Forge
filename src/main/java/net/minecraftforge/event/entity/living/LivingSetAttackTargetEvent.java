package net.minecraftforge.event.entity.living;

import net.minecraft.entity.mob.MobEntity;

public class LivingSetAttackTargetEvent extends LivingEvent {
    public final MobEntity target;

    public LivingSetAttackTargetEvent(MobEntity entity, MobEntity target) {
        super(entity);
        this.target = target;
    }
}
