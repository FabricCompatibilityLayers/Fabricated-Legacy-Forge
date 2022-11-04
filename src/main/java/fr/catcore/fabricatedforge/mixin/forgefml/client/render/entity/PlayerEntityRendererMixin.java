package fr.catcore.fabricatedforge.mixin.forgefml.client.render.entity;

import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.block.Block;
import net.minecraft.client.class_535;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BiPedModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin extends MobEntityRenderer {

    @Shadow public static String[] field_2136;

    @Shadow private BiPedModel field_2135;

    @Shadow private BiPedModel field_2134;

    @Shadow private BiPedModel field_2133;

    public PlayerEntityRendererMixin(EntityModel entityModel, float f) {
        super(entityModel, f);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int method_1564(PlayerEntity par1EntityPlayer, int par2, float par3) {
        ItemStack var4 = par1EntityPlayer.inventory.getArmor(3 - par2);
        if (var4 != null) {
            Item var5 = var4.getItem();
            if (var5 instanceof ArmorItem) {
                ArmorItem var6 = (ArmorItem)var5;
                this.method_1529(ForgeHooksClient.getArmorTexture(var4, "/armor/" + field_2136[var6.materialId] + "_" + (par2 == 2 ? 2 : 1) + ".png"));
                BiPedModel var7 = par2 == 2 ? this.field_2135 : this.field_2134;
                var7.head.visible = par2 == 0;
                var7.hat.visible = par2 == 0;
                var7.body.visible = par2 == 1 || par2 == 2;
                var7.field_1476.visible = par2 == 1;
                var7.field_1477.visible = par2 == 1;
                var7.field_1478.visible = par2 == 2 || par2 == 3;
                var7.field_1479.visible = par2 == 2 || par2 == 3;
                this.method_1556(var7);
                if (var4.hasEnchantments()) {
                    return 15;
                }

                return 1;
            }
        }

        return -1;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_1569(PlayerEntity par1EntityPlayer, float par2) {
        super.method_1569(par1EntityPlayer, par2);
        ItemStack var3 = par1EntityPlayer.inventory.getArmor(3);
        float var5;
        if (var3 != null && var3.getItem() instanceof BlockItem) {
            GL11.glPushMatrix();
            this.field_2133.head.preRender(0.0625F);
            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(var3, IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, var3, IItemRenderer.ItemRendererHelper.BLOCK_3D);
            if (is3D || class_535.method_1455(Block.BLOCKS[var3.id].getBlockType())) {
                var5 = 0.625F;
                GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(var5, -var5, var5);
            }

            this.dispatcher.field_2099.method_1357(par1EntityPlayer, var3, 0);
            GL11.glPopMatrix();
        }

        float var6;
        if (par1EntityPlayer.username.equals("deadmau5") && this.method_1530(par1EntityPlayer.skinUrl, null)) {
            for(int var19 = 0; var19 < 2; ++var19) {
                var5 = par1EntityPlayer.prevYaw + (par1EntityPlayer.yaw - par1EntityPlayer.prevYaw) * par2 - (par1EntityPlayer.field_3314 + (par1EntityPlayer.field_3313 - par1EntityPlayer.field_3314) * par2);
                var6 = par1EntityPlayer.prevPitch + (par1EntityPlayer.pitch - par1EntityPlayer.prevPitch) * par2;
                GL11.glPushMatrix();
                GL11.glRotatef(var5, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float)(var19 * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-var6, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-var5, 0.0F, 1.0F, 0.0F);
                float var7 = 1.3333334F;
                GL11.glScalef(var7, var7, var7);
                this.field_2133.method_1170(0.0625F);
                GL11.glPopMatrix();
            }
        }

        float var10;
        if (this.method_1530(par1EntityPlayer.field_4008, null)) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double var22 = par1EntityPlayer.capeX + (par1EntityPlayer.prevCapeX - par1EntityPlayer.capeX) * (double)par2 - (par1EntityPlayer.prevX + (par1EntityPlayer.x - par1EntityPlayer.prevX) * (double)par2);
            double var23 = par1EntityPlayer.capeY + (par1EntityPlayer.prevCapeY - par1EntityPlayer.capeY) * (double)par2 - (par1EntityPlayer.prevY + (par1EntityPlayer.y - par1EntityPlayer.prevY) * (double)par2);
            double var8 = par1EntityPlayer.capeZ + (par1EntityPlayer.prevCapeZ - par1EntityPlayer.capeZ) * (double)par2 - (par1EntityPlayer.prevZ + (par1EntityPlayer.z - par1EntityPlayer.prevZ) * (double)par2);
            var10 = par1EntityPlayer.field_3314 + (par1EntityPlayer.field_3313 - par1EntityPlayer.field_3314) * par2;
            double var11 = (double) MathHelper.sin(var10 * 3.1415927F / 180.0F);
            double var13 = (double)(-MathHelper.cos(var10 * 3.1415927F / 180.0F));
            float var15 = (float)var23 * 10.0F;
            if (var15 < -6.0F) {
                var15 = -6.0F;
            }

            if (var15 > 32.0F) {
                var15 = 32.0F;
            }

            float var16 = (float)(var22 * var11 + var8 * var13) * 100.0F;
            float var17 = (float)(var22 * var13 - var8 * var11) * 100.0F;
            if (var16 < 0.0F) {
                var16 = 0.0F;
            }

            float var18 = par1EntityPlayer.prevStrideDistance + (par1EntityPlayer.strideDistance - par1EntityPlayer.prevStrideDistance) * par2;
            var15 += MathHelper.sin((par1EntityPlayer.prevHorizontalSpeed + (par1EntityPlayer.horizontalSpeed - par1EntityPlayer.prevHorizontalSpeed) * par2) * 6.0F) * 32.0F * var18;
            if (par1EntityPlayer.isSneaking()) {
                var15 += 25.0F;
            }

            GL11.glRotatef(6.0F + var16 / 2.0F + var15, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var17 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var17 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.field_2133.method_1171(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack var21 = par1EntityPlayer.inventory.getMainHandStack();
        if (var21 != null) {
            GL11.glPushMatrix();
            this.field_2133.field_1476.preRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            if (par1EntityPlayer.fishHook != null) {
                var21 = new ItemStack(Item.STICK);
            }

            UseAction var20 = null;
            if (par1EntityPlayer.getItemUseTicks() > 0) {
                var20 = var21.getUseAction();
            }

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(var21, IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, var21, IItemRenderer.ItemRendererHelper.BLOCK_3D);
            if (var21.getItem() instanceof BlockItem && (is3D || class_535.method_1455(Block.BLOCKS[var21.id].getBlockType()))) {
                var6 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                var6 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(var6, -var6, var6);
            } else if (var21.id == Item.field_4349.id) {
                var6 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(var6, -var6, var6);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else if (Item.ITEMS[var21.id].isHandheld()) {
                var6 = 0.625F;
                if (Item.ITEMS[var21.id].shouldRotate()) {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                if (par1EntityPlayer.getItemUseTicks() > 0 && var20 == UseAction.BLOCK) {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(var6, -var6, var6);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                var6 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(var6, var6, var6);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            if (var21.getItem().method_3397()) {
                for(int var25 = 0; var25 < ((IItem)var21.getItem()).getRenderPasses(var21.getMeta()); ++var25) {
                    int var24 = var21.getItem().method_3344(var21.getMeta(), var25);
                    float var26 = (float)(var24 >> 16 & 255) / 255.0F;
                    float var9 = (float)(var24 >> 8 & 255) / 255.0F;
                    var10 = (float)(var24 & 255) / 255.0F;
                    GL11.glColor4f(var26, var9, var10, 1.0F);
                    this.dispatcher.field_2099.method_1357(par1EntityPlayer, var21, var25);
                }
            } else {
                this.dispatcher.field_2099.method_1357(par1EntityPlayer, var21, 0);
            }

            GL11.glPopMatrix();
        }

    }
}
