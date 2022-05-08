package net.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

public class EntityRendererProxy extends GameRenderer {
    public static final String fmlMarker = "This is an FML marker";
    private Minecraft game;

    public EntityRendererProxy(Minecraft minecraft) {
        super(minecraft);
        this.game = minecraft;
    }

    public void method_1331(float tick) {
        super.method_1331(tick);
    }
}
