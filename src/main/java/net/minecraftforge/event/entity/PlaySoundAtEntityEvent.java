package net.minecraftforge.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.net.minecraftforge.event.Cancelable;
import net.minecraft.net.minecraftforge.event.entity.EntityEvent;

@Cancelable
public class PlaySoundAtEntityEvent extends EntityEvent {
    public String name;
    public final float volume;
    public final float pitch;

    public PlaySoundAtEntityEvent(Entity entity, String name, float volume, float pitch) {
        super(entity);
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }
}
