/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client;

import net.minecraft.block.Block;
import net.minecraft.client.BlockRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MinecraftForgeClient {
    private static IItemRenderer[] customItemRenderers = new IItemRenderer[Item.ITEMS.length];

    public MinecraftForgeClient() {
    }

    public static void registerRenderContextHandler(String texture, int subid, IRenderContextHandler handler) {
        ForgeHooksClient.registerRenderContextHandler(texture, subid, handler);
    }

    public static void preloadTexture(String texture) {
        ForgeHooksClient.engine().getTextureFromPath(texture);
    }

    public static void renderBlock(BlockRenderer render, Block block, int x, int y, int z) {
        ForgeHooksClient.beforeBlockRender(block, render);
        render.render(block, x, y, z);
        ForgeHooksClient.afterBlockRender(block, render);
    }

    public static int getRenderPass() {
        return ForgeHooksClient.renderPass;
    }

    public static void registerItemRenderer(int itemID, IItemRenderer renderer) {
        customItemRenderers[itemID] = renderer;
    }

    public static IItemRenderer getItemRenderer(ItemStack item, IItemRenderer.ItemRenderType type) {
        IItemRenderer renderer = customItemRenderers[item.id];
        return renderer != null && renderer.handleRenderType(item, type) ? customItemRenderers[item.id] : null;
    }
}
