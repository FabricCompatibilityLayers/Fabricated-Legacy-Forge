/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.BlockRegistrationHelper;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Conditional(modLoaded = @Mod(value = "osl-blocks"))
@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension, net.ornithemc.osl.blocks.api.block.BlockExtension {
    @Inject(method = "<init>(ILnet/minecraft/src/Material;)V", at = @At("RETURN"))
    private void osl$registryFMLBlocks(int par1, Material par2Material, CallbackInfo ci) {
        if (BlockRegistrationHelper.ready) {
            ModContainer container = Loader.instance().activeModContainer();

            if (container != null) {
                BlockRegistrationHelper.registerBlock((Block) (Object) this, container);
            }
        }
    }

    @Override
    public boolean isAirBlock(World world, int x, int y, int z) {
        return this.isAir();
    }
}
