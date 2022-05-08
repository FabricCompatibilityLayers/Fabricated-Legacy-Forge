package net.minecraftforge.client;

import net.minecraft.block.Block;
import net.minecraft.client.class_535;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MinecraftForgeClient {
    private static net.minecraft.net.minecraftforge.client.IItemRenderer[] customItemRenderers;

    public MinecraftForgeClient() {
    }

    public static void registerRenderContextHandler(String texture, int subid, IRenderContextHandler handler) {
        net.minecraft.net.minecraftforge.client.ForgeHooksClient.registerRenderContextHandler(texture, subid, handler);
    }

    public static void preloadTexture(String texture) {
        net.minecraft.net.minecraftforge.client.ForgeHooksClient.engine().getTextureFromPath(texture);
    }

    public static void renderBlock(class_535 render, Block block, int x, int y, int z) {
        net.minecraft.net.minecraftforge.client.ForgeHooksClient.beforeBlockRender(block, render);
        render.method_1458(block, x, y, z);
        net.minecraft.net.minecraftforge.client.ForgeHooksClient.afterBlockRender(block, render);
    }

    public static int getRenderPass() {
        return ForgeHooksClient.renderPass;
    }

    public static void registerItemRenderer(int itemID, net.minecraft.net.minecraftforge.client.IItemRenderer renderer) {
        customItemRenderers[itemID] = renderer;
    }

    public static net.minecraft.net.minecraftforge.client.IItemRenderer getItemRenderer(ItemStack item, net.minecraft.net.minecraftforge.client.IItemRenderer.ItemRenderType type) {
        net.minecraft.net.minecraftforge.client.IItemRenderer renderer = customItemRenderers[item.id];
        return renderer != null && renderer.handleRenderType(item, type) ? customItemRenderers[item.id] : null;
    }

    static {
        customItemRenderers = new IItemRenderer[Item.ITEMS.length];
    }
}
