package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.IFMLTextureFXExtension;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureLavaFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ChangeSuperClass(FMLTextureFX.class)
@Mixin(TextureLavaFX.class)
public abstract class TextureLavaFXMixin extends TextureFX implements IFMLTextureFXExtension {
    @Shadow protected float[] field_76876_g;

    @Shadow protected float[] field_76878_h;

    @Shadow protected float[] field_76879_i;

    @Shadow protected float[] field_76877_j;

    public TextureLavaFXMixin(int p_i3213_1_) {
        super(p_i3213_1_);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fml$callSetup(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup()
    {
        this.superSetup();
        field_76876_g = new float[getTileSizeSquare()];
        field_76878_h = new float[getTileSizeSquare()];
        field_76879_i = new float[getTileSizeSquare()];
        field_76877_j = new float[getTileSizeSquare()];
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 16))
    private int fml$tileSizeBase(int constant) {
        return getTileSizeBase();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 15))
    private int fml$tileSizeMask(int constant) {
        return getTileSizeMask();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 256))
    private int fml$tileSizeSquare(int constant) {
        return getTileSizeSquare();
    }
}
