package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundSystem;

public class PlayStreamingEvent extends SoundResultEvent {
    public final float x;
    public final float y;
    public final float z;

    public PlayStreamingEvent(SoundSystem manager, Sound source, String name, float x, float y, float z) {
        super(manager, source, name, 0.0F, 0.0F);
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
