/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
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
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(BlockSnow.class)
public abstract class BlockSnowMixin extends Block implements BlockExtension {
    public BlockSnowMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Expression("? != 0")
    @WrapOperation(method = "canPlaceBlockAt", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isNotNull(int left, int right, Operation<Boolean> original) {
        return original.call(left, right) && Block.blocksList[left] != null;
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "canPlaceBlockAt", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isLeaves(int left, int right, Operation<Boolean> original,
                                   @Local(argsOnly = true) World par1World,
                                   @Local(argsOnly = true, ordinal = 0) int par2,
                                   @Local(argsOnly = true, ordinal = 1) int par3,
                                   @Local(argsOnly = true, ordinal = 2) int par4) {
        return ((BlockExtension) Block.blocksList[left]).isLeaves(par1World, par2, par3 - 1, par4);
    }

    @Redirect(method = {"canSnowStay", "updateTick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockSnow;dropBlockAsItem(Lnet/minecraft/src/World;IIIII)V"))
    private void forge$cancelDropBlockAsItem(BlockSnow instance, World world, int i, int j, int k, int l, int m) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
        par1World.setBlockWithNotify(par3, par4, par5, 0);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
}
