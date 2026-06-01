/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event;

import net.minecraft.src.TexturePackBase;
import net.minecraftforge.event.Event;

public class TextureLoadEvent extends Event
{

    public final String texture;
    public final TexturePackBase pack;

    public TextureLoadEvent(String texture, TexturePackBase pack)
    {
        this.texture = texture;
        this.pack = pack;
    }
}
