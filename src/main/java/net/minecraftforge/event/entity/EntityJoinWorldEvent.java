/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;

@Cancelable
public class EntityJoinWorldEvent extends EntityEvent {
    public final World world;

    public EntityJoinWorldEvent(Entity entity, World world) {
        super(entity);
        this.world = world;
    }
}
