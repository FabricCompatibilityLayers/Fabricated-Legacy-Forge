/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockRailExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.RailLogic;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RailLogic.class)
public class RailLogicMixin {
    @Mutable
    @Shadow @Final private boolean isPoweredRail;

    private boolean canMakeSlopes;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockMetadata(III)I"))
    private int forge$getBasicRailMetadata(World par2World, int par3, int par4, int par5,
                                           @Local(ordinal = 3) int var6,
                                           @Share(value = "target", namespace = "fabricated-forge") LocalRef<BlockRail> targetRef) {
        BlockRail target = (BlockRail) Block.blocksList[var6];
        targetRef.set(target);
        return ((BlockRailExtension) target).getBasicRailMetadata(par2World, null, par3, par4, par5);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RailLogic;setConnections(I)V"))
    private void forge$isPoweredRailAndCanMakeSlopes(BlockRail par1BlockRail, World par2World, int par3, int par4, int par5, CallbackInfo ci,
                                                     @Share(value = "target", namespace = "fabricated-forge") LocalRef<BlockRail> targetRef) {
        isPoweredRail = !((BlockRailExtension) targetRef.get()).isFlexibleRail(par2World, par3, par4, par5);
        canMakeSlopes = ((BlockRailExtension) targetRef.get()).canMakeSlopes(par2World, par3, par4, par5);
    }

    @Definition(id = "var6", local = @Local(type = int.class, ordinal = 4))
    @Expression("var6 == ?")
    @ModifyExpressionValue(method = "connectToNeighbor", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canMakeSlopes(boolean original) {
        return original && canMakeSlopes;
    }

    @Definition(id = "var6", local = @Local(type = int.class, ordinal = 4))
    @Expression("var6 == ?")
    @WrapOperation(method = "refreshTrackShape", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canMakeSlopes(int left, int right, Operation<Boolean> original) {
        boolean originalValue = original.call(left, right);

        if (right == -1) return originalValue;

        return originalValue && canMakeSlopes;
    }
}
