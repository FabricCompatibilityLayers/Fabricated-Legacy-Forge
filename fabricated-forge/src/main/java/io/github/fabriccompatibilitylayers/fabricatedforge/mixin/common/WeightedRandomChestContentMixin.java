/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import fr.catcore.cursedmixinextensions.annotations.NewConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import net.minecraft.src.*;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WeightedRandomChestContent.class)
public abstract class WeightedRandomChestContentMixin extends WeightedRandomItem {
    @Shadow private int theMinimumChanceToGenerateItem;

    @Shadow private int theMaximumChanceToGenerateItem;

    public WeightedRandomChestContentMixin(int par1) {
        super(par1);
    }

    public ItemStack itemStack;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void forge$setItemStack(int par1, int par2, int par3, int par4, int par5, CallbackInfo ci) {
        itemStack = new ItemStack(par1, 1, par2);
    }

    @ShadowSuperConstructor
    public abstract void superConstructor(int weight);

    @NewConstructor
    public void constructor(ItemStack stack, int min, int max, int weight)
    {
        superConstructor(weight);
        itemStack = stack;
        theMinimumChanceToGenerateItem = min;
        theMaximumChanceToGenerateItem = max;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void generateChestContents(
            Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityChest par2TileEntityChest, int par3
    ) {
        for (int var4 = 0; var4 < par3; var4++) {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent) WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, ((WeightedRandomChestContentMixin) (Object) var5).itemStack, ((WeightedRandomChestContentMixin) (Object) var5).theMinimumChanceToGenerateItem, ((WeightedRandomChestContentMixin) (Object) var5).theMinimumChanceToGenerateItem);

            for (ItemStack item : stacks)
            {
                par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), item);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void generateDispenserContents(
            Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityDispenser par2TileEntityDispenser, int par3
    ) {
        for (int var4 = 0; var4 < par3; var4++) {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, ((WeightedRandomChestContentMixin) (Object) var5).itemStack, ((WeightedRandomChestContentMixin) (Object) var5).theMinimumChanceToGenerateItem, ((WeightedRandomChestContentMixin) (Object) var5).theMinimumChanceToGenerateItem);

            for (ItemStack item : stacks)
            {
                par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), item);
            }
        }
    }
}
