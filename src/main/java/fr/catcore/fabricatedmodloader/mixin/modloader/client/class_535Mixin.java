package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import com.google.common.collect.Lists;
import fr.catcore.fabricatedmodloader.utils.class_535Data;
import modloader.ModLoader;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.client.class_523;
import net.minecraft.client.class_535;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.WorldView;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(class_535.class)
public abstract class class_535Mixin {

    @Shadow
    public WorldView field_2017;

    @Shadow
    public boolean field_2048;

    @Shadow
    public abstract void method_1446(Block block, int i, double d, double e, double f, double g);

    @Shadow
    public abstract void method_1444(Block block, double d, double e, double f, int i);

    @Shadow
    public abstract void method_1456(Block block, double d, double e, double f, int i);

    @Shadow
    public abstract void method_1461(Block block, double d, double e, double f, int i);

    @Shadow
    public abstract void method_1465(Block block, double d, double e, double f, int i);

    @Shadow
    public abstract void method_1468(Block block, double d, double e, double f, int i);

    @Shadow
    public abstract void method_1470(Block block, double d, double e, double f, int i);

    @Shadow
    public abstract void method_1457(Block block, int i, double d, double e, double f);

    @Shadow
    public abstract void method_1443(Block block, double d, double e, double f, double g, double h);

    @Shadow
    public abstract void method_4320(Block block);

    @Shadow
    public abstract void method_4311(double d, double e, double f, double g, double h, double i);

    @Shadow
    public abstract void method_1431();

    @Shadow
    public abstract void method_4312(int i);

    @Shadow
    public abstract boolean method_4316(AnvilBlock anvilBlock, int i, int j, int k, int l, boolean bl);

    @Shadow
    public double field_5187;

    @Shadow
    public abstract void method_1445(Block block, int i, double d, double e, double f, float g);

    @Shadow
    public static boolean field_2047;
    @Unique
    private static final List<Integer> RENDERER_TYPES = Lists.newArrayList(
            0, 31, 4, 13, 1, 19, 23, 6, 2, 3, 5,
            8, 7, 9, 10, 27, 11, 32, 12, 29, 30,
            14, 15, 16, 17, 18, 20, 21, 24, 33,
            35, 25, 26, 28, 34
    );

    @Inject(method = "method_1458", at = @At("RETURN"), cancellable = true)
    private void modLoaderRenderWorldBlock(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int type = block.getBlockType();
        if (!cir.getReturnValue() && !RENDERER_TYPES.contains(type)) {
            cir.setReturnValue(ModLoader.renderWorldBlock((class_535) (Object) this, this.field_2017, i, j, k, block, type));
        }
    }

