package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.IFMLTextureFXExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Item;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@ChangeSuperClass(FMLTextureFX.class)
@Mixin(TextureCompassFX.class)
public abstract class TextureCompassFXMixin extends TextureFX implements IFMLTextureFXExtension {
    @Shadow private int[] field_76867_h;

    @Shadow private Minecraft field_76865_g;

    public TextureCompassFXMixin(int icon) {
        super(icon);
    }

    @ShadowSuperConstructor
    public abstract void superConstructor(int p_i3213_1_);

    @ReplaceConstructor
    public void constructor(Minecraft p_i3212_1_) {
        superConstructor(Item.field_77750_aQ.func_77617_a(0));
        this.field_76865_g = p_i3212_1_;
        this.field_76847_f = 1;

        this.setup();
    }

    @Override
    public void setup() {
        this.superSetup();
        field_76867_h = new int[getTileSizeSquare()];
        try
        {
            BufferedImage var2 = ImageIO.read(field_76865_g.field_71418_C.func_77292_e().func_77532_a("/gui/items.png"));
            int var3 = this.field_76850_b % 16 * getTileSizeBase();
            int var4 = this.field_76850_b / 16 * getTileSizeBase();
            var2.getRGB(var3, var4, getTileSizeBase(), getTileSizeBase(), this.field_76867_h, 0, getTileSizeBase());
        } catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 256))
    private int fml$changeTileSizeSquare(int constant) {
        return getTileSizeSquare();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 4, ordinal = 3))
    private int fml$changeTileSizeBaseLoop1(int constant) {
        return getTileSizeBase() >> 2;
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = -4, ordinal = 0))
    private int fml$changeTileSizeBaseLoop2(int constant) {
        return -(getTileSizeBase() >> 2);
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(doubleValue = 8.5f))
    private double fml$changeTileSizeBaseFloat1(double constant) {
        return (getTileSizeBase() >> 1) + 0.5D;
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(doubleValue = 7.5f))
    private double fml$changeTileSizeBaseFloat2(double constant) {
        return (getTileSizeBase() >> 1) - 0.5D;
    }

    @ModifyConstant(method = "func_76846_a", constant = {
            @Constant(intValue = 16, ordinal = 1),
            @Constant(intValue = 16, ordinal = 2),
            @Constant(intValue = 16, ordinal = 3),
    })
    private int fml$changeTileSizeBase(int constant) {
        return getTileSizeBase();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = -8))
    private int fml$changeTileSizeBase2(int constant) {
        return getTileSizeBase() >> 2;
    }
}
