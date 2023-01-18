/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundSystem;

public class PlaySoundEvent extends SoundResultEvent {
    public final float x;
    public final float y;
    public final float z;

    public PlaySoundEvent(SoundSystem manager, Sound source, String name, float x, float y, float z, float volume, float pitch) {
        super(manager, source, name, volume, pitch);
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
