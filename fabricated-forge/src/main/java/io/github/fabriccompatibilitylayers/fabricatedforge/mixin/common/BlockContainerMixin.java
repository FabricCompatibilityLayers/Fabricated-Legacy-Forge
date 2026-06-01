/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockContainerExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockContainer.class)
public abstract class BlockContainerMixin extends Block implements BlockContainerExtension {
    @Shadow public abstract TileEntity createNewTileEntity(World par1World);

    public BlockContainerMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockContainer;createNewTileEntity(Lnet/minecraft/src/World;)Lnet/minecraft/src/TileEntity;"))
    private TileEntity forge$createTileEntity(BlockContainer instance, World world, @Local(argsOnly = true, ordinal = 0) int par2, @Local(argsOnly = true, ordinal = 1) int par3, @Local(argsOnly = true, ordinal = 2) int par4) {
        return createNewTileEntity(world, world.getBlockMetadata(par2, par3, par4));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return createNewTileEntity(world);
    }
}
