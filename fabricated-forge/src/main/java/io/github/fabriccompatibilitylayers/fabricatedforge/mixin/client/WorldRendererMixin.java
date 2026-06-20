/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow private int glRenderList;
    @Shadow private List tileEntities;
    @Shadow private int bytesDrawn;
    @Shadow private boolean isInitialized;
    @Shadow private void setupGLTranslation() {}

    @Shadow
    public boolean needsUpdate;

    @Shadow
    public int posX;

    @Shadow
    public int posY;

    @Shadow
    public int posZ;

    @Shadow
    public boolean[] skipRenderPass;

    @Shadow
    public List tileEntityRenderers;

    @Shadow
    public World worldObj;

    @Shadow
    public static int chunksUpdated;

    @Shadow
    public boolean isChunkLit;


    // Pattern D (@Overwrite): replaces the entire updateRenderer() body. Chosen because
    // changes span five distinct points inside a triple-nested loop (beforeRenderPass,
    // hasTileEntity(meta), condition var24>var11, canRenderInPass+continue guard,
    // before/afterBlockRender, afterRenderPass) — surgical patterns cannot express the
    // `continue` guard without a full rewrite. Covers both patch hunks 3 and 4.
    // Logic delta: render pass gate changes from "!= var11" to "> var11" (multi-pass
    // permissiveness); the else-if block is replaced by a canRenderInPass continue-guard,
    // making each block individually decide eligibility rather than the pass index alone.
    /**
     * @author FabricCompatibilityLayers
     * @reason Adds Forge multi-pass rendering hooks (beforeRenderPass, afterRenderPass,
     * beforeBlockRender, afterBlockRender), canRenderInPass guard, and hasTileEntity(meta) overload.
     */
    @Overwrite
    public void updateRenderer() {
        if (this.needsUpdate) {
            this.needsUpdate = false;
            int var1 = this.posX;
            int var2 = this.posY;
            int var3 = this.posZ;
            int var4 = this.posX + 16;
            int var5 = this.posY + 16;
            int var6 = this.posZ + 16;

            for(int var7 = 0; var7 < 2; ++var7) {
                this.skipRenderPass[var7] = true;
            }

            Chunk.isLit = false;
            HashSet var21 = new HashSet();
            var21.addAll(this.tileEntityRenderers);
            this.tileEntityRenderers.clear();
            byte var8 = 1;
            ChunkCache var9 = new ChunkCache(this.worldObj, var1 - var8, var2 - var8, var3 - var8, var4 + var8, var5 + var8, var6 + var8);
            if (!var9.extendedLevelsInChunkCache()) {
                ++chunksUpdated;
                RenderBlocks var10 = new RenderBlocks(var9);
                this.bytesDrawn = 0;

                for(int var11 = 0; var11 < 2; ++var11) {
                    boolean var12 = false;
                    boolean var13 = false;
                    boolean var14 = false;

                    for(int var15 = var2; var15 < var5; ++var15) {
                        for(int var16 = var3; var16 < var6; ++var16) {
                            for(int var17 = var1; var17 < var4; ++var17) {
                                int var18 = var9.getBlockId(var17, var15, var16);
                                if (var18 > 0) {
                                    if (!var14) {
                                        var14 = true;
                                        GL11.glNewList(this.glRenderList + var11, 4864);
                                        GL11.glPushMatrix();
                                        this.setupGLTranslation();
                                        float var19 = 1.000001F;
                                        GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
                                        GL11.glScalef(var19, var19, var19);
                                        GL11.glTranslatef(8.0F, 8.0F, 8.0F);
                                        ForgeHooksClient.beforeRenderPass(var11);
                                        Tessellator.instance.startDrawingQuads();
                                        Tessellator.instance.setTranslation((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
                                    }

                                    Block var23 = Block.blocksList[var18];
                                    if (var23 != null) {
                                        if (var11 == 0 && ((BlockExtension) var23).hasTileEntity(var9.getBlockMetadata(var17, var15, var16))) {
                                            TileEntity var20 = var9.getBlockTileEntity(var17, var15, var16);
                                            if (TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
                                                this.tileEntityRenderers.add(var20);
                                            }
                                        }

                                        int var24 = var23.getRenderBlockPass();
                                        if (var24 > var11) {
                                            var12 = true;
                                        }

                                        if (!((BlockExtension) var23).canRenderInPass(var11)) {
                                            continue;
                                        }

                                        ForgeHooksClient.beforeBlockRender(var23, var10);
                                        var13 |= var10.renderBlockByRenderType(var23, var17, var15, var16);
                                        ForgeHooksClient.afterBlockRender(var23, var10);
                                    }
                                }
                            }
                        }
                    }

                    if (var14) {
                        ForgeHooksClient.afterRenderPass(var11);
                        this.bytesDrawn += Tessellator.instance.draw();
                        GL11.glPopMatrix();
                        GL11.glEndList();
                        Tessellator.instance.setTranslation((double)0.0F, (double)0.0F, (double)0.0F);
                    } else {
                        var13 = false;
                    }

                    if (var13) {
                        this.skipRenderPass[var11] = false;
                    }

                    if (!var12) {
                        break;
                    }
                }
            }

            HashSet var22 = new HashSet();
            var22.addAll(this.tileEntityRenderers);
            var22.removeAll(var21);
            this.tileEntities.addAll(var22);
            var21.removeAll(this.tileEntityRenderers);
            this.tileEntities.removeAll(var21);
            this.isChunkLit = Chunk.isLit;
            this.isInitialized = true;
        }
    }
}