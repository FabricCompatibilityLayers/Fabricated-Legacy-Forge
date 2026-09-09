/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.BlockRegistrationHelper;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    @Final
    public int blockID;

    @Inject(method = "<init>(ILnet/minecraft/src/Material;)V", at = @At("RETURN"))
    private void osl$registryFMLBlocks(int par1, Material par2Material, CallbackInfo ci) {
        if (this.blockID == 0) return;

        if (BlockRegistrationHelper.ready) {
            ModContainer container = Loader.instance().activeModContainer();

            if (container != null) {
                BlockRegistrationHelper.registerBlock((Block) (Object) this, container);
            }
        }
    }
}
