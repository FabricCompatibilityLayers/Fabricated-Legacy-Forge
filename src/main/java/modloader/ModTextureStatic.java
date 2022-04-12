package modloader;

import net.minecraft.client.class_584;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ModTextureStatic extends class_584 {
    private boolean oldanaglyph;
    private int[] pixels;

    public ModTextureStatic(int i, int j, BufferedImage bufferedimage) {
        this(i, 1, j, bufferedimage);
    }

    public ModTextureStatic(int i, int j, int k, BufferedImage bufferedimage) {
        super(i);
        this.pixels = null;
        this.field_2156 = j;
        this.field_2157 = k;
        this.method_1614(ModLoader.getMinecraftInstance().field_3813);
        int l = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
        int i1 = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
        int j1 = bufferedimage.getWidth();
        int k1 = bufferedimage.getHeight();
        this.pixels = new int[l * i1];
        this.field_2152 = new byte[l * i1 * 4];
        if (j1 == k1 && j1 == l) {
            bufferedimage.getRGB(0, 0, j1, k1, this.pixels, 0, j1);
        } else {
            BufferedImage bufferedimage1 = new BufferedImage(l, i1, 6);
            Graphics2D graphics2d = bufferedimage1.createGraphics();
            graphics2d.drawImage(bufferedimage, 0, 0, l, i1, 0, 0, j1, k1, (ImageObserver)null);
            bufferedimage1.getRGB(0, 0, l, i1, this.pixels, 0, l);
            graphics2d.dispose();
        }

        this.update();
    }

    public void update() {
        for(int i = 0; i < this.pixels.length; ++i) {
            int j = this.pixels[i] >> 24 & 255;
            int k = this.pixels[i] >> 16 & 255;
            int l = this.pixels[i] >> 8 & 255;
            int i1 = this.pixels[i] >> 0 & 255;
            if (this.field_2154) {
                int j1 = (k + l + i1) / 3;
                i1 = j1;
                l = j1;
                k = j1;
            }

            this.field_2152[i * 4 + 0] = (byte)k;
            this.field_2152[i * 4 + 1] = (byte)l;
            this.field_2152[i * 4 + 2] = (byte)i1;
            this.field_2152[i * 4 + 3] = (byte)j;
        }

        this.oldanaglyph = this.field_2154;
    }

    @Override
    public void method_1613() {
        if (this.oldanaglyph != this.field_2154) {
            this.update();
        }

    }

    public static BufferedImage scale2x(BufferedImage bufferedimage) {
        int j2 = bufferedimage.getWidth();
        int k2 = bufferedimage.getHeight();
        BufferedImage bufferedimage1 = new BufferedImage(j2 * 2, k2 * 2, 2);

        for(int l2 = 0; l2 < k2; ++l2) {
            for(int i3 = 0; i3 < j2; ++i3) {
                int i = bufferedimage.getRGB(i3, l2);
                int j1;
                if (l2 == 0) {
                    j1 = i;
                } else {
                    j1 = bufferedimage.getRGB(i3, l2 - 1);
                }

                int k1;
                if (i3 == 0) {
                    k1 = i;
                } else {
                    k1 = bufferedimage.getRGB(i3 - 1, l2);
                }

                int l1;
                if (i3 >= j2 - 1) {
                    l1 = i;
                } else {
                    l1 = bufferedimage.getRGB(i3 + 1, l2);
                }

                int i2;
                if (l2 >= k2 - 1) {
                    i2 = i;
                } else {
                    i2 = bufferedimage.getRGB(i3, l2 + 1);
                }

                int j;
                int k;
                int l;
                int i1;
                if (j1 != i2 && k1 != l1) {
                    j = k1 != j1 ? i : k1;
                    k = j1 != l1 ? i : l1;
                    l = k1 != i2 ? i : k1;
                    i1 = i2 != l1 ? i : l1;
                } else {
                    j = i;
                    k = i;
                    l = i;
                    i1 = i;
                }

                bufferedimage1.setRGB(i3 * 2, l2 * 2, j);
                bufferedimage1.setRGB(i3 * 2 + 1, l2 * 2, k);
                bufferedimage1.setRGB(i3 * 2, l2 * 2 + 1, l);
                bufferedimage1.setRGB(i3 * 2 + 1, l2 * 2 + 1, i1);
            }
        }

        return bufferedimage1;
    }
}
