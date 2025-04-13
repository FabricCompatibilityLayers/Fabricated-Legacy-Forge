/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundSystem;

public class PlayBackgroundMusicEvent extends SoundResultEvent {
    public PlayBackgroundMusicEvent(SoundSystem manager, Sound entry) {
        super(manager, entry, (String)null, 0.0F, 0.0F);
    }
}
