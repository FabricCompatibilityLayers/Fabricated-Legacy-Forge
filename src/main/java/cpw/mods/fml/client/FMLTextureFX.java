/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
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

    public void setErrored(boolean err) {
        this.errored = err;
    }

    public boolean getErrored() {
        return this.errored;
    }

    public void onTexturePackChanged(TextureManager engine, ITexturePack texturepack, Dimension dimensions) {
        this.onTextureDimensionsUpdate(dimensions.width, dimensions.height);
    }

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
