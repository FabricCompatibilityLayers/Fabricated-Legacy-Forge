/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blocks;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.src.Block;
import net.minecraft.src.WorldGenForest;
import net.ornithemc.osl.blocks.api.block.BlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("osl-blocks")
@Mixin(WorldGenForest.class)
public class WorldGenForestMixin {
    @Expression("? != 0")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$NonNull(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] != null && !((BlockExtension) Block.blocksList[blockId]).isAir();
    }

    @Expression("? == 0")
    @WrapOperation(method = "generate", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$Null(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] == null || ((BlockExtension) Block.blocksList[blockId]).isAir();
    }
}
