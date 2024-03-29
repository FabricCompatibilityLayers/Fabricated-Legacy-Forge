/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraftforge.event.Cancelable;

import java.util.ArrayList;

@Cancelable
public class LivingDropsEvent extends LivingEvent {
    public final DamageSource source;
    public final ArrayList<ItemEntity> drops;
    public final int lootingLevel;
    public final boolean recentlyHit;
    public final int specialDropValue;

    public LivingDropsEvent(MobEntity entity, DamageSource source, ArrayList<ItemEntity> drops, int lootingLevel, boolean recentlyHit, int specialDropValue) {
        super(entity);
        this.source = source;
        this.drops = drops;
        this.lootingLevel = lootingLevel;
        this.recentlyHit = recentlyHit;
        this.specialDropValue = specialDropValue;
    }
}
