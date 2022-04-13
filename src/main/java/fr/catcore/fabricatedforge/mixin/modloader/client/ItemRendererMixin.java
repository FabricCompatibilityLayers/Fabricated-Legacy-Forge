package fr.catcore.fabricatedforge.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.class_535;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {

    @Shadow private Random field_2126;

    @Shadow private class_535 field_2125;

    @Shadow public boolean field_2123;

    @Shadow protected abstract void method_1542(int i, int j);

    /**
     * @author
     */
    @Overwrite
    public void render(ItemEntity itemEntity, double d, double e, double f, float g, float h) {
        this.field_2126.setSeed(187L);
        ItemStack var10 = itemEntity.stack;
        GL11.glPushMatrix();
        float var11 = MathHelper.sin(((float)itemEntity.age + h) / 10.0F + itemEntity.hoverHeight) * 0.1F + 0.1F;
        float var12 = (((float)itemEntity.age + h) / 20.0F + itemEntity.hoverHeight) * 57.295776F;
        byte var13 = 1;
        if (itemEntity.stack.count > 1) {
            var13 = 2;
        }

        if (itemEntity.stack.count > 5) {
            var13 = 3;
        }

        if (itemEntity.stack.count > 20) {
            var13 = 4;
        }

        GL11.glTranslatef((float)d, (float)e + var11, (float)f);
        GL11.glEnable(32826);
        Block var14 = var10.id < Block.BLOCKS.length ? Block.BLOCKS[var10.id] : null;
        int var16;
        float var19;
        float var20;
        float var24;
        if (var14 != null && class_535.method_1455(var14.getBlockType())) {
            GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
            this.method_1529("/terrain.png");
            float var22 = 0.25F;
            var16 = var14.getBlockType();
            if (var16 == 1 || var16 == 19 || var16 == 12 || var16 == 2) {
                var22 = 0.5F;
            }

            GL11.glScalef(var22, var22, var22);

            for(int var23 = 0; var23 < var13; ++var23) {
                GL11.glPushMatrix();
                if (var23 > 0) {
                    var24 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                    var19 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                    var20 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                    GL11.glTranslatef(var24, var19, var20);
                }

                var24 = 1.0F;
                this.field_2125.method_1447(var14, var10.getMeta(), var24);
                GL11.glPopMatrix();
            }
        } else {
            int var15;
            float var17;
            if (var10.getItem().method_3397()) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                this.method_1529("/gui/items.png");

                for(var15 = 0; var15 <= 1; ++var15) {
                    var16 = var10.getItem().method_3369(var10.getMeta(), var15);
                    var17 = 1.0F;
                    if (this.field_2123) {
                        int var18 = Item.ITEMS[var10.id].method_3344(var10.getMeta(), var15);
                        var19 = (float)(var18 >> 16 & 255) / 255.0F;
                        var20 = (float)(var18 >> 8 & 255) / 255.0F;
                        float var21 = (float)(var18 & 255) / 255.0F;
                        GL11.glColor4f(var19 * var17, var20 * var17, var21 * var17, 1.0F);
                    }

                    this.method_1542(var16, var13);
                }
            } else {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                var15 = var10.method_3429();
                if (var14 != null) {
                    this.method_1529("/terrain.png");
                } else {
                    this.method_1529("/gui/items.png");
                }

                if (this.field_2123) {
                    var16 = Item.ITEMS[var10.id].method_3344(var10.getMeta(), 0);
                    var17 = (float)(var16 >> 16 & 255) / 255.0F;
                    var24 = (float)(var16 >> 8 & 255) / 255.0F;
                    var19 = (float)(var16 & 255) / 255.0F;
                    var20 = 1.0F;
                    GL11.glColor4f(var17 * var20, var24 * var20, var19 * var20, 1.0F);
                }

                this.method_1542(var15, var13);
            }
        }

        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }
}
