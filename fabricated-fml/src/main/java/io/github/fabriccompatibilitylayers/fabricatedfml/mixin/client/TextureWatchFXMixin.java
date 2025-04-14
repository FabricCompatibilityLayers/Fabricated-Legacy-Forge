package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.IFMLTextureFXExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureWatchFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

@ChangeSuperClass(FMLTextureFX.class)
@Mixin(TextureWatchFX.class)
public abstract class TextureWatchFXMixin extends TextureFX implements IFMLTextureFXExtension {
    @Shadow private int[] field_76863_h;

    @Shadow private int[] field_76864_i;

    @Shadow private Minecraft field_76860_g;

    public TextureWatchFXMixin(int p_i3213_1_) {
        super(p_i3213_1_);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljavax/imageio/ImageIO;read(Ljava/net/URL;)Ljava/awt/image/BufferedImage;", remap = false), cancellable = true)
    private void fml$cancelOriginalConstructor(Minecraft par1, CallbackInfo ci) {
        this.setup();
        ci.cancel();
    }

    @Override
    public void setup() {
        this.superSetup();
        field_76863_h = new int[getTileSizeSquare()];
        field_76864_i = new int[getTileSizeSquare()];

        try {
            BufferedImage var2 = ImageIO.read(field_76860_g.field_71418_C.func_77292_e().func_77532_a("/gui/items.png"));
            int var3 = this.field_76850_b % 16 * getTileSizeBase();
            int var4 = this.field_76850_b / 16 * getTileSizeBase();
            var2.getRGB(var3, var4, getTileSizeBase(), getTileSizeBase(), this.field_76863_h, 0, getTileSizeBase());
            var2 = ImageIO.read(field_76860_g.field_71418_C.func_77292_e().func_77532_a("/misc/dial.png"));

            if (var2.getWidth() != getTileSizeBase())
            {
                BufferedImage tmp = new BufferedImage(getTileSizeBase(), getTileSizeBase(), 6);
                Graphics2D gfx = tmp.createGraphics();
                gfx.drawImage(var2, 0, 0, getTileSizeBase(), getTileSizeBase(), 0, 0, var2.getWidth(), var2.getHeight(), null);
                gfx.dispose();
                var2 = tmp;
            }

            var2.getRGB(0, 0, getTileSizeBase(), getTileSizeBase(), this.field_76864_i, 0, getTileSizeBase());
        } catch (Exception var5) {
            getLogger().log(Level.WARNING, String.format("A problem occurred with the watch texture: animation will be disabled"), var5);
            setErrored(true);
        }
    }

    @ModifyConstant(method = "func_76846_a", constant = {
            @Constant(intValue = 16, ordinal = 1),
            @Constant(intValue = 16, ordinal = 2),
            @Constant(intValue = 16, ordinal = 3)
    })
    private int fml$tileSizeBase(int constant) {
        return getTileSizeBase();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(doubleValue = 16.0f))
    private double fml$tileSizeBase(double constant) {
        return getTileSizeBase();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 15))
    private int fml$tileSizeMask(int constant) {
        return getTileSizeMask();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(doubleValue = 15.0f))
    private double fml$tileSizeMask(double constant) {
        return getTileSizeMask();
    }

    @ModifyConstant(method = "func_76846_a", constant = @Constant(intValue = 256))
    private int fml$tileSizeSquare(int constant) {
        return getTileSizeSquare();
    }
}
