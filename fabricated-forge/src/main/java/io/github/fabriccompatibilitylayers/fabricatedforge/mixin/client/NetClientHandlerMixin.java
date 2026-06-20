/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.TileEntityExtension;
import net.minecraft.src.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin {

    @Shadow private INetworkManager netManager;

    // Pattern E (@WrapOperation): intercepts the networkShutdown(String, Object...) call-site
    // in handleKickDisconnect and re-calls it with the kick reason as the vararg instead of the
    // empty Object[] the vanilla code passes. Surgical — rest of the method is untouched.
    @WrapOperation(
            method = "handleKickDisconnect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/INetworkManager;networkShutdown(Ljava/lang/String;[Ljava/lang/Object;)V")
    )
    private void forge$kickDisconnectWithReason(INetworkManager instance, String key, Object[] args,
            Operation<Void> op,
            @Local(argsOnly = true) Packet255KickDisconnect packet) {
        op.call(instance, key, new Object[]{packet.reason});
    }

    // Pattern Q (@WrapWithCondition): gates printChatMessage on ClientChatReceivedEvent not being
    // cancelled and event.message being non-null. printChatMessage returns void so suppression is safe.
    @WrapWithCondition(
            method = "handleChat",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiNewChat;printChatMessage(Ljava/lang/String;)V")
    )
    private boolean forge$chatReceivedEvent(GuiNewChat chatGui, String message) {
        ClientChatReceivedEvent event = new ClientChatReceivedEvent(message);
        return !MinecraftForge.EVENT_BUS.post(event) && event.message != null;
    }

    @ModifyExpressionValue(
            method = "handleTileEntityData",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldClient;getBlockTileEntity(III)Lnet/minecraft/src/TileEntity;")
    )
    private TileEntity forge$captureTileEntityData(TileEntity var2, @Local(argsOnly = true) Packet132TileEntityData par1Packet132TileEntityData, @Cancellable CallbackInfo ci) {
        if (var2 != null) {
            if (!(par1Packet132TileEntityData.actionType == 1 && var2 instanceof TileEntityMobSpawner) &&
            !(par1Packet132TileEntityData.actionType == 2 && var2 instanceof TileEntityCommandBlock) &&
            !(par1Packet132TileEntityData.actionType == 3 && var2 instanceof TileEntityBeacon) &&
            !(par1Packet132TileEntityData.actionType == 4 && var2 instanceof TileEntitySkull)) {
                ((TileEntityExtension) var2).onDataPacket(netManager,  par1Packet132TileEntityData);
                ci.cancel();
            }
        }

        return var2;
    }
}