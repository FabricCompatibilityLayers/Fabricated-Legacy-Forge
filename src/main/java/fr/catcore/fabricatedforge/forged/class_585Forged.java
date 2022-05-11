package fr.catcore.fabricatedforge.forged;

import cpw.mods.fml.client.FMLTextureFX;
import net.minecraft.block.Block;

public class class_585Forged extends FMLTextureFX {
    protected float[] field_2158 = new float[320];
    protected float[] field_2159 = new float[320];

    private int fireTileSize = 20;
    private int fireGridSize = 320;

    public class_585Forged(int i) {
        super(Block.FIRE.field_439 + i * 16);
        this.setup();
    }

    @Override
    public void setup() {
        super.setup();
        this.fireTileSize = this.tileSizeBase + (this.tileSizeBase >> 2);
        this.fireGridSize = this.fireTileSize * this.tileSizeBase;
        this.field_2158 = new float[this.fireGridSize];
        this.field_2159 = new float[this.fireGridSize];
    }

    @Override
    public void method_1613() {
        float fireFactor1 = 3.0F + (float)(this.tileSizeBase >> 4);
        float fireFactor2 = 1.01F + 0.8F / (float)this.tileSizeBase;

        int var3;
        float var4;
        int var6;
        int var7;
        int var8;
        for(int var1 = 0; var1 < this.tileSizeBase; ++var1) {
            for(var7 = 0; var7 < this.fireTileSize; ++var7) {
                var3 = this.fireTileSize - (this.tileSizeBase >> 3);
                var4 = this.field_2158[var1 + (var7 + 1) % this.fireTileSize * this.tileSizeBase] * (float)var3;

                for(var8 = var1 - 1; var8 <= var1 + 1; ++var8) {
                    for(var6 = var7; var6 <= var7 + 1; ++var6) {
                        if (var8 >= 0 && var6 >= 0 && var8 < this.tileSizeBase && var6 < this.fireTileSize) {
                            var4 += this.field_2158[var8 + var6 * this.tileSizeBase];
                        }

                        ++var3;
                    }
                }

                this.field_2159[var1 + var7 * this.tileSizeBase] = var4 / ((float)var3 * fireFactor2);
                if (var7 >= this.fireTileSize - (this.tileSizeBase >> 4)) {
                    this.field_2159[var1 + var7 * this.tileSizeBase] = (float)(Math.random() * Math.random() * Math.random() * (double)fireFactor1 + Math.random() * 0.10000000149011612 + 0.20000000298023224);
                }
            }
        }

        float[] var13 = this.field_2159;
        this.field_2159 = this.field_2158;
        this.field_2158 = var13;

        for(var3 = 0; var3 < this.tileSizeSquare; ++var3) {
            var4 = this.field_2158[var3] * 1.8F;
            if (var4 > 1.0F) {
                var4 = 1.0F;
            }

            if (var4 < 0.0F) {
                var4 = 0.0F;
            }

            var6 = (int)(var4 * 155.0F + 100.0F);
            var7 = (int)(var4 * var4 * 255.0F);
            var8 = (int)(var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * 255.0F);
            short var9 = 255;
            if (var4 < 0.5F) {
                var9 = 0;
            }

            float var14 = (var4 - 0.5F) * 2.0F;
            if (this.field_2154) {
                int var10 = (var6 * 30 + var7 * 59 + var8 * 11) / 100;
                int var11 = (var6 * 30 + var7 * 70) / 100;
                int var12 = (var6 * 30 + var8 * 70) / 100;
                var6 = var10;
                var7 = var11;
                var8 = var12;
            }

            this.field_2152[var3 * 4 + 0] = (byte)var6;
            this.field_2152[var3 * 4 + 1] = (byte)var7;
            this.field_2152[var3 * 4 + 2] = (byte)var8;
            this.field_2152[var3 * 4 + 3] = (byte)var9;
        }
    }
}