    /**
     * @author
     */
    @Overwrite
    public void method_1447(Block block, int i, float f) {
        Tessellator var4 = Tessellator.INSTANCE;
        boolean var5 = block.id == Block.GRASS_BLOCK.id;
        int var6;
        float var7;
        float var8;
        float var9;
        if (this.field_2048) {
            var6 = block.method_459(i);
            if (var5) {
                var6 = 16777215;
            }

            var7 = (float)(var6 >> 16 & 255) / 255.0F;
            var8 = (float)(var6 >> 8 & 255) / 255.0F;
            var9 = (float)(var6 & 255) / 255.0F;
            GL11.glColor4f(var7 * f, var8 * f, var9 * f, 1.0F);
        }

        var6 = block.getBlockType();
        this.method_4320(block);
        int var14;
        if (var6 != 0 && var6 != 31 && var6 != 16 && var6 != 26) {
            if (var6 == 1) {
                var4.method_1405();
                var4.method_1407(0.0F, -1.0F, 0.0F);
                this.method_1445(block, i, -0.5, -0.5, -0.5, 1.0F);
                var4.method_1396();
            } else if (var6 == 19) {
                var4.method_1405();
                var4.method_1407(0.0F, -1.0F, 0.0F);
                block.setBlockItemBounds();
                this.method_1446(block, i, this.field_5187, -0.5, -0.5, -0.5);
                var4.method_1396();
            } else if (var6 == 23) {
                var4.method_1405();
                var4.method_1407(0.0F, -1.0F, 0.0F);
                block.setBlockItemBounds();
                var4.method_1396();
            } else if (var6 == 13) {
                block.setBlockItemBounds();
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                var7 = 0.0625F;
                var4.method_1405();
                var4.method_1407(0.0F, -1.0F, 0.0F);
                this.method_1444(block, 0.0, 0.0, 0.0, block.method_395(0));
                var4.method_1396();
                var4.method_1405();
                var4.method_1407(0.0F, 1.0F, 0.0F);
                this.method_1456(block, 0.0, 0.0, 0.0, block.method_395(1));
                var4.method_1396();
                var4.method_1405();
                var4.method_1407(0.0F, 0.0F, -1.0F);
                var4.method_1410(0.0F, 0.0F, var7);
                this.method_1461(block, 0.0, 0.0, 0.0, block.method_395(2));
                var4.method_1410(0.0F, 0.0F, -var7);
                var4.method_1396();
                var4.method_1405();
                var4.method_1407(0.0F, 0.0F, 1.0F);
                var4.method_1410(0.0F, 0.0F, -var7);
                this.method_1465(block, 0.0, 0.0, 0.0, block.method_395(3));
                var4.method_1410(0.0F, 0.0F, var7);
                var4.method_1396();
                var4.method_1405();
                var4.method_1407(-1.0F, 0.0F, 0.0F);
                var4.method_1410(var7, 0.0F, 0.0F);
                this.method_1468(block, 0.0, 0.0, 0.0, block.method_395(4));
                var4.method_1410(-var7, 0.0F, 0.0F);
                var4.method_1396();
                var4.method_1405();
                var4.method_1407(1.0F, 0.0F, 0.0F);
                var4.method_1410(-var7, 0.0F, 0.0F);
                this.method_1470(block, 0.0, 0.0, 0.0, block.method_395(5));
                var4.method_1410(var7, 0.0F, 0.0F);
                var4.method_1396();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            } else if (var6 == 22) {
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                class_523.field_1809.method_1320(block, i, f);
                GL11.glEnable(32826);
            } else if (var6 == 6) {
                var4.method_1405();
                var4.method_1407(0.0F, -1.0F, 0.0F);
                this.method_1457(block, i, -0.5, -0.5, -0.5);
                var4.method_1396();
            } else if (var6 == 2) {
                var4.method_1405();
                var4.method_1407(0.0F, -1.0F, 0.0F);
                this.method_1443(block, -0.5, -0.5, -0.5, 0.0, 0.0);
                var4.method_1396();
            } else if (var6 == 10) {
                for(var14 = 0; var14 < 2; ++var14) {
                    if (var14 == 0) {
                        this.method_4311(0.0, 0.0, 0.0, 1.0, 1.0, 0.5);
                    }

                    if (var14 == 1) {
                        this.method_4311(0.0, 0.0, 0.5, 1.0, 0.5, 1.0);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    var4.method_1405();
                    var4.method_1407(0.0F, -1.0F, 0.0F);
                    this.method_1444(block, 0.0, 0.0, 0.0, block.method_395(0));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 1.0F, 0.0F);
                    this.method_1456(block, 0.0, 0.0, 0.0, block.method_395(1));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, -1.0F);
                    this.method_1461(block, 0.0, 0.0, 0.0, block.method_395(2));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, 1.0F);
                    this.method_1465(block, 0.0, 0.0, 0.0, block.method_395(3));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(-1.0F, 0.0F, 0.0F);
                    this.method_1468(block, 0.0, 0.0, 0.0, block.method_395(4));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(1.0F, 0.0F, 0.0F);
                    this.method_1470(block, 0.0, 0.0, 0.0, block.method_395(5));
                    var4.method_1396();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }
            } else if (var6 == 27) {
                var14 = 0;
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                var4.method_1405();

                for(int var15 = 0; var15 < 8; ++var15) {
                    byte var16 = 0;
                    byte var17 = 1;
                    if (var15 == 0) {
                        var16 = 2;
                    }

                    if (var15 == 1) {
                        var16 = 3;
                    }

                    if (var15 == 2) {
                        var16 = 4;
                    }

                    if (var15 == 3) {
                        var16 = 5;
                        var17 = 2;
                    }

                    if (var15 == 4) {
                        var16 = 6;
                        var17 = 3;
                    }

                    if (var15 == 5) {
                        var16 = 7;
                        var17 = 5;
                    }

                    if (var15 == 6) {
                        var16 = 6;
                        var17 = 2;
                    }

                    if (var15 == 7) {
                        var16 = 3;
                    }

                    float var11 = (float)var16 / 16.0F;
                    float var12 = 1.0F - (float)var14 / 16.0F;
                    float var13 = 1.0F - (float)(var14 + var17) / 16.0F;
                    var14 += var17;
                    this.method_4311((double)(0.5F - var11), (double)var13, (double)(0.5F - var11), (double)(0.5F + var11), (double)var12, (double)(0.5F + var11));
                    var4.method_1407(0.0F, -1.0F, 0.0F);
                    this.method_1444(block, 0.0, 0.0, 0.0, block.method_395(0));
                    var4.method_1407(0.0F, 1.0F, 0.0F);
                    this.method_1456(block, 0.0, 0.0, 0.0, block.method_395(1));
                    var4.method_1407(0.0F, 0.0F, -1.0F);
                    this.method_1461(block, 0.0, 0.0, 0.0, block.method_395(2));
                    var4.method_1407(0.0F, 0.0F, 1.0F);
                    this.method_1465(block, 0.0, 0.0, 0.0, block.method_395(3));
                    var4.method_1407(-1.0F, 0.0F, 0.0F);
                    this.method_1468(block, 0.0, 0.0, 0.0, block.method_395(4));
                    var4.method_1407(1.0F, 0.0F, 0.0F);
                    this.method_1470(block, 0.0, 0.0, 0.0, block.method_395(5));
                }

                var4.method_1396();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                this.method_4311(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            } else if (var6 == 11) {
                for(var14 = 0; var14 < 4; ++var14) {
                    var8 = 0.125F;
                    if (var14 == 0) {
                        this.method_4311((double)(0.5F - var8), 0.0, 0.0, (double)(0.5F + var8), 1.0, (double)(var8 * 2.0F));
                    }

                    if (var14 == 1) {
                        this.method_4311((double)(0.5F - var8), 0.0, (double)(1.0F - var8 * 2.0F), (double)(0.5F + var8), 1.0, 1.0);
                    }

                    var8 = 0.0625F;
                    if (var14 == 2) {
                        this.method_4311((double)(0.5F - var8), (double)(1.0F - var8 * 3.0F), (double)(-var8 * 2.0F), (double)(0.5F + var8), (double)(1.0F - var8), (double)(1.0F + var8 * 2.0F));
                    }

                    if (var14 == 3) {
                        this.method_4311((double)(0.5F - var8), (double)(0.5F - var8 * 3.0F), (double)(-var8 * 2.0F), (double)(0.5F + var8), (double)(0.5F - var8), (double)(1.0F + var8 * 2.0F));
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    var4.method_1405();
                    var4.method_1407(0.0F, -1.0F, 0.0F);
                    this.method_1444(block, 0.0, 0.0, 0.0, block.method_395(0));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 1.0F, 0.0F);
                    this.method_1456(block, 0.0, 0.0, 0.0, block.method_395(1));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, -1.0F);
                    this.method_1461(block, 0.0, 0.0, 0.0, block.method_395(2));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, 1.0F);
                    this.method_1465(block, 0.0, 0.0, 0.0, block.method_395(3));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(-1.0F, 0.0F, 0.0F);
                    this.method_1468(block, 0.0, 0.0, 0.0, block.method_395(4));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(1.0F, 0.0F, 0.0F);
                    this.method_1470(block, 0.0, 0.0, 0.0, block.method_395(5));
                    var4.method_1396();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }

