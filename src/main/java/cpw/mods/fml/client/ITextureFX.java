package cpw.mods.fml.client;

import net.minecraft.client.TextureManager;
import net.minecraft.client.texture.ITexturePack;

import java.awt.*;

public interface ITextureFX {
    void onTexturePackChanged(TextureManager arg, ITexturePack arg2, Dimension dimension);

    void onTextureDimensionsUpdate(int i, int j);

    void setErrored(boolean bl);

    boolean getErrored();
}
