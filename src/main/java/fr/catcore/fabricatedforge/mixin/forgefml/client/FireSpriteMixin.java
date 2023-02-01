package fr.catcore.fabricatedforge.mixin.forgefml.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.fabricatedforge.mixininterface.IFMLTextureFX;
import fr.catcore.modremapperapi.api.mixin.ChangeSuperClass;
import net.minecraft.client.FireSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireSprite.class)
@ChangeSuperClass(FMLTextureFX.class)
public class FireSpriteMixin implements IFMLTextureFX {

    @Shadow protected float[] field_2158;
    @Shadow protected float[] field_2159;
    private int fireTileSize = 20;
    private int fireGridSize = 320;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        this.fireTileSize = getThis().tileSizeBase + (getThis().tileSizeBase >> 2);
        this.fireGridSize = this.fireTileSize * getThis().tileSizeBase;
        this.field_2158 = new float[this.fireGridSize];
        this.field_2159 = new float[this.fireGridSize];
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public void method_1613() {
        float fireFactor1 = 3.0F + (float)(getThis().tileSizeBase >> 4);
        float fireFactor2 = 1.01F + 0.8F / (float)getThis().tileSizeBase;

        for(int var1 = 0; var1 < getThis().tileSizeBase; ++var1) {
            for(int var2 = 0; var2 < this.fireTileSize; ++var2) {
                int var3 = this.fireTileSize - (getThis().tileSizeBase >> 3);
                float var4 = this.field_2158[var1 + (var2 + 1) % this.fireTileSize * getThis().tileSizeBase] * (float)var3;

                for(int var5 = var1 - 1; var5 <= var1 + 1; ++var5) {
                    for(int var6 = var2; var6 <= var2 + 1; ++var6) {
                        if (var5 >= 0 && var6 >= 0 && var5 < getThis().tileSizeBase && var6 < this.fireTileSize) {
                            var4 += this.field_2158[var5 + var6 * getThis().tileSizeBase];
                        }

                        ++var3;
                    }
                }

                this.field_2159[var1 + var2 * getThis().tileSizeBase] = var4 / ((float)var3 * fireFactor2);
                if (var2 >= this.fireTileSize - (getThis().tileSizeBase >> 4)) {
                    this.field_2159[var1 + var2 * getThis().tileSizeBase] = (float)(
                            Math.random() * Math.random() * Math.random() * (double)fireFactor1 + Math.random() * 0.1F + 0.2F
                    );
                }
            }
        }

        float[] var13 = this.field_2159;
        this.field_2159 = this.field_2158;
        this.field_2158 = var13;

        for(int var3 = 0; var3 < getThis().tileSizeSquare; ++var3) {
            float var4 = this.field_2158[var3] * 1.8F;
            if (var4 > 1.0F) {
                var4 = 1.0F;
            }

            if (var4 < 0.0F) {
                var4 = 0.0F;
            }

            int var6 = (int)(var4 * 155.0F + 100.0F);
            int var7 = (int)(var4 * var4 * 255.0F);
            int var8 = (int)(var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * 255.0F);
            short var9 = 255;
            if (var4 < 0.5F) {
                var9 = 0;
            }

            float var14 = (var4 - 0.5F) * 2.0F;
            if (getThis().field_2154) {
                int var10 = (var6 * 30 + var7 * 59 + var8 * 11) / 100;
                int var11 = (var6 * 30 + var7 * 70) / 100;
                int var12 = (var6 * 30 + var8 * 70) / 100;
                var6 = var10;
                var7 = var11;
                var8 = var12;
            }

            getThis().field_2152[var3 * 4 + 0] = (byte)var6;
            getThis().field_2152[var3 * 4 + 1] = (byte)var7;
            getThis().field_2152[var3 * 4 + 2] = (byte)var8;
            getThis().field_2152[var3 * 4 + 3] = (byte)var9;
        }
    }
}
