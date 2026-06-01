/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.MapDataExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemMap.class)
public class ItemMapMixin {
    @Inject(method = "func_77873_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/MapData;func_76185_a()V"))
    private void fml$writeDimensionId(ItemStack p_77873_1_, World p_77873_2_, CallbackInfoReturnable<MapData> cir, @Local MapData var4) {
        ((MapDataExtension) var4).setDimensionId(p_77873_2_.field_73011_w.field_76574_g);
    }

    @WrapOperation(method = "func_77872_a", at = @At(value = "FIELD", target = "Lnet/minecraft/src/WorldProvider;field_76574_g:I"))
    private int fml$readDimensionId(WorldProvider instance, Operation<Integer> original, @Local(argsOnly = true) MapData p_77872_3_) {
        if (original.call(instance) == ((MapDataExtension) p_77872_3_).getDimensionId()) {
            return p_77872_3_.field_76200_c;
        }

        return -1;
    }

    @Inject(method = "func_77622_d", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/MapData;func_76185_a()V"))
    private void fml$writeDimensionId(ItemStack p_77622_1_, World p_77622_2_, EntityPlayer p_77622_3_, CallbackInfo ci, @Local MapData var5) {
        ((MapDataExtension) var5).setDimensionId(p_77622_2_.field_73011_w.field_76574_g);
    }
}
