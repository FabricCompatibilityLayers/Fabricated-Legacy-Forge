package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.IFMLTextureFXExtension;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureFlamesFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ChangeSuperClass(FMLTextureFX.class)
@Mixin(TextureFlamesFX.class)
public abstract class TextureFlamesFXMixin extends TextureFX implements IFMLTextureFXExtension {
    @Shadow protected float[] field_76869_g;

    @Shadow protected float[] field_76870_h;

    public TextureFlamesFXMixin(int p_i3213_1_) {
        super(p_i3213_1_);
    }

    private int fireTileSize = 20;
    private int fireGridSize = 320;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fml$callSetup(int par1, CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup()
    {
        this.superSetup();
        fireTileSize = getTileSizeBase() + (getTileSizeBase() >> 2);
        fireGridSize = fireTileSize * getTileSizeBase();
        field_76869_g = new float[fireGridSize];
        field_76870_h = new float[fireGridSize];
    }

    @Inject(method = "func_76846_a", at = @At("HEAD"))
    private void fml$setupSharedVariables(CallbackInfo ci,
                                          @Share(value = "fireFactor1", namespace = "fml") LocalFloatRef factor1Ref,
                                          @Share(value = "fireFactor2", namespace = "fml") LocalFloatRef factor2Ref
                                          ) {
        factor1Ref.set(3.0F + (float)(getTileSizeBase() >> 4));

        factor2Ref.set(1.01F + (0.8F / getTileSizeBase()));
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 16))
    private int fml$tileSizeBase(int constant) {
        return getTileSizeBase();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 20))
    private int fml$fireTileSize(int constant) {
        return fireTileSize;
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 18))
    private int fml$compute(int constant) {
        return fireTileSize - (getTileSizeBase() >> 3);
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(floatValue = 1.0600001F))
    private float fml$fireFactor2(float constant, @Share(value = "fireFactor2", namespace = "fml") LocalFloatRef factor2Ref) {
        return factor2Ref.get();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 19))
    private int fml$compute2(int constant) {
        return fireTileSize - (getTileSizeBase() >> 4);
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(doubleValue = 4.0F))
    private double fml$fireFactor1(double constant, @Share(value = "fireFactor1", namespace = "fml") LocalFloatRef factor1Ref) {
        return factor1Ref.get();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 256))
    private int fml$tileSizeSquare(int constant) {
        return getTileSizeSquare();
    }
}
