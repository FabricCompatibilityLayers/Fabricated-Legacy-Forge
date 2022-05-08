package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.SoundSystem;

public class SoundLoadEvent extends SoundEvent {
    public final SoundSystem manager;

    public SoundLoadEvent(SoundSystem manager) {
        this.manager = manager;
    }
}
