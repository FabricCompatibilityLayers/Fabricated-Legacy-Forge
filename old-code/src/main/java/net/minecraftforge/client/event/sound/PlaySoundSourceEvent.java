/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.SoundSystem;

public class PlaySoundSourceEvent extends SoundEvent {
    public final SoundSystem manager;
    public final String name;
    public final float x;
    public final float y;
    public final float z;

    public PlaySoundSourceEvent(SoundSystem manager, String name, float x, float y, float z) {
        this.manager = manager;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
