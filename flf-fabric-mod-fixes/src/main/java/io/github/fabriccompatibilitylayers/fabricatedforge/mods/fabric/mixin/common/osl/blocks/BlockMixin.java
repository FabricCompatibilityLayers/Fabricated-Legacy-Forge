/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blocks;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;

@Conditional(modLoaded = @Mod(value = "osl-blocks"))
@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension, net.ornithemc.osl.blocks.api.block.BlockExtension {
    @Override
    public boolean isAirBlock(World world, int x, int y, int z) {
        return this.isAir();
    }
}
