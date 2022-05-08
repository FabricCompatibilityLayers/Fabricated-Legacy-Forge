package cpw.mods.fml.client;

import net.minecraft.client.class_534;
import net.minecraft.client.texture.ITexturePack;

import java.awt.*;

public interface ITextureFX {
    void onTexturePackChanged(class_534 arg, ITexturePack arg2, Dimension dimension);

    void onTextureDimensionsUpdate(int i, int j);

    void setErrored(boolean bl);

    boolean getErrored();
}
