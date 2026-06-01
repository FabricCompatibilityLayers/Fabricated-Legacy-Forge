/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.Block;
import net.minecraft.src.EntityDiggingFX;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDiggingFX.class)
public class EntityDiggingFXMixin {

    // Sub-change A: new instance field tracking which block face spawned this particle
    private int side;

    // Sub-change B1 — Pattern E (@WrapOperation INVOKE): replaces the hardcoded 0 first-arg
    // with the actual par15 face value so the correct block face texture is selected.
    @WrapOperation(
        method = "<init>(Lnet/minecraft/src/World;DDDDDDLnet/minecraft/src/Block;II)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getBlockTextureFromSideAndMetadata(II)I")
    )
    private int forge$fixTextureIndex(Block instance, int side, int meta, Operation<Integer> original,
            @Local(argsOnly = true, ordinal = 0) int par15) {
        return original.call(instance, par15, meta);
    }

    // Sub-change B2 — Pattern A (@Inject RETURN): stores par15 in the side field
    // after the rest of constructor body has run.
    @Inject(
        method = "<init>(Lnet/minecraft/src/World;DDDDDDLnet/minecraft/src/Block;II)V",
        at = @At("RETURN")
    )
    private void forge$storeSide(World par1World, double par2, double par4, double par6,
            double par8, double par10, double par12, Block par14Block, int par15, int par16,
            CallbackInfo ci) {
        this.side = par15;
    }

    @Definition(id = "blockInstance", field = "Lnet/minecraft/src/EntityDiggingFX;blockInstance:Lnet/minecraft/src/Block;")
    @Definition(id = "grass", field = "Lnet/minecraft/src/Block;grass:Lnet/minecraft/src/BlockGrass;")
    @Expression("this.blockInstance == grass")
    @ModifyExpressionValue(
            method = "func_70596_a",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean forge$fixGrassCheckSide(boolean original) {
        return original && this.side == 1;
    }
}