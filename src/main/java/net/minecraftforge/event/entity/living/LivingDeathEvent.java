package net.minecraftforge.event.entity.living;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;

@Cancelable
public class LivingDeathEvent extends LivingEvent {
    public final DamageSource source;

    public LivingDeathEvent(MobEntity entity, DamageSource source) {
        super(entity);
        this.source = source;
    }
}
