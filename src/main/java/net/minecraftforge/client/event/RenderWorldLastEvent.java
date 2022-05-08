package net.minecraftforge.client.event;

import net.minecraft.client.render.WorldRenderer;
import net.minecraftforge.event.Event;

public class RenderWorldLastEvent extends Event {
    public final WorldRenderer context;
    public final float partialTicks;

    public RenderWorldLastEvent(WorldRenderer context, float partialTicks) {
        this.context = context;
        this.partialTicks = partialTicks;
    }
}
