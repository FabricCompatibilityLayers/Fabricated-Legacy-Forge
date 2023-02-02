/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.living;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;

public class LivingSpawnEvent extends LivingEvent {
    public final World world;
    public final float x;
    public final float y;
    public final float z;

    public LivingSpawnEvent(MobEntity entity, World world, float x, float y, float z) {
        super(entity);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @HasResult
    public static class CheckSpawn extends LivingSpawnEvent {
        public CheckSpawn(MobEntity entity, World world, float x, float y, float z) {
            super(entity, world, x, y, z);
        }
    }

    @Cancelable
    public static class SpecialSpawn extends LivingSpawnEvent {
        public SpecialSpawn(MobEntity entity, World world, float x, float y, float z) {
            super(entity, world, x, y, z);
        }
    }
}
