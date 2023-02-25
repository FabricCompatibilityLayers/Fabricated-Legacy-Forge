package fr.catcore.fabricatedforge.mixin.forgefml.client.render.item;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import fr.catcore.fabricatedforge.mixininterface.IItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TextureManager;
import net.minecraft.client.BlockRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
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
public abstract class ItemRendererMixin extends EntityRenderer implements IItemRenderer {

    @Shadow private Random field_2126;

    @Shadow private BlockRenderer field_2125;

    @Shadow public boolean field_2123;

    @Shadow public float zOffset;

    @Shadow public abstract void method_1544(int i, int j, int k, int l, int m, int n);

    @Shadow protected abstract void method_1543(int i, int j, int k, int l, int m);

    @Shadow public static boolean field_5197;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void render(ItemEntity par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
        this.field_2126.setSeed(187L);
        ItemStack var10 = par1EntityItem.method_4548();
        if (var10.getItem() != null) {
            GL11.glPushMatrix();
            float var11 = this.shouldBob() ? MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverHeight) * 0.1F + 0.1F : 0.0F;
            float var12 = (((float)par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverHeight) * (180.0F / (float)Math.PI);
            byte var13 = this.getMiniBlockCountForItemStack(var10);
            GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
            GL11.glEnable(32826);
            if (!ForgeHooksClient.renderEntityItem(par1EntityItem, var10, var11, var12, this.field_2126, this.dispatcher.textureManager, this.field_2112)) {
                if (var10.getItem() instanceof BlockItem && BlockRenderer.method_1455(Block.BLOCKS[var10.id].getBlockType())) {
                    GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
                    if (field_5197) {
                        GL11.glScalef(1.25F, 1.25F, 1.25F);
                        GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    }

                    this.method_1529(Block.BLOCKS[var10.id].getTextureFile());
                    float var22 = 0.25F;
                    int var16 = Block.BLOCKS[var10.id].getBlockType();
                    if (var16 == 1 || var16 == 19 || var16 == 12 || var16 == 2) {
                        var22 = 0.5F;
                    }

                    GL11.glScalef(var22, var22, var22);

                    for(int var23 = 0; var23 < var13; ++var23) {
                        GL11.glPushMatrix();
                        if (var23 > 0) {
                            float var24 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                            float var19 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                            float var20 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                            GL11.glTranslatef(var24, var19, var20);
                        }

                        float var24 = 1.0F;
                        this.field_2125.method_1447(Block.BLOCKS[var10.id], var10.getData(), var24);
                        GL11.glPopMatrix();
                    }
                } else if (var10.getItem().method_3397()) {
                    if (field_5197) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    for(int var15 = 0; var15 < ((IItem)var10.getItem()).getRenderPasses(var10.getData()); ++var15) {
                        this.method_1529(((IItem)Item.ITEMS[var10.id]).getTextureFile());
                        this.field_2126.setSeed(187L);
                        int var16 = ((IItem)var10.getItem()).getIconIndex(var10, var15);
                        float var17 = 1.0F;
                        if (this.field_2123) {
                            int var18 = Item.ITEMS[var10.id].getDisplayColor(var10, var15);
                            float var19 = (float)(var18 >> 16 & 0xFF) / 255.0F;
                            float var20 = (float)(var18 >> 8 & 0xFF) / 255.0F;
                            float var21 = (float)(var18 & 0xFF) / 255.0F;
                            GL11.glColor4f(var19 * var17, var20 * var17, var21 * var17, 1.0F);
                            this.method_4335(par1EntityItem, var16, var13, par9, var19 * var17, var20 * var17, var21 * var17);
                        } else {
                            this.method_4335(par1EntityItem, var16, var13, par9, 1.0F, 1.0F, 1.0F);
                        }
                    }
                } else {
                    if (field_5197) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    int var15 = var10.method_3429();
                    this.method_1529(((IItem)var10.getItem()).getTextureFile());
                    if (this.field_2123) {
                        int var16 = Item.ITEMS[var10.id].getDisplayColor(var10, 0);
                        float var17 = (float)(var16 >> 16 & 0xFF) / 255.0F;
                        float var24 = (float)(var16 >> 8 & 0xFF) / 255.0F;
                        float var19 = (float)(var16 & 0xFF) / 255.0F;
                        float var20 = 1.0F;
                        this.method_4335(par1EntityItem, var15, var13, par9, var17 * var20, var24 * var20, var19 * var20);
                    } else {
                        this.method_4335(par1EntityItem, var15, var13, par9, 1.0F, 1.0F, 1.0F);
                    }
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void method_4335(ItemEntity par1EntityItem, int par2, int par3, float par4, float par5, float par6, float par7) {
        Tessellator var8 = Tessellator.INSTANCE;
        float var9 = (float)(par2 % 16 * 16 + 0) / 256.0F;
        float var10 = (float)(par2 % 16 * 16 + 16) / 256.0F;
        float var11 = (float)(par2 / 16 * 16 + 0) / 256.0F;
        float var12 = (float)(par2 / 16 * 16 + 16) / 256.0F;
        float var13 = 1.0F;
        float var14 = 0.5F;
        float var15 = 0.25F;
        if (this.dispatcher.options.fancyGraphics) {
            GL11.glPushMatrix();
            if (field_5197) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else {
                GL11.glRotatef((((float)par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverHeight) * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float var16 = 0.0625F;
            float var17 = 0.021875F;
            ItemStack var18 = par1EntityItem.method_4548();
            int var19 = var18.count;
            byte var24 = this.getMiniItemCountForItemStack(var18);
            GL11.glTranslatef(-var14, -var15, -((var16 + var17) * (float)var24 / 2.0F));

            for(int var20 = 0; var20 < var24; ++var20) {
                if (var20 > 0 && this.shouldSpreadItems()) {
                    float x = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float y = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float z = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(x, y, var16 + var17);
                } else {
                    GL11.glTranslatef(0.0F, 0.0F, var16 + var17);
                }

                this.method_1529(((IItem)Item.ITEMS[var18.id]).getTextureFile());
                GL11.glColor4f(par5, par6, par7, 1.0F);
                HeldItemRenderer.method_4306(var8, var10, var11, var9, var12, var16);
                if (var18 != null && var18.hasEnchantmentGlint()) {
                    GL11.glDepthFunc(514);
                    GL11.glDisable(2896);
                    this.dispatcher.textureManager.bindTexture(this.dispatcher.textureManager.getTextureFromPath("%blur%/misc/glint.png"));
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(768, 1);
                    float var21 = 0.76F;
                    GL11.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    float var22 = 0.125F;
                    GL11.glScalef(var22, var22, var22);
                    float var23 = (float)(Minecraft.getTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(var23, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    HeldItemRenderer.method_4306(var8, 0.0F, 0.0F, 1.0F, 1.0F, var16);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(var22, var22, var22);
                    var23 = (float)(Minecraft.getTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-var23, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    HeldItemRenderer.method_4306(var8, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(5888);
                    GL11.glDisable(3042);
                    GL11.glEnable(2896);
                    GL11.glDepthFunc(515);
                }
            }

            GL11.glPopMatrix();
        } else {
            for(int var25 = 0; var25 < par3; ++var25) {
                GL11.glPushMatrix();
                if (var25 > 0) {
                    float var17 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var27 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var26 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var17, var27, var26);
                }

                if (!field_5197) {
                    GL11.glRotatef(180.0F - this.dispatcher.yaw, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                var8.begin();
                var8.normal(0.0F, 1.0F, 0.0F);
                var8.vertex((double)(0.0F - var14), (double)(0.0F - var15), 0.0, (double)var9, (double)var12);
                var8.vertex((double)(var13 - var14), (double)(0.0F - var15), 0.0, (double)var10, (double)var12);
                var8.vertex((double)(var13 - var14), (double)(1.0F - var15), 0.0, (double)var10, (double)var11);
                var8.vertex((double)(0.0F - var14), (double)(1.0F - var15), 0.0, (double)var9, (double)var11);
                var8.end();
                GL11.glPopMatrix();
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_1546(TextRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
        int var6 = par3ItemStack.id;
        int var7 = par3ItemStack.getData();
        int var8 = par3ItemStack.method_3429();
        if (par3ItemStack.getItem() instanceof BlockItem && BlockRenderer.method_1455(Block.BLOCKS[par3ItemStack.id].getBlockType())) {
            Block var15 = Block.BLOCKS[var6];
            par2RenderEngine.bindTexture(par2RenderEngine.getTextureFromPath(var15.getTextureFile()));
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zOffset);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int var10 = Item.ITEMS[var6].getDisplayColor(par3ItemStack, 0);
            float var16 = (float)(var10 >> 16 & 0xFF) / 255.0F;
            float var12 = (float)(var10 >> 8 & 0xFF) / 255.0F;
            float var13 = (float)(var10 & 0xFF) / 255.0F;
            if (this.field_2123) {
                GL11.glColor4f(var16, var12, var13, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.field_2125.field_2048 = this.field_2123;
            this.field_2125.method_1447(var15, var7, 1.0F);
            this.field_2125.field_2048 = true;
            GL11.glPopMatrix();
        } else if (Item.ITEMS[var6].method_3397()) {
            GL11.glDisable(2896);
            par2RenderEngine.bindTexture(par2RenderEngine.getTextureFromPath(((IItem)Item.ITEMS[var6]).getTextureFile()));

            for(int var9 = 0; var9 < ((IItem)Item.ITEMS[var6]).getRenderPasses(var7); ++var9) {
                int var10 = ((IItem)Item.ITEMS[var6]).getIconIndex(par3ItemStack, var9);
                int var11 = Item.ITEMS[var6].getDisplayColor(par3ItemStack, var9);
                float var12 = (float)(var11 >> 16 & 0xFF) / 255.0F;
                float var13 = (float)(var11 >> 8 & 0xFF) / 255.0F;
                float var14 = (float)(var11 & 0xFF) / 255.0F;
                if (this.field_2123) {
                    GL11.glColor4f(var12, var13, var14, 1.0F);
                }

                this.method_1544(par4, par5, var10 % 16 * 16, var10 / 16 * 16, 16, 16);
            }

            GL11.glEnable(2896);
        } else if (var8 >= 0) {
            GL11.glDisable(2896);
            par2RenderEngine.bindTexture(par2RenderEngine.getTextureFromPath(((IItem)par3ItemStack.getItem()).getTextureFile()));
            int var9 = Item.ITEMS[var6].getDisplayColor(par3ItemStack, 0);
            float var17 = (float)(var9 >> 16 & 0xFF) / 255.0F;
            float var16 = (float)(var9 >> 8 & 0xFF) / 255.0F;
            float var12 = (float)(var9 & 0xFF) / 255.0F;
            if (this.field_2123) {
                GL11.glColor4f(var17, var16, var12, 1.0F);
            }

            this.method_1544(par4, par5, var8 % 16 * 16, var8 / 16 * 16, 16, 16);
            GL11.glEnable(2896);
        }

        GL11.glEnable(2884);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_4336(TextRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
        if (par3ItemStack != null) {
            if (!ForgeHooksClient.renderInventoryItem(this.field_2112, par2RenderEngine, par3ItemStack, this.field_2123, this.zOffset, (float)par4, (float)par5)
            )
            {
                this.method_1546(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);
            }

            if (par3ItemStack != null && par3ItemStack.hasEnchantmentGlint()) {
                GL11.glDepthFunc(516);
                GL11.glDisable(2896);
                GL11.glDepthMask(false);
                par2RenderEngine.bindTexture(par2RenderEngine.getTextureFromPath("%blur%/misc/glint.png"));
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

    @Override
    public boolean shouldSpreadItems() {
        return true;
    }

    @Override
    public boolean shouldBob() {
        return true;
    }

    @Override
    public byte getMiniBlockCountForItemStack(ItemStack stack) {
        byte var13 = 1;
        if (stack.count > 1) {
            var13 = 2;
        }

        if (stack.count > 5) {
            var13 = 3;
        }

        if (stack.count > 20) {
            var13 = 4;
        }

        if (stack.count > 40) {
            var13 = 5;
        }

        return var13;
    }

    @Override
    public byte getMiniItemCountForItemStack(ItemStack stack) {
        int var19 = stack.count;
        byte var24;
        if (var19 < 2) {
            var24 = 1;
        } else if (var19 < 16) {
            var24 = 2;
        } else if (var19 < 32) {
            var24 = 3;
        } else {
            var24 = 4;
        }

        return var24;
    }
}
