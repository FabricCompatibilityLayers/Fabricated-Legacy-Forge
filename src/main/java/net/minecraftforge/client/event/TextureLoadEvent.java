/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event;

import net.minecraft.client.texture.ITexturePack;
import net.minecraftforge.event.Event;

public class TextureLoadEvent extends Event {
    public final String texture;
    public final ITexturePack pack;

    public TextureLoadEvent(String texture, ITexturePack pack) {
        this.texture = texture;
        this.pack = pack;
    }
}
