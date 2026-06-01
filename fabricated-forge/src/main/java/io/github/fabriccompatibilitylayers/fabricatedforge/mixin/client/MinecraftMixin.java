/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.EffectRendererExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow public MovingObjectPosition objectMouseOver;
    @Shadow public EntityClientPlayerMP thePlayer;
    @Shadow public LoadingScreenRenderer loadingScreen;
    @Shadow public WorldClient theWorld;
    @Shadow public PlayerControllerMP playerController;

    // Pattern N (@Redirect): replaces the vanilla addBlockHitEffects(IIII)V call-site in
    // sendClickBlockToController with the Forge overload that accepts MovingObjectPosition,
    // allowing blocks to override hit-particle behaviour with full context.
    // Logic delta: the sideHit int arg is dropped; the entire objectMouseOver is passed instead.
    @Redirect(
            method = "sendClickBlockToController",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EffectRenderer;addBlockHitEffects(IIII)V")
    )
    private void forge$addBlockHitEffects(EffectRenderer effectRenderer, int x, int y, int z, int sideHit) {
        ((EffectRendererExtension) effectRenderer).addBlockHitEffects(x, y, z, objectMouseOver);
    }

    // Pattern E (@WrapOperation): intercepts the onPlayerRightClick call-site so we can
    // pre-fire the Forge RIGHT_CLICK_BLOCK event and gate the original call on its result.
    // Logic delta: the original method is only called when the event is not cancelled;
    // the combined boolean short-circuits identically to the patch's "result &&" guard.
    @WrapOperation(
            method = "clickMouse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/PlayerControllerMP;onPlayerRightClick(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;Lnet/minecraft/src/ItemStack;IIIILnet/minecraft/src/Vec3;)Z")
    )
    private boolean forge$onPlayerRightClickBlock(
            PlayerControllerMP controller,
            EntityPlayer player, World world, ItemStack stack,
            int x, int y, int z, int face, Vec3 hitVec,
            Operation<Boolean> original
    ) {
        boolean eventAllowed = !ForgeEventFactory.onPlayerInteract(player, Action.RIGHT_CLICK_BLOCK, x, y, z, face).isCanceled();
        return eventAllowed && original.call(controller, player, world, stack, x, y, z, face, hitVec);
    }

    // Pattern E (@WrapOperation on MIXINEXTRAS:EXPRESSION): targets the "var9 != null" null check
    // inside clickMouse so the Forge RIGHT_CLICK_AIR event can cancel the air-use branch even when
    // var9 is non-null, matching the patch's "result && var9 != null && sendUseItem(...)" guard.
    // Logic delta: event fires before the null check; if cancelled, the whole branch is skipped.
    @Definition(id = "var9", local = @Local(type = ItemStack.class, ordinal = 1))
    @Expression("var9 != null")
    @WrapOperation(method = "clickMouse", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$gateRightClickAir(Object left, Object right, Operation<Boolean> original) {
        boolean eventAllowed = !ForgeEventFactory.onPlayerInteract((EntityPlayer) thePlayer, Action.RIGHT_CLICK_AIR, 0, 0, 0, -1).isCanceled();
        return eventAllowed && original.call(left, right);
    }

    // Pattern E (@WrapOperation): intercepts the initiateShutdown() call-site in loadWorld so we
    // can spin-wait after the shutdown request until the server thread has fully stopped.
    // Logic delta: vanilla proceeds immediately after initiateShutdown(); here we block the client
    // thread (sleeping 10 ms/tick) until isServerStopped() returns true, matching the Forge patch.
    @WrapOperation(
        method = "loadWorld(Lnet/minecraft/src/WorldClient;Ljava/lang/String;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/IntegratedServer;initiateShutdown()V")
    )
    private void forge$waitForServerShutdown(IntegratedServer server, Operation<Void> original) {
        original.call(server);
        if (loadingScreen != null) {
            loadingScreen.resetProgresAndWorkingMessage("Shutting down internal server...");
        }
        while (!server.isServerStopped()) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException ie) {}
        }
    }

    // Pattern D (@Overwrite): the patch replaces >80% of clickMiddleMouseButton with a single
    // ForgeHooks.onPickBlock call; @Overwrite is appropriate since the original logic is discarded.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates pick-block item selection to ForgeHooks.onPickBlock so mods can override it
     */
    @Overwrite
    private void clickMiddleMouseButton() {
        if (this.objectMouseOver != null) {
            boolean var1 = this.thePlayer.capabilities.isCreativeMode;

            if (!ForgeHooks.onPickBlock(this.objectMouseOver, this.thePlayer, this.theWorld)) {
                return;
            }

            if (var1) {
                int var11 = this.thePlayer.inventorySlots.inventorySlots.size() - 9 + this.thePlayer.inventory.currentItem;
                this.playerController.sendSlotPacket(this.thePlayer.inventory.getStackInSlot(this.thePlayer.inventory.currentItem), var11);
            }
        }
    }
}