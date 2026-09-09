/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.FurnaceRecipesExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ItemData;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(FurnaceRecipes.class)
public class FurnaceRecipesMixin implements FurnaceRecipesExtension {
    @Shadow private Map<Integer, ItemStack> smeltingList;


    private Map<ItemData, ItemStack> metaSmeltingList = new HashMap<>();

    /**
     * Add a metadata-sensitive furnace recipe
     * @param itemID The Item ID
     * @param metadata The Item Metadata
     * @param itemstack The ItemStack for the result
     */
    @Override
    public void addSmelting(int itemID, int metadata, ItemStack itemstack)
    {
        metaSmeltingList.put(new ItemData(itemID, metadata), itemstack);
    }

    /**
     * Used to get the resulting ItemStack form a source ItemStack
     * @param item The Source ItemStack
     * @return The result ItemStack
     */
    @Override
    public ItemStack getSmeltingResult(ItemStack item)
    {
        if (item == null)
        {
            return null;
        }
        ItemStack ret = metaSmeltingList.get(new ItemData(item.getItem(), item.getItemDamage()));
        if (ret != null)
        {
            return ret;
        }
        return smeltingList.get(Integer.valueOf(item.itemID));
    }
}
