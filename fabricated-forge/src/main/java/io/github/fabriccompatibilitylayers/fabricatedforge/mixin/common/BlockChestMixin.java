/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import net.minecraft.src.BlockChest;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockChest.class)
public class BlockChestMixin {
    @Redirect(method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"))
    private boolean forge$isBlockSolidOnSide(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, ForgeDirection.DOWN);
    }
}
