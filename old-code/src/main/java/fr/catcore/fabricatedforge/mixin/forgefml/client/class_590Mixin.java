package fr.catcore.fabricatedforge.mixin.forgefml.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import fr.catcore.fabricatedforge.mixininterface.IFMLTextureFX;
import net.minecraft.client.class_590;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_590.class)
@ChangeSuperClass(FMLTextureFX.class)
public class class_590Mixin implements IFMLTextureFX {
    @Shadow protected float[] field_2176;

    @Shadow protected float[] field_2177;

    @Shadow protected float[] field_2178;

    @Shadow protected float[] field_2179;

    @Shadow private int field_2180;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        this.field_2176 = new float[getThis().tileSizeSquare];
        this.field_2177 = new float[getThis().tileSizeSquare];
        this.field_2178 = new float[getThis().tileSizeSquare];
        this.field_2179 = new float[getThis().tileSizeSquare];
        this.field_2180 = 0;
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public void method_1613() {
        ++this.field_2180;

        for(int var1 = 0; var1 < getThis().tileSizeBase; ++var1) {
            for(int var2 = 0; var2 < getThis().tileSizeBase; ++var2) {
                float var3 = 0.0F;

                for(int var4 = var1 - 1; var4 <= var1 + 1; ++var4) {
                    int var5 = var4 & getThis().tileSizeMask;
                    int var6 = var2 & getThis().tileSizeMask;
                    var3 += this.field_2176[var5 + var6 * getThis().tileSizeBase];
                }

                this.field_2177[var1 + var2 * getThis().tileSizeBase] = var3 / 3.3F + this.field_2178[var1 + var2 * getThis().tileSizeBase] * 0.8F;
            }
        }

        for(int var131 = 0; var131 < getThis().tileSizeBase; ++var131) {
            for(int var2 = 0; var2 < getThis().tileSizeBase; ++var2) {
                this.field_2178[var131 + var2 * getThis().tileSizeBase] += this.field_2179[var131 + var2 * getThis().tileSizeBase] * 0.05F;
                if (this.field_2178[var131 + var2 * getThis().tileSizeBase] < 0.0F) {
                    this.field_2178[var131 + var2 * getThis().tileSizeBase] = 0.0F;
                }

                this.field_2179[var131 + var2 * getThis().tileSizeBase] -= 0.1F;
                if (Math.random() < 0.05) {
                    this.field_2179[var131 + var2 * getThis().tileSizeBase] = 0.5F;
                }
            }
        }

        float[] var12 = this.field_2177;
        this.field_2177 = this.field_2176;
        this.field_2176 = var12;

        for(int var2 = 0; var2 < getThis().tileSizeSquare; ++var2) {
            float var3 = this.field_2176[var2];
            if (var3 > 1.0F) {
                var3 = 1.0F;
            }

            if (var3 < 0.0F) {
                var3 = 0.0F;
            }

            float var13 = var3 * var3;
            int var5 = (int)(32.0F + var13 * 32.0F);
            int var6 = (int)(50.0F + var13 * 64.0F);
            int var7 = 255;
            int var8 = (int)(146.0F + var13 * 50.0F);
            if (getThis().field_2154) {
                int var9 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
                int var10 = (var5 * 30 + var6 * 70) / 100;
                int var11 = (var5 * 30 + var7 * 70) / 100;
                var5 = var9;
                var6 = var10;
                var7 = var11;
            }

            getThis().field_2152[var2 * 4 + 0] = (byte)var5;
            getThis().field_2152[var2 * 4 + 1] = (byte)var6;
            getThis().field_2152[var2 * 4 + 2] = (byte)var7;
            getThis().field_2152[var2 * 4 + 3] = (byte)var8;
        }
    }
}
