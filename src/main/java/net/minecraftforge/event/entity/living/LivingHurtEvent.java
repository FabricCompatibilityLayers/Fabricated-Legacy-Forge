package net.minecraftforge.event.entity.living;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;

@Cancelable
public class LivingHurtEvent extends LivingEvent {
    public final DamageSource source;
    public int ammount;

    public LivingHurtEvent(MobEntity entity, DamageSource source, int ammount) {
        super(entity);
        this.source = source;
        this.ammount = ammount;
    }
}
