/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event.sound;

import net.minecraft.src.SoundManager;
import net.minecraft.src.SoundPoolEntry;

public class PlaySoundEffectEvent extends SoundResultEvent
{
    public PlaySoundEffectEvent(SoundManager manager, SoundPoolEntry source, String name, float volume, float pitch)
    { super(manager, source, name, volume, pitch); }
}
