/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class LiquidContainerRegistry {
    public static final int BUCKET_VOLUME = 1000;
    public static final ItemStack EMPTY_BUCKET = new ItemStack(Item.BUCKET);
    private static Map<List, LiquidContainerData> mapFilledItemFromLiquid = new HashMap();
    private static Map<List, LiquidContainerData> mapLiquidFromFilledItem = new HashMap();
    private static Set<List> setContainerValidation = new HashSet();
    private static Set<List> setLiquidValidation = new HashSet();
    private static ArrayList<LiquidContainerData> liquids = new ArrayList();

    public LiquidContainerRegistry() {
    }

    public static void registerLiquid(LiquidContainerData data) {
        mapFilledItemFromLiquid.put(Arrays.asList(data.container.id, data.container.getData(), data.stillLiquid.itemID, data.stillLiquid.itemMeta), data);
        mapLiquidFromFilledItem.put(Arrays.asList(data.filled.id, data.filled.getData()), data);
        setContainerValidation.add(Arrays.asList(data.container.id, data.container.getData()));
        setLiquidValidation.add(Arrays.asList(data.stillLiquid.itemID, data.stillLiquid.itemMeta));
        liquids.add(data);
    }

    public static LiquidStack getLiquidForFilledItem(ItemStack filledContainer) {
        if (filledContainer == null) {
            return null;
        } else {
            LiquidContainerData ret = (LiquidContainerData)mapLiquidFromFilledItem.get(Arrays.asList(filledContainer.id, filledContainer.getData()));
            return ret != null ? ret.stillLiquid.copy() : null;
        }
    }

    public static ItemStack fillLiquidContainer(LiquidStack liquid, ItemStack emptyContainer) {
        if (emptyContainer != null && liquid != null) {
            LiquidContainerData ret = (LiquidContainerData)mapFilledItemFromLiquid.get(
                    Arrays.asList(emptyContainer.id, emptyContainer.getData(), liquid.itemID, liquid.itemMeta)
            );
            return ret != null && liquid.amount >= ret.stillLiquid.amount ? ret.filled.copy() : null;
        } else {
            return null;
        }
    }

    public static boolean containsLiquid(ItemStack filledContainer, LiquidStack liquid) {
        if (filledContainer != null && liquid != null) {
            LiquidContainerData ret = (LiquidContainerData)mapLiquidFromFilledItem.get(Arrays.asList(filledContainer.id, filledContainer.getData()));
            return ret != null ? ret.stillLiquid.isLiquidEqual(liquid) : false;
        } else {
            return false;
        }
    }

    public static boolean isBucket(ItemStack container) {
        if (container == null) {
            return false;
        } else if (container.equalsIgnoreNbt(EMPTY_BUCKET)) {
            return true;
        } else {
            LiquidContainerData ret = (LiquidContainerData)mapLiquidFromFilledItem.get(Arrays.asList(container.id, container.getData()));
            return ret != null ? ret.container.equalsIgnoreNbt(EMPTY_BUCKET) : false;
        }
    }

    public static boolean isContainer(ItemStack container) {
        return isEmptyContainer(container) || isFilledContainer(container);
    }

    public static boolean isEmptyContainer(ItemStack emptyContainer) {
        return emptyContainer == null ? false : setContainerValidation.contains(Arrays.asList(emptyContainer.id, emptyContainer.getData()));
    }

    public static boolean isFilledContainer(ItemStack filledContainer) {
        if (filledContainer == null) {
            return false;
        } else {
            return getLiquidForFilledItem(filledContainer) != null;
        }
    }

    public static boolean isLiquid(ItemStack item) {
        return item == null ? false : setLiquidValidation.contains(Arrays.asList(item.id, item.getData()));
    }

    public static LiquidContainerData[] getRegisteredLiquidContainerData() {
        return (LiquidContainerData[])liquids.toArray(new LiquidContainerData[0]);
    }

    static {
        registerLiquid(new LiquidContainerData(new LiquidStack(Block.WATER, 1000), new ItemStack(Item.WATER_BUCKET), new ItemStack(Item.BUCKET)));
        registerLiquid(new LiquidContainerData(new LiquidStack(Block.LAVA, 1000), new ItemStack(Item.LAVA_BUCKET), new ItemStack(Item.BUCKET)));
        registerLiquid(new LiquidContainerData(new LiquidStack(Block.WATER, 1000), new ItemStack(Item.POTION), new ItemStack(Item.GLASS_BOTTLE)));
    }
}
