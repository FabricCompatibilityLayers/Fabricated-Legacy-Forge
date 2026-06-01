/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Packet.class)
public class PacketMixin {
    @WrapOperation(method = "<clinit>", slice = {
            @Slice(from = @At(value = "CONSTANT", args = "intValue=131")),
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Packet;func_73285_a(IZZLjava/lang/Class;)V", ordinal = 0))
    private static void fml$mapDataPacket(int p_73285_0_, boolean p_73285_1_, boolean p_73285_2_, Class p_73285_3_, Operation<Void> original) {
        original.call(p_73285_0_, p_73285_1_, true, p_73285_3_);
    }
}
