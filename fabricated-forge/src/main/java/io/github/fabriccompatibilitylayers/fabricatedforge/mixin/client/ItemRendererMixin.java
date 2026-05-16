package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemMap;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapData;
import net.minecraft.src.MapItemRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemRenderer.class, priority = 1010)
public abstract class ItemRendererMixin {

    @Shadow private Minecraft mc;
    @Shadow private RenderBlocks renderBlocksInstance;
    @Shadow private float prevEquippedProgress;
    @Shadow private float equippedProgress;
    @Shadow private ItemStack itemToRender;
    @Shadow private MapItemRenderer mapItemRenderer;

    @Shadow
    private void renderItemIn2D(Tessellator par1Tessellator, float par2, float par3, float par4, float par5) {}

    // Pattern D (@Overwrite): the top-level if/else structure is fully restructured around
    // a new custom-renderer check — no surgical injection can replace the whole branch tree cleanly.
    /**
     * @author FabricCompatibilityLayers
     * @reason Adds IItemRenderer support via MinecraftForgeClient.getItemRenderer for equipped items
     */
    @Overwrite
    public void renderItem(EntityLiving par1EntityLiving, ItemStack par2ItemStack, int par3) {
        GL11.glPushMatrix();
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(par2ItemStack, IItemRenderer.ItemRenderType.EQUIPPED);

        if (customRenderer != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(((ItemExtension) par2ItemStack.getItem()).getTextureFile()));
            ForgeHooksClient.renderEquippedItem(customRenderer, this.renderBlocksInstance, par1EntityLiving, par2ItemStack);
        } else if (par2ItemStack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[par2ItemStack.itemID].getRenderType())) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(((ItemExtension) par2ItemStack.getItem()).getTextureFile()));
            this.renderBlocksInstance.renderBlockAsItem(Block.blocksList[par2ItemStack.itemID], par2ItemStack.getItemDamage(), 1.0F);
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(((ItemExtension) par2ItemStack.getItem()).getTextureFile()));

            Tessellator var5 = Tessellator.instance;
            int var6 = par1EntityLiving.getItemIcon(par2ItemStack, par3);
            float var7 = (var6 % 16 * 16 + 0.0F) / 256.0F;
            float var8 = (var6 % 16 * 16 + 15.99F) / 256.0F;
            float var9 = (var6 / 16 * 16 + 0.0F) / 256.0F;
            float var10 = (var6 / 16 * 16 + 15.99F) / 256.0F;
            float var11 = 0.0F;
            float var12 = 0.3F;
            GL11.glEnable(32826);
            GL11.glTranslatef(-var11, -var12, 0.0F);
            float var13 = 1.5F;
            GL11.glScalef(var13, var13, var13);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            this.renderItemIn2D(var5, var8, var9, var7, var10);
            if (par2ItemStack != null && par2ItemStack.hasEffect() && par3 == 0) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("%blur%/misc/glint.png"));
                GL11.glEnable(3042);
                GL11.glBlendFunc(768, 1);
                float var14 = 0.76F;
                GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                float var15 = 0.125F;
                GL11.glScalef(var15, var15, var15);
                float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(var16, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                this.renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(var15, var15, var15);
                var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-var16, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                this.renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(5888);
                GL11.glDisable(3042);
                GL11.glEnable(2896);
                GL11.glDepthFunc(515);
            }

            GL11.glDisable(32826);
        }

        GL11.glPopMatrix();
    }

    // Pattern D (@Overwrite): hunks 2 and 3 together — hunk 2 restructures the map condition
    // and renderer dispatch; hunk 3 replaces the hardcoded two-pass render with a dynamic loop.
    // Surgical injections for hunk 2 were superseded by this overwrite.
    /**
     * @author FabricCompatibilityLayers
     * @reason Adds IItemRenderer/ItemMap instanceof support for map rendering; dynamic render pass loop
     */
    @Overwrite
    public void renderItemInFirstPerson(float par1) {
        float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * par1;
        EntityClientPlayerMP var3 = this.mc.thePlayer;
        float var4 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * par1;
        GL11.glPushMatrix();
        GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        if (var3 instanceof EntityPlayerSP) {
            EntityPlayerSP var5 = (EntityPlayerSP) var3;
            float var6 = var5.prevRenderArmPitch + (var5.renderArmPitch - var5.prevRenderArmPitch) * par1;
            float var7 = var5.prevRenderArmYaw + (var5.renderArmYaw - var5.prevRenderArmYaw) * par1;
            GL11.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
        }

        ItemStack var17 = this.itemToRender;
        float var18 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
        var18 = 1.0F;
        int var20 = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ), 0);
        int var8 = var20 % 65536;
        int var9 = var20 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var8 / 1.0F, var9 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (var17 != null) {
            var20 = Item.itemsList[var17.itemID].getColorFromDamage(var17.getItemDamage(), 0);
            float var25 = (var20 >> 16 & 0xFF) / 255.0F;
            float var34 = (var20 >> 8 & 0xFF) / 255.0F;
            float var10 = (var20 & 0xFF) / 255.0F;
            GL11.glColor4f(var18 * var25, var18 * var34, var18 * var10, 1.0F);
        } else {
            GL11.glColor4f(var18, var18, var18, 1.0F);
        }

        if (var17 != null && var17.getItem() instanceof ItemMap) {
            IItemRenderer custom = MinecraftForgeClient.getItemRenderer(var17, IItemRenderer.ItemRenderType.FIRST_PERSON_MAP);
            GL11.glPushMatrix();
            float var24 = 0.8F;
            float var31 = var3.getSwingProgress(par1);
            float var40 = MathHelper.sin(var31 * (float) Math.PI);
            float var48 = MathHelper.sin(MathHelper.sqrt_float(var31) * (float) Math.PI);
            GL11.glTranslatef(-var48 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var31) * (float) Math.PI * 2.0F) * 0.2F, -var40 * 0.2F);
            float var32 = 1.0F - var4 / 45.0F + 0.1F;
            if (var32 < 0.0F) {
                var32 = 0.0F;
            }

            if (var32 > 1.0F) {
                var32 = 1.0F;
            }

            float var33 = -MathHelper.cos(var32 * (float) Math.PI) * 0.5F + 0.5F;
            GL11.glTranslatef(0.0F, 0.0F * var24 - (1.0F - var2) * 1.2F - var33 * 0.5F + 0.04F, -0.9F * var24);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var33 * -85.0F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(32826);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));

            for (int var41 = 0; var41 < 2; var41++) {
                int var49 = var41 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.0F, -0.6F, 1.1F * var49);
                GL11.glRotatef(-45 * var49, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * var49, 0.0F, 1.0F, 0.0F);
                Render var56 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
                RenderPlayer var61 = (RenderPlayer) var56;
                float var65 = 1.0F;
                GL11.glScalef(var65, var65, var65);
                var61.drawFirstPersonHand();
                GL11.glPopMatrix();
            }

            float var42 = var3.getSwingProgress(par1);
            var48 = MathHelper.sin(var42 * var42 * (float) Math.PI);
            float var57 = MathHelper.sin(MathHelper.sqrt_float(var42) * (float) Math.PI);
            GL11.glRotatef(-var48 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var57 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var57 * 80.0F, 1.0F, 0.0F, 0.0F);
            float var62 = 0.38F;
            GL11.glScalef(var62, var62, var62);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
            float var66 = 0.015625F;
            GL11.glScalef(var66, var66, var66);
            this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/misc/mapbg.png"));
            Tessellator var69 = Tessellator.instance;
            GL11.glNormal3f(0.0F, 0.0F, -1.0F);
            var69.startDrawingQuads();
            byte var71 = 7;
            var69.addVertexWithUV(0 - var71, 128 + var71, 0.0, 0.0, 1.0);
            var69.addVertexWithUV(128 + var71, 128 + var71, 0.0, 1.0, 1.0);
            var69.addVertexWithUV(128 + var71, 0 - var71, 0.0, 1.0, 0.0);
            var69.addVertexWithUV(0 - var71, 0 - var71, 0.0, 0.0, 0.0);
            var69.draw();
            MapData var16 = ((ItemMap) var17.getItem()).getMapData(var17, this.mc.theWorld);
            if (custom == null) {
                this.mapItemRenderer.renderMap(this.mc.thePlayer, this.mc.renderEngine, var16);
            } else {
                custom.renderItem(IItemRenderer.ItemRenderType.FIRST_PERSON_MAP, var17, this.mc.thePlayer, this.mc.renderEngine, var16);
            }
            GL11.glPopMatrix();
        } else if (var17 != null) {
            GL11.glPushMatrix();
            float var22 = 0.8F;
            if (var3.getItemInUseCount() > 0) {
                EnumAction var27 = var17.getItemUseAction();
                if (var27 == EnumAction.eat || var27 == EnumAction.drink) {
                    float var36 = var3.getItemInUseCount() - par1 + 1.0F;
                    float var44 = 1.0F - var36 / var17.getMaxItemUseDuration();
                    float var11 = 1.0F - var44;
                    var11 = var11 * var11 * var11;
                    var11 = var11 * var11 * var11;
                    var11 = var11 * var11 * var11;
                    float var12 = 1.0F - var11;
                    GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(var36 / 4.0F * (float) Math.PI) * 0.1F) * (var44 > 0.2 ? 1 : 0), 0.0F);
                    GL11.glTranslatef(var12 * 0.6F, -var12 * 0.5F, 0.0F);
                    GL11.glRotatef(var12 * 90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(var12 * 10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(var12 * 30.0F, 0.0F, 0.0F, 1.0F);
                }
            } else {
                float var26 = var3.getSwingProgress(par1);
                float var35 = MathHelper.sin(var26 * (float) Math.PI);
                float var43 = MathHelper.sin(MathHelper.sqrt_float(var26) * (float) Math.PI);
                GL11.glTranslatef(-var43 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var26) * (float) Math.PI * 2.0F) * 0.2F, -var35 * 0.2F);
            }

            GL11.glTranslatef(0.7F * var22, -0.65F * var22 - (1.0F - var2) * 0.6F, -0.9F * var22);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(32826);
            float var28 = var3.getSwingProgress(par1);
            float var37 = MathHelper.sin(var28 * var28 * (float) Math.PI);
            float var45 = MathHelper.sin(MathHelper.sqrt_float(var28) * (float) Math.PI);
            GL11.glRotatef(-var37 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var45 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var45 * 80.0F, 1.0F, 0.0F, 0.0F);
            float var54 = 0.4F;
            GL11.glScalef(var54, var54, var54);
            if (var3.getItemInUseCount() > 0) {
                EnumAction var58 = var17.getItemUseAction();
                if (var58 == EnumAction.block) {
                    GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
                    GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
                } else if (var58 == EnumAction.bow) {
                    GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
                    float var13 = var17.getMaxItemUseDuration() - (var3.getItemInUseCount() - par1 + 1.0F);
                    float var14 = var13 / 20.0F;
                    var14 = (var14 * var14 + var14 * 2.0F) / 3.0F;
                    if (var14 > 1.0F) {
                        var14 = 1.0F;
                    }

                    if (var14 > 0.1F) {
                        GL11.glTranslatef(0.0F, MathHelper.sin((var13 - 0.1F) * 1.3F) * 0.01F * (var14 - 0.1F), 0.0F);
                    }

                    GL11.glTranslatef(0.0F, 0.0F, var14 * 0.1F);
                    GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                    float var15 = 1.0F + var14 * 0.2F;
                    GL11.glScalef(1.0F, 1.0F, var15);
                    GL11.glTranslatef(0.0F, -0.5F, 0.0F);
                    GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
                }
            }

            if (var17.getItem().shouldRotateAroundWhenRendering()) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if (var17.getItem().requiresMultipleRenderPasses()) {
                this.renderItem(var3, var17, 0);
                for (int x = 1; x < ((ItemExtension) var17.getItem()).getRenderPasses(var17.getItemDamage()); x++) {
                    int var59 = Item.itemsList[var17.itemID].getColorFromDamage(var17.getItemDamage(), x);
                    float var63 = (var59 >> 16 & 0xFF) / 255.0F;
                    float var68 = (var59 >> 8 & 0xFF) / 255.0F;
                    float var70 = (var59 & 0xFF) / 255.0F;
                    GL11.glColor4f(var18 * var63, var18 * var68, var18 * var70, 1.0F);
                    this.renderItem(var3, var17, x);
                }
            } else {
                this.renderItem(var3, var17, 0);
            }

            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            float var23 = 0.8F;
            float var29 = var3.getSwingProgress(par1);
            float var38 = MathHelper.sin(var29 * (float) Math.PI);
            float var46 = MathHelper.sin(MathHelper.sqrt_float(var29) * (float) Math.PI);
            GL11.glTranslatef(-var46 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var29) * (float) Math.PI * 2.0F) * 0.4F, -var38 * 0.4F);
            GL11.glTranslatef(0.8F * var23, -0.75F * var23 - (1.0F - var2) * 0.6F, -0.9F * var23);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(32826);
            float var30 = var3.getSwingProgress(par1);
            float var39 = MathHelper.sin(var30 * var30 * (float) Math.PI);
            var46 = MathHelper.sin(MathHelper.sqrt_float(var30) * (float) Math.PI);
            GL11.glRotatef(var46 * 70.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var39 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
            GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
            GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            Render var55 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
            RenderPlayer var60 = (RenderPlayer) var55;
            float var64 = 1.0F;
            GL11.glScalef(var64, var64, var64);
            var60.drawFirstPersonHand();
            GL11.glPopMatrix();
        }

        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
    }
}