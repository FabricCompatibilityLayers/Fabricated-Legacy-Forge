package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface IWeightedRandomChestContent {
    ItemStack[] generateChestContent(Random random, Inventory newInventory);
}
