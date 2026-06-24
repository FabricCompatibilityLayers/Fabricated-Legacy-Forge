/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuper;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.IFMLTextureFXExtension;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TexturePortalFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Random;

@ChangeSuperClass(FMLTextureFX.class)
@Mixin(TexturePortalFX.class)
public abstract class TexturePortalFXMixin extends TextureFX implements IFMLTextureFXExtension {
    @Shadow private byte[][] field_76854_h;

    public TexturePortalFXMixin(int p_i3213_1_) {
        super(p_i3213_1_);
    }

    @ShadowSuperConstructor
    public abstract void superConstructor(int p_i3213_1_);

    @ReplaceConstructor
    public void constructor() {
        superConstructor(Block.field_72015_be.field_72059_bZ);

        this.setup();
    }

    @ShadowSuper("setup")
    public abstract void fmltexturefx$setup();

    @Override
    public void setup() {
        this.fmltexturefx$setup();
        field_76854_h = new byte[32][getTileSizeSquare() << 4];

        Random var1 = new Random(100L);

        for(int var2 = 0; var2 < 32; ++var2) {
            for(int var3 = 0; var3 < getTileSizeBase(); ++var3) {
                for(int var4 = 0; var4 < getTileSizeBase(); ++var4) {
                    float var5 = 0.0F;

                    for(int var6 = 0; var6 < 2; ++var6) {
                        float var7 = (float)(var6 * getTileSizeBase()) * 0.5F;
                        float var8 = (float)(var6 * getTileSizeBase()) * 0.5F;
                        float var9 = ((float)var3 - var7) / (float) getTileSizeBase() * 2.0F;
                        float var10 = ((float)var4 - var8) / (float) getTileSizeBase() * 2.0F;
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
                        float var12 = (float)Math.atan2((double)var10, (double)var9) + ((float)var2 / 32.0F * (float)Math.PI * 2.0F - var11 * 10.0F + (float)(var6 * 2)) * (float)(var6 * 2 - 1);
                        var12 = (MathHelper.func_76126_a(var12) + 1.0F) / 2.0F;
                        var12 /= var11 + 1.0F;
                        var5 += var12 * 0.5F;
                    }

                    var5 += var1.nextFloat() * 0.1F;
                    int var14 = (int)(var5 * 100.0F + 155.0F);
                    int var15 = (int)(var5 * var5 * 200.0F + 55.0F);
                    int var16 = (int)(var5 * var5 * var5 * var5 * 255.0F);
                    int var17 = (int)(var5 * 100.0F + 155.0F);
                    int var18 = var4 * getTileSizeBase() + var3;
                    this.field_76854_h[var2][var18 * 4 + 0] = (byte)var15;
                    this.field_76854_h[var2][var18 * 4 + 1] = (byte)var16;
                    this.field_76854_h[var2][var18 * 4 + 2] = (byte)var14;
                    this.field_76854_h[var2][var18 * 4 + 3] = (byte)var17;
                }
            }
        }
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 256))
    private int fml$tileSizeSquare(int constant) {
        return getTileSizeSquare();
    }
}
