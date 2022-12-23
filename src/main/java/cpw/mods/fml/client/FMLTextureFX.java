package cpw.mods.fml.client;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.TextureManager;
import net.minecraft.client.Sprite;
import net.minecraft.client.texture.ITexturePack;

import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class FMLTextureFX extends Sprite implements ITextureFX {
    public int tileSizeBase = 16;
    public int tileSizeSquare = 256;
    public int tileSizeMask = 15;
    public int tileSizeSquareMask = 255;
    public boolean errored = false;
    protected Logger log = FMLLog.getLogger();

    public FMLTextureFX(int icon) {
        super(icon);
    }

    @Override
    public void setErrored(boolean err) {
        this.errored = err;
    }

    @Override
    public boolean getErrored() {
        return this.errored;
    }

    @Override
    public void onTexturePackChanged(TextureManager engine, ITexturePack texturepack, Dimension dimensions) {
        this.onTextureDimensionsUpdate(dimensions.width, dimensions.height);
    }

    @Override
    public void onTextureDimensionsUpdate(int width, int height) {
        this.tileSizeBase = width >> 4;
        this.tileSizeSquare = this.tileSizeBase * this.tileSizeBase;
        this.tileSizeMask = this.tileSizeBase - 1;
        this.tileSizeSquareMask = this.tileSizeSquare - 1;
        this.setErrored(false);
        this.setup();
    }

    protected void setup() {
        this.field_2152 = new byte[this.tileSizeSquare << 2];
    }

    public boolean unregister(TextureManager engine, List<Sprite> effects) {
        effects.remove(this);
        return true;
    }
}
