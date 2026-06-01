/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event;

import net.minecraft.src.RenderGlobal;
import net.minecraftforge.event.Event;

public class RenderWorldLastEvent extends Event
{
    public final RenderGlobal context;
    public final float partialTicks;
    public RenderWorldLastEvent(RenderGlobal context, float partialTicks)
    {
        this.context = context;
        this.partialTicks = partialTicks;
    }
}
