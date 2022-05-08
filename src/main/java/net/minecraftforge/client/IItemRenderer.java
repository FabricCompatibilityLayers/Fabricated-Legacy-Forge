package net.minecraftforge.client;

import net.minecraft.item.ItemStack;

public interface IItemRenderer {
    boolean handleRenderType(ItemStack arg, net.minecraft.net.minecraftforge.client.IItemRenderer.ItemRenderType itemRenderType);

    boolean shouldUseRenderHelper(net.minecraft.net.minecraftforge.client.IItemRenderer.ItemRenderType itemRenderType, ItemStack arg, net.minecraft.net.minecraftforge.client.IItemRenderer.ItemRendererHelper itemRendererHelper);

    void renderItem(net.minecraft.net.minecraftforge.client.IItemRenderer.ItemRenderType itemRenderType, ItemStack arg, Object... objects);

    public static enum ItemRendererHelper {
        ENTITY_ROTATION,
        ENTITY_BOBBING,
        EQUIPPED_BLOCK,
        BLOCK_3D,
        INVENTORY_BLOCK;

        private ItemRendererHelper() {
        }
    }

    public static enum ItemRenderType {
        ENTITY,
        EQUIPPED,
        INVENTORY,
        FIRST_PERSON_MAP;

        private ItemRenderType() {
        }
    }
}
