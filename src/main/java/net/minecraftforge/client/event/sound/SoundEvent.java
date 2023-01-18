/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event.sound;

import net.minecraft.client.sound.Sound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

public class SoundEvent extends Event {
    public SoundEvent() {
    }

    public static Sound getResult(SoundResultEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
        return event.result;
    }
}
