/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.NetClientHandlerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public abstract class NetClientHandlerMixin extends NetHandler implements NetClientHandlerExtension {
    @Shadow private NetworkManager field_72555_g;

    @Shadow public abstract void func_72552_c(Packet par1);

    @Shadow private boolean field_72554_f;
    @Shadow private Minecraft field_72563_h;

    private static byte connectionCompatibilityLevel;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/lang/String;I)V", at = @At("RETURN"))
    private void fml$onClientConnectionToRemoteServer(Minecraft p_i3103_1_, String p_i3103_2_, int p_i3103_3_, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToRemoteServer(this, p_i3103_2_, p_i3103_3_, this.field_72555_g);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/src/IntegratedServer;)V", at = @At("RETURN"))
    private void fml$onClientConnectionToIntegratedServer(Minecraft p_i3104_1_, IntegratedServer p_i3104_2_, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToIntegratedServer(this, p_i3104_2_, this.field_72555_g);
    }

    @Inject(method = "func_72513_a", at = @At("HEAD"))
    private void fml$sendFMLFakeLoginPacket(Packet252SharedKey p_72513_1_, CallbackInfo ci) {
        this.func_72552_c(FMLNetworkHandler.getFMLFakeLoginPacket());
    }

    @Inject(method = "func_72455_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetClientHandler;func_72552_c(Lnet/minecraft/src/Packet;)V"))
    private void fml$onConnectionEstablishedToServer(Packet1Login p_72455_1_, CallbackInfo ci) {
        FMLNetworkHandler.onConnectionEstablishedToServer(this, field_72555_g, p_72455_1_);
    }

    @Inject(method = "func_72546_b", at = @At("RETURN"))
    private void fml$onConnectionClosed(Packet p_72546_1_, CallbackInfo ci) {
        if (!this.field_72554_f) {
            FMLNetworkHandler.onConnectionClosed(this.field_72555_g, this.getPlayer());
        }
    }

    @Inject(method = "func_72481_a", at = @At("HEAD"))
    private void fml$handleChatMessage(Packet3Chat p_72481_1_, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet3Chat> ref) {
        ref.set(FMLNetworkHandler.handleChatMessage(this, p_72481_1_));
    }

    private boolean fmlPacket131Callback = false;

    @WrapMethod(method = "func_72494_a")
    private void fml$handlePacket131Packet(Packet131MapData p_72494_1_, Operation<Void> original) {
        if (fmlPacket131Callback) {
            fmlPacket131Callback = false;
            original.call(p_72494_1_);
        } else {
            FMLNetworkHandler.handlePacket131Packet(this, p_72494_1_);
        }
    }

    @Override
    public void fmlPacket131Callback(Packet131MapData p_72494_1_) {
        fmlPacket131Callback = true;
        func_72494_a(p_72494_1_);
    }

    private boolean handleVanilla250Packet = false;

    @WrapMethod(method = "func_72501_a")
    private void fml$handlePacket250Packet(Packet250CustomPayload p_72501_1_, Operation<Void> original) {
        if (handleVanilla250Packet) {
            handleVanilla250Packet = false;
            original.call(p_72501_1_);
        } else {
            FMLNetworkHandler.handlePacket250Packet(p_72501_1_, field_72555_g, this);
        }
    }

    @Override
    public void handleVanilla250Packet(Packet250CustomPayload p_72501_1_) {
        handleVanilla250Packet = true;
        func_72501_a(p_72501_1_);
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return field_72563_h.field_71439_g;
    }

    @Public
    private static void setConnectionCompatibilityLevel(byte connectionCompatibilityLevel)
    {
        NetClientHandlerMixin.connectionCompatibilityLevel = connectionCompatibilityLevel;
    }

    @Public
    private static byte getConnectionCompatibilityLevel()
    {
        return connectionCompatibilityLevel;
    }
}
