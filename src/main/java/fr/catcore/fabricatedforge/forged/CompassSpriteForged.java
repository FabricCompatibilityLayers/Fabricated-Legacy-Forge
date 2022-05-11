package fr.catcore.fabricatedforge.forged;

import cpw.mods.fml.client.FMLTextureFX;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CompassSpriteForged extends FMLTextureFX {
    private Minecraft field_2148;
    private int[] field_2149 = new int[256];
    private double field_2150;
    private double field_2151;

    public CompassSpriteForged(Minecraft par1Minecraft) {
        super(Item.COMPASS.method_3343(0));
        this.field_2148 = par1Minecraft;
        this.field_2157 = 1;
        this.setup();
    }

    public void setup() {
        super.setup();
        this.field_2149 = new int[this.tileSizeSquare];

        try {
            BufferedImage var2 = ImageIO.read(this.field_2148.texturePackManager.getCurrentTexturePack().openStream("/gui/items.png"));
            int var3 = this.field_2153 % 16 * this.tileSizeBase;
            int var4 = this.field_2153 / 16 * this.tileSizeBase;
            var2.getRGB(var3, var4, this.tileSizeBase, this.tileSizeBase, this.field_2149, 0, this.tileSizeBase);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void method_1613() {
        for(int var1 = 0; var1 < this.tileSizeSquare; ++var1) {
            int var2 = this.field_2149[var1] >> 24 & 255;
            int var3 = this.field_2149[var1] >> 16 & 255;
            int var4 = this.field_2149[var1] >> 8 & 255;
            int var5 = this.field_2149[var1] >> 0 & 255;
            if (this.field_2154) {
                int var6 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
                int var7 = (var3 * 30 + var4 * 70) / 100;
                int var8 = (var3 * 30 + var5 * 70) / 100;
                var3 = var6;
                var4 = var7;
                var5 = var8;
            }

            this.field_2152[var1 * 4 + 0] = (byte)var3;
            this.field_2152[var1 * 4 + 1] = (byte)var4;
            this.field_2152[var1 * 4 + 2] = (byte)var5;
            this.field_2152[var1 * 4 + 3] = (byte)var2;
        }

        double var20 = 0.0;
        if (this.field_2148.world != null && this.field_2148.playerEntity != null) {
            BlockPos var21 = this.field_2148.world.getWorldSpawnPos();
            double var23 = (double)var21.x - this.field_2148.playerEntity.x;
            double var25 = (double)var21.z - this.field_2148.playerEntity.z;
            var20 = (double)(this.field_2148.playerEntity.yaw - 90.0F) * Math.PI / 180.0 - Math.atan2(var25, var23);
            if (!this.field_2148.world.dimension.canPlayersSleep()) {
                var20 = Math.random() * Math.PI * 2.0;
            }
        }

        double var22;
        for(var22 = var20 - this.field_2150; var22 < -3.141592653589793; var22 += 6.283185307179586) {
        }

        while(var22 >= Math.PI) {
            var22 -= 6.283185307179586;
        }

        if (var22 < -1.0) {
            var22 = -1.0;
        }

        if (var22 > 1.0) {
            var22 = 1.0;
        }

        this.field_2151 += var22 * 0.1;
        this.field_2151 *= 0.8;
        this.field_2150 += this.field_2151;
        double var24 = Math.sin(this.field_2150);
        double var26 = Math.cos(this.field_2150);

        int var9;
        int var10;
        int var11;
        int var12;
        int var13;
        int var14;
        int var15;
        int var17;
        short var16;
        int var19;
        int var18;
        for(var9 = -(this.tileSizeBase >> 2); var9 <= this.tileSizeBase >> 2; ++var9) {
            var10 = (int)((double)(this.tileSizeBase >> 1) + 0.5 + var26 * (double)var9 * 0.3);
            var11 = (int)((double)(this.tileSizeBase >> 1) - 0.5 - var24 * (double)var9 * 0.3 * 0.5);
            var12 = var11 * this.tileSizeBase + var10;
            var13 = 100;
            var14 = 100;
            var15 = 100;
            var16 = 255;
            if (this.field_2154) {
                var17 = (var13 * 30 + var14 * 59 + var15 * 11) / 100;
                var18 = (var13 * 30 + var14 * 70) / 100;
                var19 = (var13 * 30 + var15 * 70) / 100;
                var13 = var17;
                var14 = var18;
                var15 = var19;
            }

            this.field_2152[var12 * 4 + 0] = (byte)var13;
            this.field_2152[var12 * 4 + 1] = (byte)var14;
            this.field_2152[var12 * 4 + 2] = (byte)var15;
            this.field_2152[var12 * 4 + 3] = (byte)var16;
        }

        for(var9 = -(this.tileSizeBase >> 2); var9 <= this.tileSizeBase; ++var9) {
            var10 = (int)((double)(this.tileSizeBase >> 1) + 0.5 + var24 * (double)var9 * 0.3);
            var11 = (int)((double)(this.tileSizeBase >> 1) - 0.5 + var26 * (double)var9 * 0.3 * 0.5);
            var12 = var11 * this.tileSizeBase + var10;
            var13 = var9 >= 0 ? 255 : 100;
            var14 = var9 >= 0 ? 20 : 100;
            var15 = var9 >= 0 ? 20 : 100;
            var16 = 255;
            if (this.field_2154) {
                var17 = (var13 * 30 + var14 * 59 + var15 * 11) / 100;
                var18 = (var13 * 30 + var14 * 70) / 100;
                var19 = (var13 * 30 + var15 * 70) / 100;
                var13 = var17;
                var14 = var18;
                var15 = var19;
            }

            this.field_2152[var12 * 4 + 0] = (byte)var13;
            this.field_2152[var12 * 4 + 1] = (byte)var14;
            this.field_2152[var12 * 4 + 2] = (byte)var15;
            this.field_2152[var12 * 4 + 3] = (byte)var16;
        }

    }
}
