/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemSeeds;
import net.minecraft.src.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemSeeds.class)
public abstract class ItemSeedsMixin extends Item implements IPlantable, ItemExtension {
    @Shadow private int blockType;

    protected ItemSeedsMixin(int par1) {
        super(par1);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        return (blockType == Block.netherStalk.blockID ? EnumPlantType.Nether : EnumPlantType.Crop);
    }

    @Override
    public int getPlantID(World world, int x, int y, int z)
    {
        return blockType;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z)
    {
        return 0;
    }
}
