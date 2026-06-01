/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(Packet1Login.class)
public class Packet1LoginMixin {

    @Shadow public int field_73558_e;

    private boolean vanillaCompatible;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void fml$init(CallbackInfo ci) {
        this.vanillaCompatible = FMLNetworkHandler.vanillaLoginPacketCompatibility();
    }

    @Inject(method = "<init>(ILnet/minecraft/src/WorldType;Lnet/minecraft/src/EnumGameType;ZIIII)V", at = @At("RETURN"))
    private void fml$init(int p_i3327_2_, WorldType p_i3327_3_, EnumGameType p_i3327_4_, boolean p_i3327_5_, int p_i3327_6_, int p_i3327_7_, int p_i3327_8_, int par8, CallbackInfo ci) {
        this.vanillaCompatible = false;
    }

    @Redirect(method = "func_73267_a", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readByte()B", ordinal = 1, remap = false))
    private byte fml$redirectOriginalCall(DataInputStream instance) {
        return -1;
    }

    @Inject(method = "func_73267_a", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Packet1Login;field_73555_f:B"))
    private void fml$readDimensionId(DataInputStream p_73267_1_, CallbackInfo ci) throws IOException {
        if (this.vanillaCompatible) {
            this.field_73558_e = p_73267_1_.readByte();
        } else {
            this.field_73558_e = p_73267_1_.readInt();
        }
    }

    @WrapOperation(method = "func_73273_a", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeByte(I)V", ordinal = 1, remap = false))
    private void fml$writeDimensionId(DataOutputStream instance, int v, Operation<Void> original) throws IOException {
        if (this.vanillaCompatible) {
            original.call(instance, v);
        } else {
            instance.writeInt(this.field_73558_e);
        }
    }

    @ModifyReturnValue(method = "func_73284_a", at = @At("RETURN"))
    private int fml$addCompatibilityFlag(int original) {
        return original + (vanillaCompatible ? 0 : 3);
    }
}
