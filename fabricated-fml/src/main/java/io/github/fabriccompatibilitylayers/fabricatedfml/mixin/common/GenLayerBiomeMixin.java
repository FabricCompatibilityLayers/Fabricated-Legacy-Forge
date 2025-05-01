package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.WorldTypeExtension;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.GenLayer;
import net.minecraft.src.GenLayerBiome;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenLayerBiome.class)
public class GenLayerBiomeMixin {
    @WrapOperation(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GenLayerBiome;field_75914_b:[Lnet/minecraft/src/BiomeGenBase;", ordinal = 0))
    private void fml$getBiomesForWorldType(GenLayerBiome instance, BiomeGenBase[] value, Operation<Void> original, @Local(argsOnly = true) WorldType p_i3888_4_) {
        original.call(instance, ((WorldTypeExtension) p_i3888_4_).getBiomesForWorldType());
    }

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/src/WorldType;field_77136_e:Lnet/minecraft/src/WorldType;"), cancellable = true)
    private void fml$cancelRemaining(long p_i3888_3_, GenLayer p_i3888_4_, WorldType par3, CallbackInfo ci) {
        ci.cancel();
    }
}
