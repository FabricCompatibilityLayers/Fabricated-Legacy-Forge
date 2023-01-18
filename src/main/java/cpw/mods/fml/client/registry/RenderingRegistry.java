/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.client.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import cpw.mods.fml.client.SpriteHelper;
import cpw.mods.fml.client.TextureFXManager;
import net.minecraft.block.Block;
import net.minecraft.client.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

import java.util.List;
import java.util.Map;

public class RenderingRegistry {
    private static final RenderingRegistry INSTANCE = new RenderingRegistry();
    private int nextRenderId = 32;
    private Map<Integer, ISimpleBlockRenderingHandler> blockRenderers = Maps.newHashMap();
    private List<RenderingRegistry.EntityRendererInfo> entityRenderers = Lists.newArrayList();

    public RenderingRegistry() {
    }

    public static int addNewArmourRendererPrefix(String armor) {
        PlayerEntityRenderer.field_2136 = (String[]) ObjectArrays.concat(PlayerEntityRenderer.field_2136, armor);
        return PlayerEntityRenderer.field_2136.length - 1;
    }

    public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, EntityRenderer renderer) {
        instance().entityRenderers.add(new RenderingRegistry.EntityRendererInfo(entityClass, renderer));
    }

    public static void registerBlockHandler(ISimpleBlockRenderingHandler handler) {
        instance().blockRenderers.put(handler.getRenderId(), handler);
    }

    public static void registerBlockHandler(int renderId, ISimpleBlockRenderingHandler handler) {
        instance().blockRenderers.put(renderId, handler);
    }

    public static int getNextAvailableRenderId() {
        return instance().nextRenderId++;
    }

    public static int addTextureOverride(String fileToOverride, String fileToAdd) {
        int idx = SpriteHelper.getUniqueSpriteIndex(fileToOverride);
        addTextureOverride(fileToOverride, fileToAdd, idx);
        return idx;
    }

    public static void addTextureOverride(String path, String overlayPath, int index) {
        TextureFXManager.instance().addNewTextureOverride(path, overlayPath, index);
    }

    public static int getUniqueTextureIndex(String path) {
        return SpriteHelper.getUniqueSpriteIndex(path);
    }

    /** @deprecated */
    @Deprecated
    public static RenderingRegistry instance() {
        return INSTANCE;
    }

    public boolean renderWorldBlock(BlockRenderer renderer, BlockView world, int x, int y, int z, Block block, int modelId) {
        if (!this.blockRenderers.containsKey(modelId)) {
            return false;
        } else {
            ISimpleBlockRenderingHandler bri = (ISimpleBlockRenderingHandler)this.blockRenderers.get(modelId);
            return bri.renderWorldBlock(world, x, y, z, block, modelId, renderer);
        }
    }

    public void renderInventoryBlock(BlockRenderer renderer, Block block, int metadata, int modelID) {
        if (this.blockRenderers.containsKey(modelID)) {
            ISimpleBlockRenderingHandler bri = (ISimpleBlockRenderingHandler)this.blockRenderers.get(modelID);
            bri.renderInventoryBlock(block, metadata, modelID, renderer);
        }
    }

    public boolean renderItemAsFull3DBlock(int modelId) {
        ISimpleBlockRenderingHandler bri = (ISimpleBlockRenderingHandler)this.blockRenderers.get(modelId);
        return bri != null && bri.shouldRender3DInInventory();
    }

    public void loadEntityRenderers(Map<Class<? extends Entity>, EntityRenderer> rendererMap) {
        for (EntityRendererInfo info : entityRenderers)
        {
            rendererMap.put(info.target, info.renderer);
            info.renderer.setRenderDispatcher(EntityRenderDispatcher.INSTANCE);
        }
    }

    private static class EntityRendererInfo {
        private Class<? extends Entity> target;
        private EntityRenderer renderer;

        public EntityRendererInfo(Class<? extends Entity> target, EntityRenderer renderer) {
            this.target = target;
            this.renderer = renderer;
        }
    }
}
