package modloader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

public class EntityRendererProxy extends GameRenderer {
    private final MinecraftClient game;

    public EntityRendererProxy(MinecraftClient minecraft) {
        super(minecraft);
        this.game = minecraft;
    }

    @Override
    public void method_1331(float f) {
        super.method_1331(f);
        ModLoader.onTick(f, this.game);
    }
}
