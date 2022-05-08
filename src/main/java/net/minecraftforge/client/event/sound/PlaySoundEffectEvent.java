package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundSystem;

public class PlaySoundEffectEvent extends SoundResultEvent {
    public PlaySoundEffectEvent(SoundSystem manager, Sound source, String name, float volume, float pitch) {
        super(manager, source, name, volume, pitch);
    }
}
