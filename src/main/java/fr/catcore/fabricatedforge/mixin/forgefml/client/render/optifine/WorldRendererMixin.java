package fr.catcore.fabricatedforge.mixin.forgefml.client.render.optifine;

import fr.catcore.fabricatedforge.mixininterface.IWorldRenderer;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements IWorldRenderer {
}
