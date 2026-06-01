/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockPistonBase.class)
public abstract class BlockPistonBaseMixin extends Block implements BlockExtension {
    public BlockPistonBaseMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @ModifyReturnValue(method = "canPushBlock", at = @At(value = "RETURN", ordinal = 5))
    private static boolean forge$blockHasTileEntity(boolean original,
                                                    @Local(argsOnly = true) World par1World,
                                                    @Local(argsOnly = true, ordinal = 0) int par2,
                                                    @Local(argsOnly = true, ordinal = 1) int par3,
                                                    @Local(argsOnly = true, ordinal = 2) int par4) {
        return !par1World.blockHasTileEntity(par2, par3, par4) || original;
    }
}
