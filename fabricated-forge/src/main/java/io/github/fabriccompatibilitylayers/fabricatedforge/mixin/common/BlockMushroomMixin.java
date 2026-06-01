/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.IPlantableBlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockMushroom.class)
public abstract class BlockMushroomMixin extends BlockFlower implements IPlantableBlockExtension {
    protected BlockMushroomMixin(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
    }

    @Redirect(method = "canBlockStay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockMushroom;canThisPlantGrowOnThisBlockID(I)Z"))
    private boolean forge$canSustainPlant(BlockMushroom instance, int i,
                                          @Local(argsOnly = true) World par1World,
                                          @Local(argsOnly = true, ordinal = 0) int par2,
                                          @Local(argsOnly = true, ordinal = 1) int par3,
                                          @Local(argsOnly = true, ordinal = 2) int par4,
                                          @Local(ordinal = 3) int var5) {
        Block soil = Block.blocksList[var5];

        return soil != null && ((BlockExtension) soil).canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this);
    }
}
