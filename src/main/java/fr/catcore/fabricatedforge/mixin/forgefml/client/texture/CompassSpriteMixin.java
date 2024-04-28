package fr.catcore.fabricatedforge.mixin.forgefml.client.texture;

import cpw.mods.fml.client.FMLTextureFX;
import fr.catcore.cursedmixinextensions.annotations.ChangeSuperClass;
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
@ChangeSuperClass(FMLTextureFX.class)
public class CompassSpriteMixin implements IFMLTextureFX {
    @Shadow private int[] field_2149;
    @Shadow private Minecraft field_2148;
    @Shadow public static CompassSprite field_5222;

    private static int stileSizeBase = 16;
    private static int stileSizeSquare = 256;
    private static int stileSizeMask = 15;
    private static int stileSizeSquareMask = 255;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCtr(CallbackInfo ci) {
        this.setup();
    }

    @Override
    public void setup() {
        this.callFMLSetup();
        stileSizeBase = getThis().tileSizeBase;
        stileSizeSquare = getThis().tileSizeSquare;
        stileSizeMask = getThis().tileSizeMask;
        stileSizeSquareMask = getThis().tileSizeSquareMask;
        this.field_2149 = new int[getThis().tileSizeSquare];

        try {
            BufferedImage var2 = ImageIO.read(this.field_2148.texturePackManager.getCurrentTexturePack().openStream("/gui/items.png"));
            int var3 = getThis().field_2153 % 16 * getThis().tileSizeBase;
            int var4 = getThis().field_2153 / 16 * getThis().tileSizeBase;
            var2.getRGB(var3, var4, getThis().tileSizeBase, getThis().tileSizeBase, this.field_2149, 0, getThis().tileSizeBase);
        } catch (IOException var41) {
            var41.printStackTrace();
        }

        field_5222 = (CompassSprite)(Object) this;
    }

    /**
     * @author forge
     * @reason fmltexturefx
     */
    @Overwrite
    public static void method_4361(double par0, double par2, double par4, boolean par6, boolean par7) {
        int[] var8 = ((CompassSpriteMixin)(Object)field_5222).field_2149;
        byte[] var9 = field_5222.field_2152;

        for(int var10 = 0; var10 < stileSizeSquare; ++var10) {
            int var11 = var8[var10] >> 24 & 0xFF;
            int var12 = var8[var10] >> 16 & 0xFF;
            int var13 = var8[var10] >> 8 & 0xFF;
            int var14 = var8[var10] >> 0 & 0xFF;
            if (field_5222.field_2154) {
                int var15 = (var12 * 30 + var13 * 59 + var14 * 11) / 100;
                int var16 = (var12 * 30 + var13 * 70) / 100;
                int var17 = (var12 * 30 + var14 * 70) / 100;
                var12 = var15;
                var13 = var16;
                var14 = var17;
            }

            var9[var10 * 4 + 0] = (byte)var12;
            var9[var10 * 4 + 1] = (byte)var13;
            var9[var10 * 4 + 2] = (byte)var14;
            var9[var10 * 4 + 3] = (byte)var11;
        }

        double var27 = 0.0;
        if (((CompassSpriteMixin)(Object)field_5222).field_2148.world != null && !par6) {
            BlockPos var29 = ((CompassSpriteMixin)(Object)field_5222).field_2148.world.getWorldSpawnPos();
            double var28 = (double)var29.x - par0;
            double var32 = (double)var29.z - par2;
            var27 = (par4 - 90.0) * Math.PI / 180.0 - Math.atan2(var32, var28);
            if (!((CompassSpriteMixin)(Object)field_5222).field_2148.world.dimension.canPlayersSleep()) {
                var27 = Math.random() * Math.PI * 2.0;
            }
        }

        if (par7) {
            field_5222.field_2150 = var27;
        } else {
            double var30 = var27 - field_5222.field_2150;

            while(var30 < -Math.PI) {
                var30 += Math.PI * 2;
            }

            while(var30 >= Math.PI) {
                var30 -= Math.PI * 2;
            }

            if (var30 < -1.0) {
                var30 = -1.0;
            }

            if (var30 > 1.0) {
                var30 = 1.0;
            }

            field_5222.field_2151 += var30 * 0.1;
            field_5222.field_2151 *= 0.8;
            field_5222.field_2150 += field_5222.field_2151;
        }

        double var30 = Math.sin(field_5222.field_2150);
        double var31 = Math.cos(field_5222.field_2150);

        for(int var16 = -(stileSizeBase >> 2); var16 <= stileSizeBase >> 2; ++var16) {
            int var17 = (int)((double)(stileSizeBase >> 1) + 0.5 + var31 * (double)var16 * 0.3);
            int var18 = (int)((double)(stileSizeBase >> 1) - 0.5 - var30 * (double)var16 * 0.3 * 0.5);
            int var19 = var18 * stileSizeBase + var17;
            int var20 = 100;
            int var21 = 100;
            int var22 = 100;
            short var23 = 255;
            if (field_5222.field_2154) {
                int var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
                int var25 = (var20 * 30 + var21 * 70) / 100;
                int var26 = (var20 * 30 + var22 * 70) / 100;
                var20 = var24;
                var21 = var25;
                var22 = var26;
            }

            var9[var19 * 4 + 0] = (byte)var20;
            var9[var19 * 4 + 1] = (byte)var21;
            var9[var19 * 4 + 2] = (byte)var22;
            var9[var19 * 4 + 3] = (byte)var23;
        }

        for(int var30x = -(stileSizeBase >> 2); var30x <= stileSizeBase; ++var30x) {
            int var17 = (int)((double)(stileSizeBase >> 1) + 0.5 + var30 * (double)var30x * 0.3);
            int var18 = (int)((double)(stileSizeBase >> 1) - 0.5 + var31 * (double)var30x * 0.3 * 0.5);
            int var19 = var18 * stileSizeBase + var17;
            int var20 = var30x >= 0 ? 255 : 100;
            int var21 = var30x >= 0 ? 20 : 100;
            int var22 = var30x >= 0 ? 20 : 100;
            short var23 = 255;
            if (field_5222.field_2154) {
                int var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
                int var25 = (var20 * 30 + var21 * 70) / 100;
                int var26 = (var20 * 30 + var22 * 70) / 100;
                var20 = var24;
                var21 = var25;
                var22 = var26;
            }

            var9[var19 * 4 + 0] = (byte)var20;
            var9[var19 * 4 + 1] = (byte)var21;
            var9[var19 * 4 + 2] = (byte)var22;
            var9[var19 * 4 + 3] = (byte)var23;
        }
    }
}
