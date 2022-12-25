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
    private int index = 0;
    private int ticks = 0;
    private String targetTex = null;
    private BufferedImage imgData = null;

    public ModTextureAnimation(int icon, int target, BufferedImage image, int tickCount) {
        this(icon, 1, target, image, tickCount);
    }

    public ModTextureAnimation(int icon, int size, int target, BufferedImage image, int tickCount) {
        this(icon, size, target == 0 ? "/terrain.png" : "/gui/items.png", image, tickCount);
    }

    public ModTextureAnimation(int icon, int size, String target, BufferedImage image, int tickCount) {
        super(icon);
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
                    this.images[frame][i4 + 0] = (byte)(pixels[i] >> 16 & 0xFF);
                    this.images[frame][i4 + 1] = (byte)(pixels[i] >> 8 & 0xFF);
                    this.images[frame][i4 + 2] = (byte)(pixels[i] >> 0 & 0xFF);
                    this.images[frame][i4 + 3] = (byte)(pixels[i] >> 24 & 0xFF);
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

    @Deprecated
    public void func_783_a() {
        this.method_1613();
    }
}
