package fr.catcore.fabricatedforge.mixin.forgefml.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.fabricatedforge.mixininterface.IFMLTextureFX;
import fr.catcore.modremapperapi.api.mixin.ChangeSuperClass;
import net.minecraft.client.class_589;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_589.class)
@ChangeSuperClass(FMLTextureFX.class)
public class class_589Mixin implements IFMLTextureFX {
    @Shadow protected float[] field_2171;

    @Shadow protected float[] field_2172;

    @Shadow protected float[] field_2173;

    @Shadow protected float[] field_2174;

    @Shadow private int field_2175;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        this.field_2171 = new float[getThis().tileSizeSquare];
        this.field_2172 = new float[getThis().tileSizeSquare];
        this.field_2173 = new float[getThis().tileSizeSquare];
        this.field_2174 = new float[getThis().tileSizeSquare];
        this.field_2175 = 0;
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public void method_1613() {
        ++this.field_2175;

        for(int var1 = 0; var1 < getThis().tileSizeBase; ++var1) {
            for(int var2 = 0; var2 < getThis().tileSizeBase; ++var2) {
                float var3 = 0.0F;

                for(int var4 = var2 - 2; var4 <= var2; ++var4) {
                    int var5 = var1 & getThis().tileSizeMask;
                    int var6 = var4 & getThis().tileSizeMask;
                    var3 += this.field_2171[var5 + var6 * getThis().tileSizeBase];
                }

                this.field_2172[var1 + var2 * getThis().tileSizeBase] = var3 / 3.2F + this.field_2173[var1 + var2 * getThis().tileSizeBase] * 0.8F;
            }
        }

        for(int var131 = 0; var131 < getThis().tileSizeBase; ++var131) {
            for(int var2 = 0; var2 < getThis().tileSizeBase; ++var2) {
                this.field_2173[var131 + var2 * getThis().tileSizeBase] += this.field_2174[var131 + var2 * getThis().tileSizeBase] * 0.05F;
                if (this.field_2173[var131 + var2 * getThis().tileSizeBase] < 0.0F) {
                    this.field_2173[var131 + var2 * getThis().tileSizeBase] = 0.0F;
                }

                this.field_2174[var131 + var2 * getThis().tileSizeBase] -= 0.3F;
                if (Math.random() < 0.2) {
                    this.field_2174[var131 + var2 * getThis().tileSizeBase] = 0.5F;
                }
            }
        }

        float[] var12 = this.field_2172;
        this.field_2172 = this.field_2171;
        this.field_2171 = var12;

        for(int var2 = 0; var2 < getThis().tileSizeSquare; ++var2) {
            float var3 = this.field_2171[var2 - this.field_2175 * getThis().tileSizeBase & getThis().tileSizeSquareMask];
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
