package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.SoundSystem;

public class SoundSetupEvent extends SoundEvent {
    public final SoundSystem manager;

    public SoundSetupEvent(SoundSystem manager) {
        this.manager = manager;
    }
}
