package fr.catcore.fabricatedforge.mixin.forgefml.client.render.item;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.block.Block;
import net.minecraft.client.class_534;
import net.minecraft.client.class_535;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
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

    @Shadow public float zOffset;

    @Shadow public abstract void method_1544(int i, int j, int k, int l, int m, int n);

    @Shadow protected abstract void method_1543(int i, int j, int k, int l, int m);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void render(ItemEntity par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
        this.field_2126.setSeed(187L);
        ItemStack var10 = par1EntityItem.stack;
        GL11.glPushMatrix();
        float var11 = MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverHeight) * 0.1F + 0.1F;
        float var12 = (((float)par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverHeight) * 57.295776F;
        byte var13 = 1;
        if (par1EntityItem.stack.count > 1) {
            var13 = 2;
        }

        if (par1EntityItem.stack.count > 5) {
            var13 = 3;
        }

        if (par1EntityItem.stack.count > 20) {
            var13 = 4;
        }

        GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
        GL11.glEnable(32826);
        if (!ForgeHooksClient.renderEntityItem(par1EntityItem, var10, var11, var12, this.field_2126, this.dispatcher.field_2098, this.field_2125)) {
            int var16;
            float var19;
            float var20;
            float var24;
            if (var10.getItem() instanceof BlockItem && class_535.method_1455(Block.BLOCKS[var10.id].getBlockType())) {
                GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
                this.method_1529(((IBlock)Block.BLOCKS[var10.id]).getTextureFile());
                float var22 = 0.25F;
                var16 = Block.BLOCKS[var10.id].getBlockType();
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
                    this.field_2125.method_1447(Block.BLOCKS[var10.id], var10.getMeta(), var24);
                    GL11.glPopMatrix();
                }
            } else {
                int var15;
                float var17;
                if (var10.getItem().method_3397()) {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    this.method_1529(((IItem)Item.ITEMS[var10.id]).getTextureFile());

                    for(var15 = 0; var15 <= ((IItem)var10.getItem()).getRenderPasses(var10.getMeta()); ++var15) {
                        this.field_2126.setSeed(187L);
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
                    this.method_1529(((IItem)var10.getItem()).getTextureFile());
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
        }

        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_1545(TextRenderer par1FontRenderer, class_534 par2RenderEngine, int par3, int par4, int par5, int par6, int par7) {
        float var16;
        int var9;
        float var11;
        float var12;
        if (Item.ITEMS[par3] instanceof BlockItem && class_535.method_1455(Block.BLOCKS[par3].getBlockType())) {
            par2RenderEngine.method_1426(par2RenderEngine.getTextureFromPath(((IBlock)Block.BLOCKS[par3]).getTextureFile()));
            Block var15 = Block.BLOCKS[par3];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(par6 - 2), (float)(par7 + 3), -3.0F + this.zOffset);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            var9 = Item.ITEMS[par3].method_3344(par4, 0);
            var16 = (float)(var9 >> 16 & 255) / 255.0F;
            var11 = (float)(var9 >> 8 & 255) / 255.0F;
            var12 = (float)(var9 & 255) / 255.0F;
            if (this.field_2123) {
                GL11.glColor4f(var16, var11, var12, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.field_2125.field_2048 = this.field_2123;
            this.field_2125.method_1447(var15, par4, 1.0F);
            this.field_2125.field_2048 = true;
            GL11.glPopMatrix();
        } else {
            int var8;
            if (Item.ITEMS[par3].method_3397()) {
                GL11.glDisable(2896);
                par2RenderEngine.method_1426(par2RenderEngine.getTextureFromPath(((IItem)Item.ITEMS[par3]).getTextureFile()));

                for(var8 = 0; var8 <= ((IItem)Item.ITEMS[par3]).getRenderPasses(par4); ++var8) {
                    var9 = Item.ITEMS[par3].method_3369(par4, var8);
                    int var10 = Item.ITEMS[par3].method_3344(par4, var8);
                    var11 = (float)(var10 >> 16 & 255) / 255.0F;
                    var12 = (float)(var10 >> 8 & 255) / 255.0F;
                    float var13 = (float)(var10 & 255) / 255.0F;
                    if (this.field_2123) {
                        GL11.glColor4f(var11, var12, var13, 1.0F);
                    }

                    this.method_1544(par6, par7, var9 % 16 * 16, var9 / 16 * 16, 16, 16);
                }

                GL11.glEnable(2896);
            } else if (par5 >= 0) {
                GL11.glDisable(2896);
                par2RenderEngine.method_1426(par2RenderEngine.getTextureFromPath(((IItem)Item.ITEMS[par3]).getTextureFile()));
                var8 = Item.ITEMS[par3].method_3344(par4, 0);
                float var14 = (float)(var8 >> 16 & 255) / 255.0F;
                var16 = (float)(var8 >> 8 & 255) / 255.0F;
                var11 = (float)(var8 & 255) / 255.0F;
                if (this.field_2123) {
                    GL11.glColor4f(var14, var16, var11, 1.0F);
                }

                this.method_1544(par6, par7, par5 % 16 * 16, par5 / 16 * 16, 16, 16);
                GL11.glEnable(2896);
            }
        }

        GL11.glEnable(2884);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_1546(TextRenderer par1FontRenderer, class_534 par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
        if (par3ItemStack != null) {
            if (!ForgeHooksClient.renderInventoryItem(this.field_2125, par2RenderEngine, par3ItemStack, this.field_2123, this.zOffset, (float)par4, (float)par5)) {
                this.method_1545(par1FontRenderer, par2RenderEngine, par3ItemStack.id, par3ItemStack.getMeta(), par3ItemStack.method_3429(), par4, par5);
            }

            if (par3ItemStack != null && par3ItemStack.hasEnchantmentGlint()) {
                GL11.glDepthFunc(516);
                GL11.glDisable(2896);
                GL11.glDepthMask(false);
                par2RenderEngine.method_1426(par2RenderEngine.getTextureFromPath("%blur%/misc/glint.png"));
                this.zOffset -= 50.0F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(774, 774);
                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
                this.method_1543(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
                GL11.glDisable(3042);
                GL11.glDepthMask(true);
                this.zOffset += 50.0F;
                GL11.glEnable(2896);
                GL11.glDepthFunc(515);
            }
        }

    }
}
