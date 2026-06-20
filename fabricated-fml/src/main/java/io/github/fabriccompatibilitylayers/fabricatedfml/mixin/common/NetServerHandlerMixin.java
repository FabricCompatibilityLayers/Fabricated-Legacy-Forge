/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMixin extends NetHandler implements NetHandlerExtension {
    @Shadow private EntityPlayerMP field_72574_e;

    @Shadow public INetworkManager field_72575_b;

    @Inject(method = "func_72481_a", at = @At("HEAD"))
    private void fml$handleChatMessage(Packet3Chat p_72481_1_, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet3Chat> ref) {
        ref.set(FMLNetworkHandler.handleChatMessage(this, p_72481_1_));
    }

    private boolean handleVanilla250Packet = false;

    @WrapMethod(method = "func_72501_a")
    private void fml$handlePacket250Packet(Packet250CustomPayload p_72501_1_, Operation<Void> original) {
        if (handleVanilla250Packet) {
            handleVanilla250Packet = false;
            original.call(p_72501_1_);
        } else {
            FMLNetworkHandler.handlePacket250Packet(p_72501_1_, field_72575_b, this);
        }
    }

    @Override
    public void handleVanilla250Packet(Packet250CustomPayload p_72501_1_) {
        handleVanilla250Packet = true;
        func_72501_a(p_72501_1_);
    }

    @Override
    public void func_72494_a(Packet131MapData p_72494_1_)
    {
        FMLNetworkHandler.handlePacket131Packet(this, p_72494_1_);
    }

    // modloader compat -- yuk!
    @Override
    public EntityPlayerMP getPlayer()
    {
        return field_72574_e;
    }
}
