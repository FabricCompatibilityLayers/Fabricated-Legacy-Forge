package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import java.util.Random;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin extends Render {

    @Shadow private RenderBlocks renderBlocks;
    @Shadow private Random random;
    @Shadow public boolean field_77024_a;

    @Shadow private void func_77020_a(int par1, int par2) {}
    @Shadow public float zLevel;
    @Shadow public void renderTexturedQuad(int par1, int par2, int par3, int par4, int par5, int par6) {}

    // Pattern D (@Overwrite): hunks 1–4 all modify doRenderItem — the new
    // ForgeHooksClient.renderEntityItem guard restructures the entire if/else chain,
    // and three further changes (instanceof check, getTextureFile(), getRenderPasses()
    // loop bound, random reseed) are scattered throughout the body. @Overwrite is
    // clearest given >60% of the body is affected.
    // Logic delta: vanilla caches `Block var14 = Block.blocksList[var10.itemID]` once
    // at the top; this version drops that local and re-reads Block.blocksList[...] at
    // each use site (consistent with the patch).
    /**
     * @author FabricCompatibilityLayers
     * @reason Adds ForgeHooksClient.renderEntityItem early-skip; replaces Block null-check
     *         with instanceof ItemBlock; uses getTextureFile() instead of hardcoded paths;
     *         uses getRenderPasses() for loop bound; reseeds random each pass (vanilla bug fix).
     */
    @Overwrite
    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
        this.random.setSeed(187L);
        ItemStack var10 = par1EntityItem.item;
        GL11.glPushMatrix();
        float var11 = MathHelper.sin((par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
        float var12 = ((par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180.0F / (float)Math.PI);
        byte var13 = 1;
        if (par1EntityItem.item.stackSize > 1) {
            var13 = 2;
        }
        if (par1EntityItem.item.stackSize > 5) {
            var13 = 3;
        }
        if (par1EntityItem.item.stackSize > 20) {
            var13 = 4;
        }
        GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if (ForgeHooksClient.renderEntityItem(par1EntityItem, var10, var11, var12, random, renderManager.renderEngine, renderBlocks)) {
            // Forge handled rendering entirely; skip vanilla branches
        } else if (var10.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[var10.itemID].getRenderType())) {
            GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
            this.loadTexture(((BlockExtension)Block.blocksList[var10.itemID]).getTextureFile());
            float var23 = 0.25F;
            int var25 = Block.blocksList[var10.itemID].getRenderType();
            if (var25 == 1 || var25 == 19 || var25 == 12 || var25 == 2) {
                var23 = 0.5F;
            }
            GL11.glScalef(var23, var23, var23);
            for (int var27 = 0; var27 < var13; var27++) {
                GL11.glPushMatrix();
                if (var27 > 0) {
                    float var29 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var23;
                    float var32 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var23;
                    float var34 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var23;
                    GL11.glTranslatef(var29, var32, var34);
                }
                float var30 = 1.0F;
                this.renderBlocks.renderBlockAsItem(Block.blocksList[var10.itemID], var10.getItemDamage(), var30);
                GL11.glPopMatrix();
            }
        } else if (var10.getItem().requiresMultipleRenderPasses()) {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.loadTexture(((ItemExtension)Item.itemsList[var10.itemID]).getTextureFile());
            for (int var15 = 0; var15 <= ((ItemExtension)var10.getItem()).getRenderPasses(var10.getItemDamage()); var15++) {
                this.random.setSeed(187L); // fixes vanilla bug: layers would not render aligned properly
                int var16 = var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var15);
                float var17 = 1.0F;
                if (this.field_77024_a) {
                    int var18 = Item.itemsList[var10.itemID].getColorFromDamage(var10.getItemDamage(), var15);
                    float var19 = (var18 >> 16 & 0xFF) / 255.0F;
                    float var20 = (var18 >> 8 & 0xFF) / 255.0F;
                    float var21 = (var18 & 0xFF) / 255.0F;
                    GL11.glColor4f(var19 * var17, var20 * var17, var21 * var17, 1.0F);
                }
                this.func_77020_a(var16, var13);
            }
        } else {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int var22 = var10.getIconIndex();
            this.loadTexture(((ItemExtension)var10.getItem()).getTextureFile());
            if (this.field_77024_a) {
                int var24 = Item.itemsList[var10.itemID].getColorFromDamage(var10.getItemDamage(), 0);
                float var26 = (var24 >> 16 & 0xFF) / 255.0F;
                float var28 = (var24 >> 8 & 0xFF) / 255.0F;
                float var31 = (var24 & 0xFF) / 255.0F;
                float var33 = 1.0F;
                GL11.glColor4f(var26 * var33, var28 * var33, var31 * var33, 1.0F);
            }
            this.func_77020_a(var22, var13);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    // Pattern D (@Overwrite): hunks 5–7 modify drawItemIntoGui across all three branches —
    // the instanceof condition, both hardcoded texture paths, the loop bound, and the
    // if/else texture collapse. @Overwrite is clearest given >60% of the body is affected.
    // Logic delta: vanilla's first branch guards with `par3 < 256`; patched version uses
    // `instanceof ItemBlock` which is semantically equivalent but respects Forge's ItemBlock.
    /**
     * @author FabricCompatibilityLayers
     * @reason Replaces par3 < 256 block-check with instanceof ItemBlock; uses getTextureFile()
     *         instead of hardcoded paths; uses getRenderPasses() for loop bound.
     */
    @Overwrite
    public void drawItemIntoGui(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, int par3, int par4, int par5, int par6, int par7) {
        if (Item.itemsList[par3] instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[par3].getRenderType())) {
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture(((BlockExtension)Block.blocksList[par3]).getTextureFile()));
            Block var15 = Block.blocksList[par3];
            GL11.glPushMatrix();
            GL11.glTranslatef(par6 - 2, par7 + 3, -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int var17 = Item.itemsList[par3].getColorFromDamage(par4, 0);
            float var19 = (var17 >> 16 & 0xFF) / 255.0F;
            float var21 = (var17 >> 8 & 0xFF) / 255.0F;
            float var22 = (var17 & 0xFF) / 255.0F;
            if (this.field_77024_a) {
                GL11.glColor4f(var19, var21, var22, 1.0F);
            }
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.renderBlocks.useInventoryTint = this.field_77024_a;
            this.renderBlocks.renderBlockAsItem(var15, par4, 1.0F);
            this.renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        } else if (Item.itemsList[par3].requiresMultipleRenderPasses()) {
            GL11.glDisable(2896);
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture(((ItemExtension)Item.itemsList[par3]).getTextureFile()));
            for (int var8 = 0; var8 <= ((ItemExtension)Item.itemsList[par3]).getRenderPasses(par4); var8++) {
                int var9 = Item.itemsList[par3].getIconFromDamageForRenderPass(par4, var8);
                int var10 = Item.itemsList[par3].getColorFromDamage(par4, var8);
                float var11 = (var10 >> 16 & 0xFF) / 255.0F;
                float var12 = (var10 >> 8 & 0xFF) / 255.0F;
                float var13 = (var10 & 0xFF) / 255.0F;
                if (this.field_77024_a) {
                    GL11.glColor4f(var11, var12, var13, 1.0F);
                }
                this.renderTexturedQuad(par6, par7, var9 % 16 * 16, var9 / 16 * 16, 16, 16);
            }
            GL11.glEnable(2896);
        } else if (par5 >= 0) {
            GL11.glDisable(2896);
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture(((ItemExtension)Item.itemsList[par3]).getTextureFile()));
            int var14 = Item.itemsList[par3].getColorFromDamage(par4, 0);
            float var16 = (var14 >> 16 & 0xFF) / 255.0F;
            float var18 = (var14 >> 8 & 0xFF) / 255.0F;
            float var20 = (var14 & 0xFF) / 255.0F;
            if (this.field_77024_a) {
                GL11.glColor4f(var16, var18, var20, 1.0F);
            }
            this.renderTexturedQuad(par6, par7, par5 % 16 * 16, par5 / 16 * 16, 16, 16);
            GL11.glEnable(2896);
        }
        GL11.glEnable(2884);
    }

    // Pattern Q (@WrapWithCondition): drawItemIntoGui is a bare statement call whose result is
    // discarded — the condition gate is all we need; no interest in the return value.
    // Logic delta: patch uses `if (!ForgeHooksClient.renderInventoryItem(...))` to allow vanilla;
    // here we return true (allow) when Forge returns false, false (suppress) when Forge returns true
    // — identical net effect.
    @WrapWithCondition(
        method = "renderItemIntoGUI",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;drawItemIntoGui(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/RenderEngine;IIIII)V")
    )
    private boolean forge$renderInventoryItem(RenderItem instance, FontRenderer fontRenderer, RenderEngine renderEngine,
                                              int itemId, int damage, int iconIndex, int x, int y,
                                              @Local(argsOnly = true) ItemStack itemStack) {
        return !ForgeHooksClient.renderInventoryItem(renderBlocks, renderEngine, itemStack, field_77024_a, zLevel, (float) x, (float) y);
    }
}