package fr.catcore.fabricatedforge.mixin.forgefml.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import fr.catcore.fabricatedforge.mixininterface.IFMLTextureFX;
import net.minecraft.client.NetherPortalSprite;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetherPortalSprite.class)
@ChangeSuperClass(FMLTextureFX.class)
public class NetherPortalSpriteMixin implements IFMLTextureFX {
    @Shadow private byte[][] field_2170;

    @Shadow private int field_2169;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        this.field_2170 = new byte[32][getThis().tileSizeSquare << 4];
        Random var1 = new Random(100L);

        for(int var2 = 0; var2 < 32; ++var2) {
            for(int var3 = 0; var3 < getThis().tileSizeBase; ++var3) {
                for(int var4 = 0; var4 < getThis().tileSizeBase; ++var4) {
                    float var5 = 0.0F;

                    for(int var6 = 0; var6 < 2; ++var6) {
                        float var7 = (float)(var6 * getThis().tileSizeBase) * 0.5F;
                        float var8 = (float)(var6 * getThis().tileSizeBase) * 0.5F;
                        float var9 = ((float)var3 - var7) / (float)getThis().tileSizeBase * 2.0F;
                        float var10 = ((float)var4 - var8) / (float)getThis().tileSizeBase * 2.0F;
                        if (var9 < -1.0F) {
                            var9 += 2.0F;
                        }

                        if (var9 >= 1.0F) {
                            var9 -= 2.0F;
                        }

                        if (var10 < -1.0F) {
                            var10 += 2.0F;
                        }

                        if (var10 >= 1.0F) {
                            var10 -= 2.0F;
                        }

                        float var11 = var9 * var9 + var10 * var10;
                        float var12 = (float)Math.atan2((double)var10, (double)var9)
                                + ((float)var2 / 32.0F * (float) Math.PI * 2.0F - var11 * 10.0F + (float)(var6 * 2)) * (float)(var6 * 2 - 1);
                        var12 = (MathHelper.sin(var12) + 1.0F) / 2.0F;
                        var12 /= var11 + 1.0F;
                        var5 += var12 * 0.5F;
                    }

                    var5 += var1.nextFloat() * 0.1F;
                    int var141 = (int)(var5 * 100.0F + 155.0F);
                    int var13 = (int)(var5 * var5 * 200.0F + 55.0F);
                    int var14x = (int)(var5 * var5 * var5 * var5 * 255.0F);
                    int var15 = (int)(var5 * 100.0F + 155.0F);
                    int var16 = var4 * getThis().tileSizeBase + var3;
                    this.field_2170[var2][var16 * 4 + 0] = (byte)var13;
                    this.field_2170[var2][var16 * 4 + 1] = (byte)var14x;
                    this.field_2170[var2][var16 * 4 + 2] = (byte)var141;
                    this.field_2170[var2][var16 * 4 + 3] = (byte)var15;
                }
            }
        }
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public void method_1613() {
        ++this.field_2169;
        byte[] var1 = this.field_2170[this.field_2169 & 31];

        for(int var2 = 0; var2 < getThis().tileSizeSquare; ++var2) {
            int var3 = var1[var2 * 4 + 0] & 255;
            int var4 = var1[var2 * 4 + 1] & 255;
            int var5 = var1[var2 * 4 + 2] & 255;
            int var6 = var1[var2 * 4 + 3] & 255;
            if (getThis().field_2154) {
                int var7 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
                int var8 = (var3 * 30 + var4 * 70) / 100;
                int var9 = (var3 * 30 + var5 * 70) / 100;
                var3 = var7;
                var4 = var8;
                var5 = var9;
            }

            getThis().field_2152[var2 * 4 + 0] = (byte)var3;
            getThis().field_2152[var2 * 4 + 1] = (byte)var4;
            getThis().field_2152[var2 * 4 + 2] = (byte)var5;
            getThis().field_2152[var2 * 4 + 3] = (byte)var6;
        }
    }
}
