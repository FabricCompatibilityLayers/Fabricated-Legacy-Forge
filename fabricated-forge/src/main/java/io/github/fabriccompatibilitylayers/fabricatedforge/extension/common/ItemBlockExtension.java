package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface ItemBlockExtension extends ItemExtension {
    boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
}
