/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRedstoneWire.class)
public abstract class BlockRedstoneWireMixin extends Block implements BlockExtension {
    public BlockRedstoneWireMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Redirect(method = "isPowerProviderOrWire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;canProvidePower()Z"))
    private static boolean forge$canConnectRedstone(Block instance,
                                                    @Local(argsOnly = true) IBlockAccess par0IBlockAccess,
                                                    @Local(argsOnly = true, ordinal = 0) int par1,
                                                    @Local(argsOnly = true, ordinal = 1) int par2,
                                                    @Local(argsOnly = true, ordinal = 2) int par3,
                                                    @Local(argsOnly = true, ordinal = 3) int par4) {
        return instance != null && ((BlockExtension) instance).canConnectRedstone(par0IBlockAccess, par1, par2, par3, par4);
    }

    @ModifyConstant(method = "isPowerProviderOrWire", constant = @Constant(intValue = -1))
    private static int forge$hackCheck(int constant) {
        return -2;
    }
}
