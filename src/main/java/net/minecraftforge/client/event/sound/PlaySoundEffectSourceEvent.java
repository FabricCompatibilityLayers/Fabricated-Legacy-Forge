package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.SoundSystem;

public class PlaySoundEffectSourceEvent extends SoundEvent {
    public final SoundSystem manager;
    public final String name;

    public PlaySoundEffectSourceEvent(SoundSystem manager, String name) {
        this.manager = manager;
        this.name = name;
    }
}
