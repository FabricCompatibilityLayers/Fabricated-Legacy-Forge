package net.minecraftforge.event.entity.living;

import net.minecraft.entity.mob.MobEntity;

@Cancelable
public class LivingFallEvent extends LivingEvent {
    public float distance;

    public LivingFallEvent(MobEntity entity, float distance) {
        super(entity);
        this.distance = distance;
    }
}
