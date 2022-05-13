package fr.catcore.fabricatedforge.mixininterface;

public interface ITessellator {

    boolean defaultTexture();

    boolean renderingWorldRenderer();
    void renderingWorldRenderer(boolean bool);

    int getTextureID();

    void setTextureID(int textureID);
}
