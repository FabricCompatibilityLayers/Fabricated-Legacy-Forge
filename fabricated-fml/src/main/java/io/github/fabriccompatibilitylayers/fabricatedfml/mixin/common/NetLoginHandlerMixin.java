/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetLoginHandlerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.SocketAddress;

@Mixin(NetLoginHandler.class)
public abstract class NetLoginHandlerMixin extends NetHandler implements NetLoginHandlerExtension {
    @Shadow private MinecraftServer field_72534_f;

    @Shadow public String field_72543_h;

    @Shadow public TcpConnection field_72538_b;

    @Shadow
    public abstract void func_72529_d();

    @ModifyConstant(method = "func_72532_c", constant = @Constant(intValue = 600))
    private int fml$increaseThreshold(int constant) {
        return 6000;
    }

    @Inject(method = "func_72455_a", at = @At("RETURN"))
    private void fml$handleLoginPacketOnServer(Packet1Login p_72455_1_, CallbackInfo ci) {
        FMLNetworkHandler.handleLoginPacketOnServer((NetLoginHandler) (Object) this, p_72455_1_);
    }

    private boolean completeConnection = false;
    private String completeConnectionReason;

    @WrapMethod(method = "func_72529_d")
    private void forge$onConnectionReceivedFromClient(Operation<Void> original) {
        if (completeConnection) {
            original.call();
            completeConnection = false;
            completeConnectionReason = null;
        } else {
            FMLNetworkHandler.onConnectionReceivedFromClient((NetLoginHandler) (Object) this, this.field_72534_f, this.field_72538_b.func_74430_c(), this.field_72543_h);
        }
    }

    @Override
    public void completeConnection(String var1) {
        completeConnection = true;
        completeConnectionReason = var1;
        func_72529_d();
    }

    @WrapOperation(method = "func_72529_d", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerConfigurationManager;func_72399_a(Ljava/net/SocketAddress;Ljava/lang/String;)Ljava/lang/String;"))
    private String fml$replaceReason(ServerConfigurationManager instance, SocketAddress socketAddress, String s, Operation<String> original) {
        if (completeConnection) {
            return completeConnectionReason;
        }

        return original.call(instance, socketAddress, s);
    }

    @Override
    public void func_72501_a(Packet250CustomPayload p_72501_1_)
    {
        FMLNetworkHandler.handlePacket250Packet(p_72501_1_, field_72538_b, this);
    }

    @Override
    public void handleVanilla250Packet(Packet250CustomPayload payload)
    {
        // NOOP for login
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return null;
    };
}
