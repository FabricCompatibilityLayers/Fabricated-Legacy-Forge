/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.RenderGlobalExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Shadow private Minecraft mc;
    @Shadow private float fovMultiplierTemp;
    @Shadow private float fovModifierHandPrev;
    @Shadow private float fovModifierHand;
    @Shadow
    public int debugViewDirection;
    @Shadow private float prevDebugCamFOV;
    @Shadow private float debugCamFOV;
    @Shadow private float prevCamRoll;
    @Shadow private float camRoll;
    @Shadow private float thirdPersonDistanceTemp;
    @Shadow private float thirdPersonDistance;
    @Shadow private float prevDebugCamYaw;
    @Shadow private float debugCamYaw;
    @Shadow private float prevDebugCamPitch;
    @Shadow private float debugCamPitch;
    @Shadow private boolean cloudFog;
    @Shadow private boolean lightmapUpdateNeeded;
    @Shadow private double cameraZoom;
    @Shadow public void getMouseOver(float par1) {}
    @Shadow private void updateFogColor(float par1) {}
    @Shadow private void setupCameraTransform(float par1, int par2) {}
    @Shadow private void setupFog(int par1, float par2) {}
    @Shadow public void enableLightmap(double par1) {}
    @Shadow public void disableLightmap(double par1) {}
    @Shadow protected void renderRainSnow(float par1) {}
    @Shadow private void renderHand(float par1, int par2) {}

    // Pattern D (@Overwrite): replaces updateFovModifierHand entirely — the patch changes the type
    // of var1 and adds an else branch, making surgical injection impractical for a 4-line method.
    @Shadow
    public static int anaglyphField;

    @Shadow
    protected abstract void updateLightmap(float par1);

    @Shadow
    protected abstract void func_82829_a(RenderGlobal par1RenderGlobal, float par2);

    /**
     * @author FabricCompatibilityLayers
     * @reason Guards the EntityPlayerSP cast with instanceof; falls back to mc.thePlayer when
     *         renderViewEntity is not an EntityPlayerSP, allowing non-player view entities.
     */
    @Overwrite
    private void updateFovModifierHand() {
        if (mc.renderViewEntity instanceof EntityPlayerSP)
        {
            EntityPlayerSP var1 = (EntityPlayerSP)this.mc.renderViewEntity;
            this.fovMultiplierTemp = var1.getFOVMultiplier();
        }
        else
        {
            this.fovMultiplierTemp = mc.thePlayer.getFOVMultiplier();
        }
        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;
    }

    // Pattern D (@Overwrite): no surgical mixin can change a local variable's declared type —
    // only @Overwrite lets us replace (EntityPlayer) with (EntityLiving) cleanly.
    /**
     * @author FabricCompatibilityLayers
     * @reason Casts renderViewEntity to EntityLiving instead of EntityPlayer so non-player
     *         view entities (e.g. spectator mobs) do not throw ClassCastException.
     */
    @Overwrite
    private float getFOVModifier(float par1, boolean par2) {
        if (this.debugViewDirection > 0) {
            return 90.0F;
        } else {
            EntityLiving var3 = this.mc.renderViewEntity;
            float var4 = 70.0F;
            if (par2) {
                var4 += this.mc.gameSettings.fovSetting * 40.0F;
                var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * par1;
            }

            if (var3.getHealth() <= 0) {
                float var5 = (float)var3.deathTime + par1;
                var4 /= (1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F;
            }

            int var7 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);
            if (var7 != 0 && Block.blocksList[var7].blockMaterial == Material.water) {
                var4 = var4 * 60.0F / 70.0F;
            }

            return var4 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * par1;
        }
    }

    // Pattern D (@Overwrite): the patch removes the 8-line inline bed-rotation block and replaces
    // it with one ForgeHooksClient call — no clean injection point exists to excise a conditional
    // block, so @Overwrite copies the ~90-line method.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates bed camera orientation to ForgeHooksClient.orientBedCamera, allowing mods
     *         to override the sleeping camera angle instead of relying on hardcoded bed metadata.
     */
    @Overwrite
    private void orientCamera(float par1) {
        EntityLiving var2 = this.mc.renderViewEntity;
        float var3 = var2.yOffset - 1.62F;
        double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)par1;
        double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)par1 - (double)var3;
        double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)par1;
        GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);
        if (var2.isPlayerSleeping()) {
            var3 = (float)((double)var3 + (double)1.0F);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.mc.gameSettings.debugCamEnable) {
                ForgeHooksClient.orientBedCamera(mc, var2);

                GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double var30 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * par1);
            if (this.mc.gameSettings.debugCamEnable) {
                float var31 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * par1;
                float var13 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * par1;
                GL11.glTranslatef(0.0F, 0.0F, (float)(-var30));
                GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var31, 0.0F, 1.0F, 0.0F);
            } else {
                float var32 = var2.rotationYaw;
                float var33 = var2.rotationPitch;
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    var33 += 180.0F;
                }

                double var14 = (double)(-MathHelper.sin(var32 / 180.0F * (float)Math.PI) * MathHelper.cos(var33 / 180.0F * (float)Math.PI)) * var30;
                double var16 = (double)(MathHelper.cos(var32 / 180.0F * (float)Math.PI) * MathHelper.cos(var33 / 180.0F * (float)Math.PI)) * var30;
                double var18 = (double)(-MathHelper.sin(var33 / 180.0F * (float)Math.PI)) * var30;

                for(int var20 = 0; var20 < 8; ++var20) {
                    float var21 = (float)((var20 & 1) * 2 - 1);
                    float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
                    float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
                    var21 *= 0.1F;
                    var22 *= 0.1F;
                    var23 *= 0.1F;
                    MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(this.mc.theWorld.func_82732_R().getVecFromPool(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), this.mc.theWorld.func_82732_R().getVecFromPool(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));
                    if (var24 != null) {
                        double var25 = var24.hitVec.distanceTo(this.mc.theWorld.func_82732_R().getVecFromPool(var4, var6, var8));
                        if (var25 < var30) {
                            var30 = var25;
                        }
                    }
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.rotationPitch - var33, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var2.rotationYaw - var32, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-var30));
                GL11.glRotatef(var32 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var33 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.debugCamEnable) {
            GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, var3, 0.0F);
        var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)par1;
        var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)par1 - (double)var3;
        var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)par1;
        this.cloudFog = this.mc.renderGlobal.func_72721_a(var4, var6, var8, par1);
    }

    // Pattern D (@Overwrite): incorporates hunks 5, 6 Part A, 6 Part B, and 7 (dispatchRenderLast)
    // in one pass — the cast-removal in Part B and the two onDrawBlockHighlight guards together make
    // surgical injection across all four changes more fragile than a single replacement.
    // Logic delta: drawBlockDamageTexture guarded by instanceof EntityPlayer until RenderGlobal gains
    // the EntityLiving overload; dispatchRenderLast from hunk 7 is pre-included here.
    /**
     * @author FabricCompatibilityLayers
     * @reason Adds ForgeHooksClient.onDrawBlockHighlight guards around both outline draw sites,
     *         removes unsafe EntityPlayer cast on drawBlockDamageTexture, and dispatches
     *         the RenderWorldLastEvent via ForgeHooksClient.dispatchRenderLast.
     */
    @Overwrite
    public void renderWorld(float par1, long par2) {
        this.mc.mcProfiler.startSection("lightTex");
        if (this.lightmapUpdateNeeded) {
            this.updateLightmap(par1);
        }

        GL11.glEnable(2884);
        GL11.glEnable(2929);
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        this.mc.mcProfiler.endStartSection("pick");
        this.getMouseOver(par1);
        EntityLiving var4 = this.mc.renderViewEntity;
        RenderGlobal var5 = this.mc.renderGlobal;
        EffectRenderer var6 = this.mc.effectRenderer;
        double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)par1;
        double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)par1;
        double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)par1;
        this.mc.mcProfiler.endStartSection("center");

        for(int var13 = 0; var13 < 2; ++var13) {
            if (this.mc.gameSettings.anaglyph) {
                anaglyphField = var13;
                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            this.mc.mcProfiler.endStartSection("clear");
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(par1);
            GL11.glClear(16640);
            GL11.glEnable(2884);
            this.mc.mcProfiler.endStartSection("camera");
            this.setupCameraTransform(par1, var13);
            ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
            this.mc.mcProfiler.endStartSection("frustrum");
            ClippingHelperImpl.getInstance();
            if (this.mc.gameSettings.renderDistance < 2) {
                this.setupFog(-1, par1);
                this.mc.mcProfiler.endStartSection("sky");
                var5.renderSky(par1);
            }

            GL11.glEnable(2912);
            this.setupFog(1, par1);
            if (this.mc.gameSettings.ambientOcclusion) {
                GL11.glShadeModel(7425);
            }

            this.mc.mcProfiler.endStartSection("culling");
            Frustrum var14 = new Frustrum();
            var14.setPosition(var7, var9, var11);
            this.mc.renderGlobal.clipRenderersByFrustum(var14, par1);
            if (var13 == 0) {
                this.mc.mcProfiler.endStartSection("updatechunks");

                while(!this.mc.renderGlobal.updateRenderers(var4, false) && par2 != 0L) {
                    long var15 = par2 - System.nanoTime();
                    if (var15 < 0L || var15 > 1000000000L) {
                        break;
                    }
                }
            }

            if (var4.posY < (double)128.0F) {
                this.func_82829_a(var5, par1);
            }

            this.setupFog(0, par1);
            GL11.glEnable(2912);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("terrain");
            var5.sortAndRender(var4, 0, (double)par1);
            GL11.glShadeModel(7424);
            if (this.debugViewDirection == 0) {
                RenderHelper.enableStandardItemLighting();
                this.mc.mcProfiler.endStartSection("entities");
                var5.renderEntities(var4.getPosition(par1), var14, par1);
                this.enableLightmap((double)par1);
                this.mc.mcProfiler.endStartSection("litParticles");
                var6.renderLitParticles(var4, par1);
                RenderHelper.disableStandardItemLighting();
                this.setupFog(0, par1);
                this.mc.mcProfiler.endStartSection("particles");
                var6.renderParticles(var4, par1);
                this.disableLightmap((double)par1);
                if (this.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.water) && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI) {
                    EntityPlayer var17 = (EntityPlayer)var4;
                    GL11.glDisable(3008);
                    this.mc.mcProfiler.endStartSection("outline");
                    if (!ForgeHooksClient.onDrawBlockHighlight(var5, var17, mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1)) {
                        var5.drawBlockBreaking(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
                        var5.drawSelectionBox(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
                    }
                    GL11.glEnable(3008);
                }
            }

            GL11.glDisable(3042);
            GL11.glEnable(2884);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthMask(true);
            this.setupFog(0, par1);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
            if (this.mc.gameSettings.fancyGraphics) {
                this.mc.mcProfiler.endStartSection("water");
                if (this.mc.gameSettings.ambientOcclusion) {
                    GL11.glShadeModel(7425);
                }

                GL11.glColorMask(false, false, false, false);
                int var18 = var5.sortAndRender(var4, 1, (double)par1);
                if (this.mc.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                if (var18 > 0) {
                    var5.renderAllRenderLists(1, (double)par1);
                }

                GL11.glShadeModel(7424);
            } else {
                this.mc.mcProfiler.endStartSection("water");
                var5.sortAndRender(var4, 1, (double)par1);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            if (this.cameraZoom == (double)1.0F && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI && this.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.water)) {
                EntityPlayer var19 = (EntityPlayer)var4;
                GL11.glDisable(3008);
                this.mc.mcProfiler.endStartSection("outline");
                if (!ForgeHooksClient.onDrawBlockHighlight(var5, var19, mc.objectMouseOver, 0, var19.inventory.getCurrentItem(), par1)) {
                    var5.drawBlockBreaking(var19, this.mc.objectMouseOver, 0, var19.inventory.getCurrentItem(), par1);
                    var5.drawSelectionBox(var19, this.mc.objectMouseOver, 0, var19.inventory.getCurrentItem(), par1);
                }
                GL11.glEnable(3008);
            }

            this.mc.mcProfiler.endStartSection("destroyProgress");
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            ((RenderGlobalExtension) var5).drawBlockDamageTexture(Tessellator.instance, var4, par1);
            GL11.glDisable(3042);
            this.mc.mcProfiler.endStartSection("weather");
            this.renderRainSnow(par1);
            GL11.glDisable(2912);
            if (var4.posY >= (double)128.0F) {
                this.func_82829_a(var5, par1);
            }

            this.mc.mcProfiler.endStartSection("FRenderLast");
            ForgeHooksClient.dispatchRenderLast(var5, par1);

            this.mc.mcProfiler.endStartSection("hand");
            if (this.cameraZoom == (double)1.0F) {
                GL11.glClear(256);
                this.renderHand(par1, var13);
            }

            if (!this.mc.gameSettings.anaglyph) {
                this.mc.mcProfiler.endSection();
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
        this.mc.mcProfiler.endSection();
    }
}