package fr.catcore.fabricatedforge.mixin.forgefml.client.render.optifine;

import fr.catcore.fabricatedforge.mixininterface.ITessellator;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Tessellator.class)
public abstract class TessellatorMixin implements ITessellator {
    @SuppressWarnings("UnresolvedMixinReference")
    @Shadow
    public static boolean renderingWorldRenderer;

    @SuppressWarnings("UnresolvedMixinReference")
    @Shadow
    public boolean defaultTexture;

    @SuppressWarnings("UnresolvedMixinReference")
    @Shadow
    public int textureID;

    @Override
    public boolean defaultTexture() {
        return this.defaultTexture;
    }

    @Override
    public boolean renderingWorldRenderer() {
        return renderingWorldRenderer;
    }

    @Override
    public void renderingWorldRenderer(boolean bool) {
        renderingWorldRenderer = bool;
    }

    @Override
    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    @Override
    public int getTextureID() {
        return this.textureID;
    }
}
