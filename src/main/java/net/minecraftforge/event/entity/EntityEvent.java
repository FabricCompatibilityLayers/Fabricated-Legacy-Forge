/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.Event;

public class EntityEvent extends Event {
    public final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public static class CanUpdate extends EntityEvent {
        public boolean canUpdate = false;

        public CanUpdate(Entity entity) {
            super(entity);
        }
    }

    public static class EnteringChunk extends EntityEvent {
        public int newChunkX;
        public int newChunkZ;
        public int oldChunkX;
        public int oldChunkZ;

        public EnteringChunk(Entity entity, int newChunkX, int newChunkZ, int oldChunkX, int oldChunkZ) {
            super(entity);
            this.newChunkX = newChunkX;
            this.newChunkZ = newChunkZ;
            this.oldChunkX = oldChunkX;
            this.oldChunkZ = oldChunkZ;
        }
    }
}
