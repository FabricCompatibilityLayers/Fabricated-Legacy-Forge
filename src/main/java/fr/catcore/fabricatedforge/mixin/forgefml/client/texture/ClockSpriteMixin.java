package fr.catcore.fabricatedforge.mixin.forgefml.client.texture;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
import fr.catcore.fabricatedforge.mixininterface.IFMLTextureFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.ClockSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.logging.Level;

@Mixin(ClockSprite.class)
@ChangeSuperClass(FMLTextureFX.class)
public class ClockSpriteMixin implements IFMLTextureFX {
    @Shadow private int[] field_2144;

    @Shadow private int[] field_2145;

    @Shadow private Minecraft field_2143;

    @Shadow private double field_2146;

    @Shadow private double field_2147;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        this.field_2144 = new int[getThis().tileSizeSquare];
        this.field_2145 = new int[getThis().tileSizeSquare];

        try {
            BufferedImage var2 = ImageIO.read(this.field_2143.texturePackManager.getCurrentTexturePack().openStream("/gui/items.png"));
            int var3 = getThis().field_2153 % 16 * getThis().tileSizeBase;
            int var4 = getThis().field_2153 / 16 * getThis().tileSizeBase;
            var2.getRGB(var3, var4, getThis().tileSizeBase, getThis().tileSizeBase, this.field_2144, 0, getThis().tileSizeBase);
            var2 = ImageIO.read(this.field_2143.texturePackManager.getCurrentTexturePack().openStream("/misc/dial.png"));
            if (var2.getWidth() != getThis().tileSizeBase) {
                BufferedImage tmp = new BufferedImage(getThis().tileSizeBase, getThis().tileSizeBase, 6);
                Graphics2D gfx = tmp.createGraphics();
                gfx.drawImage(var2, 0, 0, getThis().tileSizeBase, getThis().tileSizeBase, 0, 0, var2.getWidth(), var2.getHeight(), (ImageObserver)null);
                gfx.dispose();
                var2 = tmp;
            }

            var2.getRGB(0, 0, getThis().tileSizeBase, getThis().tileSizeBase, this.field_2145, 0, getThis().tileSizeBase);
        } catch (Exception var6) {
            var6.printStackTrace();
            getThis().log.log(Level.WARNING, String.format("A problem occurred with the watch texture: animation will be disabled"), var6);
            getThis().setErrored(true);
        }
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public void method_1613() {
        double var1 = 0.0;
        if (this.field_2143.world != null && this.field_2143.playerEntity != null) {
            float var3 = this.field_2143.world.getSkyAngle(1.0F);
            var1 = (double)(-var3 * (float) Math.PI * 2.0F);
            if (!this.field_2143.world.dimension.canPlayersSleep()) {
                var1 = Math.random() * Math.PI * 2.0;
            }
        }

        double var22 = var1 - this.field_2146;

        while(var22 < -Math.PI) {
            var22 += Math.PI * 2;
        }

        while(var22 >= Math.PI) {
            var22 -= Math.PI * 2;
        }

        if (var22 < -1.0) {
            var22 = -1.0;
        }

        if (var22 > 1.0) {
            var22 = 1.0;
        }

        this.field_2147 += var22 * 0.1;
        this.field_2147 *= 0.8;
        this.field_2146 += this.field_2147;
        double var5 = Math.sin(this.field_2146);
        double var7 = Math.cos(this.field_2146);

        for(int var9 = 0; var9 < getThis().tileSizeSquare; ++var9) {
            int var10 = this.field_2144[var9] >> 24 & 0xFF;
            int var11 = this.field_2144[var9] >> 16 & 0xFF;
            int var12 = this.field_2144[var9] >> 8 & 0xFF;
            int var13 = this.field_2144[var9] >> 0 & 0xFF;
            if (var11 == var13 && var12 == 0 && var13 > 0) {
                double var14 = -((double)(var9 % getThis().tileSizeBase) / (double)getThis().tileSizeMask - 0.5);
                double var16 = (double)(var9 / getThis().tileSizeBase) / (double)getThis().tileSizeMask - 0.5;
                int var18 = var11;
                int var19 = (int)((var14 * var7 + var16 * var5 + 0.5) * (double)getThis().tileSizeBase);
                int var20 = (int)((var16 * var7 - var14 * var5 + 0.5) * (double)getThis().tileSizeBase);
                int var21 = (var19 & getThis().tileSizeMask) + (var20 & getThis().tileSizeMask) * getThis().tileSizeBase;
                var10 = this.field_2145[var21] >> 24 & 0xFF;
                var11 = (this.field_2145[var21] >> 16 & 0xFF) * var11 / 255;
                var12 = (this.field_2145[var21] >> 8 & 0xFF) * var18 / 255;
                var13 = (this.field_2145[var21] >> 0 & 0xFF) * var18 / 255;
            }

            if (getThis().field_2154) {
                int var23 = (var11 * 30 + var12 * 59 + var13 * 11) / 100;
                int var15 = (var11 * 30 + var12 * 70) / 100;
                int var24 = (var11 * 30 + var13 * 70) / 100;
                var11 = var23;
                var12 = var15;
                var13 = var24;
            }

            getThis().field_2152[var9 * 4 + 0] = (byte)var11;
            getThis().field_2152[var9 * 4 + 1] = (byte)var12;
            getThis().field_2152[var9 * 4 + 2] = (byte)var13;
            getThis().field_2152[var9 * 4 + 3] = (byte)var10;
        }
    }
}
