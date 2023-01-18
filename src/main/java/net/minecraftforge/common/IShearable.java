/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public interface IShearable {
    boolean isShearable(ItemStack arg, World arg2, int i, int j, int k);

    ArrayList<ItemStack> onSheared(ItemStack arg, World arg2, int i, int j, int k, int l);
}
