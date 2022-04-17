package modloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

public class EntityRendererProxy extends GameRenderer {
    private final Minecraft game;

    public EntityRendererProxy(Minecraft minecraft) {
        super(minecraft);
        this.game = minecraft;
    }

    @Override
    public void method_1331(float f) {
        super.method_1331(f);
        ModLoader.onTick(f, this.game);
    }
}
