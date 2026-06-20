/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemInWorldManagerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetServerHandler.class)
public class NetServerHandlerMixin {
    @Shadow private boolean hasMoved;

    @Shadow private EntityPlayerMP playerEntity;

    @Shadow private MinecraftServer mcServer;

    @Inject(method = "handleFlying", at = {
            //Fixes teleportation kick while riding entities
            @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerConfigurationManager;serverUpdateMountedMovingPlayer(Lnet/minecraft/src/EntityPlayerMP;)V", ordinal = 0),
            //Fixes "Moved Too Fast" kick when being teleported while moving
            @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;moveEntity(DDD)V", ordinal = 1),
            //Fixes "Moved Too Fast" kick when being teleported while moving
            @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;setPositionAndRotation(DDDFF)V", ordinal = 3),
            //Fixes "Moved Too Fast" kick when being teleported while moving
            @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerMP;onGround:Z", ordinal = 2)
    }, cancellable = true)
    private void forge$fixBug(Packet10Flying par1, CallbackInfo ci) {
        if (!this.hasMoved) ci.cancel();
    }

    @WrapOperation(method = "handleFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;isPlayerSleeping()Z", ordinal = 3))
    private boolean forge$noClip(EntityPlayerMP instance, Operation<Boolean> original) {
        return original.call(instance) || instance.noClip;
    }

    @WrapOperation(method = "handleFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;isAABBNonEmpty(Lnet/minecraft/src/AxisAlignedBB;)Z"))
    private boolean forge$allowFlying(WorldServer instance, AxisAlignedBB axisAlignedBB, Operation<Boolean> original) {
        return original.call(instance, axisAlignedBB) || this.playerEntity.capabilities.allowFlying;
    }

    @ModifyConstant(method = "handleBlockDig", constant = @Constant(doubleValue = 36.0D))
    private double forge$getBlockReachDistance1(double constant) {
        double dist = ((ItemInWorldManagerExtension) playerEntity.theItemInWorldManager).getBlockReachDistance() + 1;
        dist *= dist;

        return dist;
    }

    @Inject(method = "handleBlockDig", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetServerHandler;sendPacketToPlayer(Lnet/minecraft/src/Packet;)V", ordinal = 0))
    private void forge$onPlayerInteract(Packet14BlockDig par1, CallbackInfo ci,
                                        @Local(ordinal = 2) int var5,
                                        @Local(ordinal = 3) int var6,
                                        @Local(ordinal = 4) int var7) {
        ForgeEventFactory.onPlayerInteract(playerEntity, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, var5, var6, var7, 0);
    }

    @WrapOperation(method = "handlePlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemInWorldManager;tryUseItem(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;Lnet/minecraft/src/ItemStack;)Z"))
    private boolean forge$onPlayerInteract(ItemInWorldManager instance, EntityPlayer playerEntity, World world, ItemStack itemStack, Operation<Boolean> original) {
        PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(playerEntity, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1);
        if (event.useItem != Event.Result.DENY) {
            return original.call(instance, playerEntity, world, itemStack);
        } else {
            return false;
        }
    }

    @ModifyConstant(method = "handlePlace", constant = @Constant(doubleValue = 64.0D))
    private double forge$getBlockReachDistance2(double constant) {
        double dist = ((ItemInWorldManagerExtension) playerEntity.theItemInWorldManager).getBlockReachDistance() + 1;
        dist *= dist;

        return dist;
    }

    @WrapOperation(method = "handleClientCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerConfigurationManager;respawnPlayer(Lnet/minecraft/src/EntityPlayerMP;IZ)Lnet/minecraft/src/EntityPlayerMP;", ordinal = 1))
    private EntityPlayerMP forge$dehardcodeDimensionId(ServerConfigurationManager instance, EntityPlayerMP par2, int par3, boolean b, Operation<EntityPlayerMP> original) {
        return original.call(instance, par2, par3 == 0 ? par2.dimension : par3, b);
    }
}
