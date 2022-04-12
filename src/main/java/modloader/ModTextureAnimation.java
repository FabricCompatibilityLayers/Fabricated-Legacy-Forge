package modloader;

import net.minecraft.client.class_584;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ModTextureAnimation extends class_584 {
    private final int tickRate;
    private final byte[][] images;
    private int index;
    private int ticks;

    public ModTextureAnimation(int i, int j, BufferedImage bufferedimage, int k) {
        this(i, 1, j, bufferedimage, k);
    }

    public ModTextureAnimation(int i, int j, int k, BufferedImage bufferedimage, int l) {
        super(i);
        this.index = 0;
        this.ticks = 0;
        this.field_2156 = j;
        this.field_2157 = k;
        this.tickRate = l;
        this.ticks = l;
        this.method_1614(ModLoader.getMinecraftInstance().field_3813);
        int i1 = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
        int j1 = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
        int k1 = bufferedimage.getWidth();
        int l1 = bufferedimage.getHeight();
        int i2 = (int)Math.floor((double)(l1 / k1));
        if (i2 <= 0) {
            throw new IllegalArgumentException("source has no complete images");
        } else {
            this.images = new byte[i2][];
            if (k1 != i1) {
                BufferedImage bufferedimage1 = new BufferedImage(i1, j1 * i2, 6);
                Graphics2D graphics2d = bufferedimage1.createGraphics();
                graphics2d.drawImage(bufferedimage, 0, 0, i1, j1 * i2, 0, 0, k1, l1, (ImageObserver)null);
                graphics2d.dispose();
                bufferedimage = bufferedimage1;
            }

            for(int j2 = 0; j2 < i2; ++j2) {
                int[] ai = new int[i1 * j1];
                bufferedimage.getRGB(0, j1 * j2, i1, j1, ai, 0, i1);
                this.images[j2] = new byte[i1 * j1 * 4];

                for(int k2 = 0; k2 < ai.length; ++k2) {
                    int l2 = ai[k2] >> 24 & 255;
                    int i3 = ai[k2] >> 16 & 255;
                    int j3 = ai[k2] >> 8 & 255;
                    int k3 = ai[k2] >> 0 & 255;
                    this.images[j2][k2 * 4 + 0] = (byte)i3;
                    this.images[j2][k2 * 4 + 1] = (byte)j3;
                    this.images[j2][k2 * 4 + 2] = (byte)k3;
                    this.images[j2][k2 * 4 + 3] = (byte)l2;
                }
            }

        }
    }

    @Override
    public void method_1613() {
        if (this.ticks >= this.tickRate) {
            ++this.index;
            if (this.index >= this.images.length) {
                this.index = 0;
            }

            this.field_2152 = this.images[this.index];
            this.ticks = 0;
        }

        ++this.ticks;
    }
}