                this.method_4311(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            } else if (var6 == 21) {
                for(var14 = 0; var14 < 3; ++var14) {
                    var8 = 0.0625F;
                    if (var14 == 0) {
                        this.method_4311((double)(0.5F - var8), 0.30000001192092896, 0.0, (double)(0.5F + var8), 1.0, (double)(var8 * 2.0F));
                    }

                    if (var14 == 1) {
                        this.method_4311((double)(0.5F - var8), 0.30000001192092896, (double)(1.0F - var8 * 2.0F), (double)(0.5F + var8), 1.0, 1.0);
                    }

                    var8 = 0.0625F;
                    if (var14 == 2) {
                        this.method_4311((double)(0.5F - var8), 0.5, 0.0, (double)(0.5F + var8), (double)(1.0F - var8), 1.0);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    var4.method_1405();
                    var4.method_1407(0.0F, -1.0F, 0.0F);
                    this.method_1444(block, 0.0, 0.0, 0.0, block.method_395(0));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 1.0F, 0.0F);
                    this.method_1456(block, 0.0, 0.0, 0.0, block.method_395(1));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, -1.0F);
                    this.method_1461(block, 0.0, 0.0, 0.0, block.method_395(2));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, 1.0F);
                    this.method_1465(block, 0.0, 0.0, 0.0, block.method_395(3));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(-1.0F, 0.0F, 0.0F);
                    this.method_1468(block, 0.0, 0.0, 0.0, block.method_395(4));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(1.0F, 0.0F, 0.0F);
                    this.method_1470(block, 0.0, 0.0, 0.0, block.method_395(5));
                    var4.method_1396();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }
            } else if (var6 == 32) {
                for(var14 = 0; var14 < 2; ++var14) {
                    if (var14 == 0) {
                        this.method_4311(0.0, 0.0, 0.3125, 1.0, 0.8125, 0.6875);
                    }

                    if (var14 == 1) {
                        this.method_4311(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    var4.method_1405();
                    var4.method_1407(0.0F, -1.0F, 0.0F);
                    this.method_1444(block, 0.0, 0.0, 0.0, block.method_396(0, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 1.0F, 0.0F);
                    this.method_1456(block, 0.0, 0.0, 0.0, block.method_396(1, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, -1.0F);
                    this.method_1461(block, 0.0, 0.0, 0.0, block.method_396(2, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, 1.0F);
                    this.method_1465(block, 0.0, 0.0, 0.0, block.method_396(3, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(-1.0F, 0.0F, 0.0F);
                    this.method_1468(block, 0.0, 0.0, 0.0, block.method_396(4, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(1.0F, 0.0F, 0.0F);
                    this.method_1470(block, 0.0, 0.0, 0.0, block.method_396(5, i));
                    var4.method_1396();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }

                this.method_4311(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            } else if (var6 == 35) {
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                this.method_4316((AnvilBlock)block, 0, 0, 0, i, true);
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            } else if (var6 == 34) {
                for(var14 = 0; var14 < 3; ++var14) {
                    if (var14 == 0) {
                        this.method_4311(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
                        this.method_4312(Block.OBSIDIAN.field_439);
                    } else if (var14 == 1) {
                        this.method_4311(0.1875, 0.1875, 0.1875, 0.8125, 0.875, 0.8125);
                        this.method_4312(41);
                    } else if (var14 == 2) {
                        this.method_4311(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                        this.method_4312(Block.GLASS_BLOCK.field_439);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    var4.method_1405();
                    var4.method_1407(0.0F, -1.0F, 0.0F);
                    this.method_1444(block, 0.0, 0.0, 0.0, block.method_396(0, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 1.0F, 0.0F);
                    this.method_1456(block, 0.0, 0.0, 0.0, block.method_396(1, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, -1.0F);
                    this.method_1461(block, 0.0, 0.0, 0.0, block.method_396(2, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(0.0F, 0.0F, 1.0F);
                    this.method_1465(block, 0.0, 0.0, 0.0, block.method_396(3, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(-1.0F, 0.0F, 0.0F);
                    this.method_1468(block, 0.0, 0.0, 0.0, block.method_396(4, i));
                    var4.method_1396();
                    var4.method_1405();
                    var4.method_1407(1.0F, 0.0F, 0.0F);
                    this.method_1470(block, 0.0, 0.0, 0.0, block.method_396(5, i));
                    var4.method_1396();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }

                this.method_4311(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                this.method_1431();
            } else {
                ModLoader.renderInvBlock((class_535) (Object) this, block, i, var6);
            }
        } else {
            if (var6 == 16) {
                i = 1;
            }

            block.setBlockItemBounds();
            this.method_4320(block);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            var4.method_1405();
            var4.method_1407(0.0F, -1.0F, 0.0F);
            this.method_1444(block, 0.0, 0.0, 0.0, block.method_396(0, i));
            var4.method_1396();
            if (var5 && this.field_2048) {
                var14 = block.method_459(i);
                var8 = (float)(var14 >> 16 & 255) / 255.0F;
                var9 = (float)(var14 >> 8 & 255) / 255.0F;
                float var10 = (float)(var14 & 255) / 255.0F;
                GL11.glColor4f(var8 * f, var9 * f, var10 * f, 1.0F);
            }

            var4.method_1405();
            var4.method_1407(0.0F, 1.0F, 0.0F);
            this.method_1456(block, 0.0, 0.0, 0.0, block.method_396(1, i));
            var4.method_1396();
            if (var5 && this.field_2048) {
                GL11.glColor4f(f, f, f, 1.0F);
            }

            var4.method_1405();
            var4.method_1407(0.0F, 0.0F, -1.0F);
            this.method_1461(block, 0.0, 0.0, 0.0, block.method_396(2, i));
            var4.method_1396();
            var4.method_1405();
            var4.method_1407(0.0F, 0.0F, 1.0F);
            this.method_1465(block, 0.0, 0.0, 0.0, block.method_396(3, i));
            var4.method_1396();
            var4.method_1405();
            var4.method_1407(-1.0F, 0.0F, 0.0F);
            this.method_1468(block, 0.0, 0.0, 0.0, block.method_396(4, i));
            var4.method_1396();
            var4.method_1405();
            var4.method_1407(1.0F, 0.0F, 0.0F);
            this.method_1470(block, 0.0, 0.0, 0.0, block.method_396(5, i));
            var4.method_1396();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

    }

    @Inject(method = "method_1455", at = @At("RETURN"), cancellable = true)
    private static void modLoaderRenderBlockIsItemFull3D(int i, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            cir.setReturnValue(ModLoader.renderBlockIsItemFull3D(i));
        }
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void modLoaderCfgGrassFix(CallbackInfo ci) {
        field_2047 = class_535Data.cfgGrassFix;
    }
}
