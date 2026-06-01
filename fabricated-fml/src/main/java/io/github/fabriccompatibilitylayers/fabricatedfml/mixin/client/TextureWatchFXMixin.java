/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.IFMLTextureFXExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Item;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureWatchFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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

    @ShadowSuperConstructor
    public abstract void superConstructor(int p_i3213_1_);

    @ReplaceConstructor
    public void constructor(Minecraft p_i3214_1_) {
        superConstructor(Item.field_77752_aS.func_77617_a(0));
        this.field_76860_g = p_i3214_1_;
        this.field_76847_f = 1;

        this.setup();
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
