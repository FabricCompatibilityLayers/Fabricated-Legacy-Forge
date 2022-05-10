package fr.catcore.fabricatedforge.mixin.forgefml.client;

import cpw.mods.fml.client.FMLTextureFX;
import net.minecraft.client.class_589;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_589.class) // TODO: make it somehow extend FMLTextureFX.
public class class_589Mixin extends FMLTextureFX {
    @Shadow protected float[] field_2171;

    @Shadow protected float[] field_2172;

    @Shadow protected float[] field_2173;

    @Shadow protected float[] field_2174;

    @Shadow private int field_2175;

    public class_589Mixin(int icon) {
        super(icon);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setupInCTR(CallbackInfo ci) {
        this.setup();
    }

    public void setup() {
        super.setup();
        this.field_2171 = new float[this.tileSizeSquare];
        this.field_2172 = new float[this.tileSizeSquare];
        this.field_2173 = new float[this.tileSizeSquare];
        this.field_2174 = new float[this.tileSizeSquare];
        this.field_2175 = 0;
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_1613() {
        ++this.field_2175;

        int var1;
        int var2;
        float var3;
        int var5;
        int var6;
        for(var1 = 0; var1 < this.tileSizeBase; ++var1) {
            for(var2 = 0; var2 < this.tileSizeBase; ++var2) {
                var3 = 0.0F;

                for(int var4 = var2 - 2; var4 <= var2; ++var4) {
                    var5 = var1 & this.tileSizeMask;
                    var6 = var4 & this.tileSizeMask;
                    var3 += this.field_2171[var5 + var6 * this.tileSizeBase];
                }

                this.field_2172[var1 + var2 * this.tileSizeBase] = var3 / 3.2F + this.field_2173[var1 + var2 * this.tileSizeBase] * 0.8F;
            }
        }

        for(var1 = 0; var1 < this.tileSizeBase; ++var1) {
            for(var2 = 0; var2 < this.tileSizeBase; ++var2) {
                float[] var10000 = this.field_2173;
                int var10001 = var1 + var2 * this.tileSizeBase;
                var10000[var10001] += this.field_2174[var1 + var2 * this.tileSizeBase] * 0.05F;
                if (this.field_2173[var1 + var2 * this.tileSizeBase] < 0.0F) {
                    this.field_2173[var1 + var2 * this.tileSizeBase] = 0.0F;
                }

                var10000 = this.field_2174;
                var10001 = var1 + var2 * this.tileSizeBase;
                var10000[var10001] -= 0.3F;
                if (Math.random() < 0.2) {
                    this.field_2174[var1 + var2 * this.tileSizeBase] = 0.5F;
                }
            }
        }

        float[] var12 = this.field_2172;
        this.field_2172 = this.field_2171;
        this.field_2171 = var12;

        for(var2 = 0; var2 < this.tileSizeSquare; ++var2) {
            var3 = this.field_2171[var2 - this.field_2175 * this.tileSizeBase & this.tileSizeSquareMask];
            if (var3 > 1.0F) {
                var3 = 1.0F;
            }

            if (var3 < 0.0F) {
                var3 = 0.0F;
            }

            float var13 = var3 * var3;
            var5 = (int)(32.0F + var13 * 32.0F);
            var6 = (int)(50.0F + var13 * 64.0F);
            int var7 = 255;
            int var8 = (int)(146.0F + var13 * 50.0F);
            if (this.field_2154) {
                int var9 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
                int var10 = (var5 * 30 + var6 * 70) / 100;
                int var11 = (var5 * 30 + var7 * 70) / 100;
                var5 = var9;
                var6 = var10;
                var7 = var11;
            }

            this.field_2152[var2 * 4 + 0] = (byte)var5;
            this.field_2152[var2 * 4 + 1] = (byte)var6;
            this.field_2152[var2 * 4 + 2] = (byte)var7;
            this.field_2152[var2 * 4 + 3] = (byte)var8;
        }

    }
}
