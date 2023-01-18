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
package net.minecraft;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLTextureFX;
import net.minecraft.client.TextureManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ModTextureAnimation extends FMLTextureFX {
    private final int tickRate;
    private byte[][] images;
    private int index;
    private int ticks;
    private String targetTex;
    private BufferedImage imgData;

    public ModTextureAnimation(int icon, int target, BufferedImage image, int tickCount) {
        this(icon, 1, target, image, tickCount);
    }

    public ModTextureAnimation(int icon, int size, int target, BufferedImage image, int tickCount) {
        this(icon, size, target == 0 ? "/terrain.png" : "/gui/items.png", image, tickCount);
    }

    public ModTextureAnimation(int icon, int size, String target, BufferedImage image, int tickCount) {
        super(icon);
        this.index = 0;
        this.ticks = 0;
        this.targetTex = null;
        this.imgData = null;
        TextureManager re = FMLClientHandler.instance().getClient().textureManager;
        this.targetTex = target;
        this.field_2156 = size;
        this.field_2157 = re.getTextureFromPath(target);
        this.tickRate = tickCount;
        this.ticks = tickCount;
        this.imgData = image;
    }

    public void setup() {
        super.setup();
        int sWidth = this.imgData.getWidth();
        int sHeight = this.imgData.getHeight();
        int tWidth = this.tileSizeBase;
        int tHeight = this.tileSizeBase;
        int frames = (int)Math.floor((double)(sHeight / sWidth));
        if (frames < 1) {
            throw new IllegalArgumentException(String.format("Attempted to create a TextureAnimation with no complete frames: %dx%d", sWidth, sHeight));
        } else {
            this.images = new byte[frames][];
            BufferedImage image = this.imgData;
            if (sWidth != tWidth) {
                BufferedImage b = new BufferedImage(tWidth, tHeight * frames, 6);
                Graphics2D g = b.createGraphics();
                g.drawImage(this.imgData, 0, 0, tWidth, tHeight * frames, 0, 0, sWidth, sHeight, (ImageObserver)null);
                g.dispose();
                image = b;
            }

            for(int frame = 0; frame < frames; ++frame) {
                int[] pixels = new int[this.tileSizeSquare];
                image.getRGB(0, tHeight * frame, tWidth, tHeight, pixels, 0, tWidth);
                this.images[frame] = new byte[this.tileSizeSquare << 2];

                for(int i = 0; i < pixels.length; ++i) {
                    int i4 = i * 4;
                    this.images[frame][i4 + 0] = (byte)(pixels[i] >> 16 & 255);
                    this.images[frame][i4 + 1] = (byte)(pixels[i] >> 8 & 255);
                    this.images[frame][i4 + 2] = (byte)(pixels[i] >> 0 & 255);
                    this.images[frame][i4 + 3] = (byte)(pixels[i] >> 24 & 255);
                }
            }

        }
    }

    public void method_1613() {
        if (++this.ticks >= this.tickRate) {
            if (++this.index >= this.images.length) {
                this.index = 0;
            }

            this.field_2152 = this.images[this.index];
            this.ticks = 0;
        }

    }

    public void method_1614(TextureManager renderEngine) {
        GL11.glBindTexture(3553, this.field_2157);
    }

    /** @deprecated */
    @Deprecated
    public void func_783_a() {
        this.method_1613();
    }
}
