/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFarmland;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockFarmland.class)
public abstract class BlockFarmlandMixin extends Block implements BlockExtension {
    public BlockFarmlandMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Definition(id = "crops", field = "Lnet/minecraft/src/Block;crops:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("? == crops.blockID")
    @WrapOperation(method = "isCropsNearby", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canSustainPlant(int left, int right, Operation<Boolean> original,
                                          @Local(argsOnly = true) World par1World,
                                          @Local(argsOnly = true, ordinal = 0) int par2,
                                          @Local(argsOnly = true, ordinal = 1) int par3,
                                          @Local(argsOnly = true, ordinal = 2) int par4
    ) {
        Block block = blocksList[right];
        return (block instanceof IPlantable && canSustainPlant(par1World, par2, par3, par4, ForgeDirection.UP, (IPlantable) block)) && original.call(left, right);
    }
}
