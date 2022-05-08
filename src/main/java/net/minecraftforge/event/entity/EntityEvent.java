package net.minecraftforge.event.entity;

import net.minecraft.entity.Entity;

public class EntityEvent extends Event {
    public final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public static class EnteringChunk extends net.minecraft.net.minecraftforge.event.entity.EntityEvent {
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

    public static class CanUpdate extends net.minecraft.net.minecraftforge.event.entity.EntityEvent {
        public boolean canUpdate = false;

        public CanUpdate(Entity entity) {
            super(entity);
        }
    }
}
