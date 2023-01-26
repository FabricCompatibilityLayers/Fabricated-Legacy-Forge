package fr.catcore.fabricatedforge.mixin.forgefml.client.texture;

import fr.catcore.fabricatedforge.mixininterface.IFMLTextureFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.CompassSprite;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Mixin(CompassSprite.class)
public class CompassSpriteMixin implements IFMLTextureFX {
    @Shadow private int[] field_2149;

    @Shadow private Minecraft field_2148;

    @Shadow private double field_2150;

    @Shadow private double field_2151;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        this.field_2149 = new int[getThis().tileSizeSquare];

        try {
            BufferedImage var2 = ImageIO.read(this.field_2148.texturePackManager.getCurrentTexturePack().openStream("/gui/items.png"));
            int var3 = getThis().field_2153 % 16 * getThis().tileSizeBase;
            int var4 = getThis().field_2153 / 16 * getThis().tileSizeBase;
            var2.getRGB(var3, var4, getThis().tileSizeBase, getThis().tileSizeBase, this.field_2149, 0, getThis().tileSizeBase);
        } catch (IOException var41) {
            var41.printStackTrace();
        }
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public void method_1613() {
        for(int var1 = 0; var1 < getThis().tileSizeSquare; ++var1) {
            int var2 = this.field_2149[var1] >> 24 & 0xFF;
            int var3 = this.field_2149[var1] >> 16 & 0xFF;
            int var4 = this.field_2149[var1] >> 8 & 0xFF;
            int var5 = this.field_2149[var1] >> 0 & 0xFF;
            if (getThis().field_2154) {
                int var6 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
                int var7 = (var3 * 30 + var4 * 70) / 100;
                int var8 = (var3 * 30 + var5 * 70) / 100;
                var3 = var6;
                var4 = var7;
                var5 = var8;
            }

            getThis().field_2152[var1 * 4 + 0] = (byte)var3;
            getThis().field_2152[var1 * 4 + 1] = (byte)var4;
            getThis().field_2152[var1 * 4 + 2] = (byte)var5;
            getThis().field_2152[var1 * 4 + 3] = (byte)var2;
        }

        double var20 = 0.0;
        if (this.field_2148.world != null && this.field_2148.playerEntity != null) {
            BlockPos var21 = this.field_2148.world.getWorldSpawnPos();
            double var23 = (double)var21.x - this.field_2148.playerEntity.x;
            double var25 = (double)var21.z - this.field_2148.playerEntity.z;
            var20 = (double)(this.field_2148.playerEntity.yaw - 90.0F) * Math.PI / 180.0 - Math.atan2(var25, var23);
            if (!this.field_2148.world.dimension.canPlayersSleep()) {
                var20 = Math.random() * Math.PI * 2.0;
            }
        }

        double var22 = var20 - this.field_2150;

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

        this.field_2151 += var22 * 0.1;
        this.field_2151 *= 0.8;
        this.field_2150 += this.field_2151;
        double var24 = Math.sin(this.field_2150);
        double var26 = Math.cos(this.field_2150);

        for(int var9 = -(getThis().tileSizeBase >> 2); var9 <= getThis().tileSizeBase >> 2; ++var9) {
            int var10 = (int)((double)(getThis().tileSizeBase >> 1) + 0.5 + var26 * (double)var9 * 0.3);
            int var11 = (int)((double)(getThis().tileSizeBase >> 1) - 0.5 - var24 * (double)var9 * 0.3 * 0.5);
            int var12 = var11 * getThis().tileSizeBase + var10;
            int var13 = 100;
            int var14 = 100;
            int var15 = 100;
            short var16 = 255;
            if (getThis().field_2154) {
                int var17 = (var13 * 30 + var14 * 59 + var15 * 11) / 100;
                int var18 = (var13 * 30 + var14 * 70) / 100;
                int var19 = (var13 * 30 + var15 * 70) / 100;
                var13 = var17;
                var14 = var18;
                var15 = var19;
            }

            getThis().field_2152[var12 * 4 + 0] = (byte)var13;
            getThis().field_2152[var12 * 4 + 1] = (byte)var14;
            getThis().field_2152[var12 * 4 + 2] = (byte)var15;
            getThis().field_2152[var12 * 4 + 3] = (byte)var16;
        }

        for(int var27 = -(getThis().tileSizeBase >> 2); var27 <= getThis().tileSizeBase; ++var27) {
            int var10 = (int)((double)(getThis().tileSizeBase >> 1) + 0.5 + var24 * (double)var27 * 0.3);
            int var11 = (int)((double)(getThis().tileSizeBase >> 1) - 0.5 + var26 * (double)var27 * 0.3 * 0.5);
            int var12 = var11 * getThis().tileSizeBase + var10;
            int var13 = var27 >= 0 ? 255 : 100;
            int var14 = var27 >= 0 ? 20 : 100;
            int var15 = var27 >= 0 ? 20 : 100;
            short var16 = 255;
            if (getThis().field_2154) {
                int var17 = (var13 * 30 + var14 * 59 + var15 * 11) / 100;
                int var18 = (var13 * 30 + var14 * 70) / 100;
                int var19 = (var13 * 30 + var15 * 70) / 100;
                var13 = var17;
                var14 = var18;
                var15 = var19;
            }

            getThis().field_2152[var12 * 4 + 0] = (byte)var13;
            getThis().field_2152[var12 * 4 + 1] = (byte)var14;
            getThis().field_2152[var12 * 4 + 2] = (byte)var15;
            getThis().field_2152[var12 * 4 + 3] = (byte)var16;
        }
    }
}
