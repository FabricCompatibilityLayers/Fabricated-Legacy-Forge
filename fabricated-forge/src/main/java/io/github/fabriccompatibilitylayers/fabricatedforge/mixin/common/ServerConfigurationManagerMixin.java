/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.common.DimensionManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerConfigurationManager.class)
public abstract class ServerConfigurationManagerMixin {
    @Shadow @Final private MinecraftServer mcServer;

    @Shadow
    public abstract void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2);

    @Inject(method = "respawnPlayer", at = @At("HEAD"))
    private void forge$FixRespawnDimension(EntityPlayerMP par1EntityPlayerMP, int par2, boolean par3, CallbackInfoReturnable<EntityPlayerMP> cir,
                                           @Local(ordinal = 0, argsOnly = true) LocalIntRef par2Ref) {
        World world = mcServer.worldServerForDimension(par2);
        if (world == null || !world.provider.canRespawnHere())
        {
            par2Ref.set(0);
        }
    }

    @Inject(method = "respawnPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerMP;entityId:I", ordinal = 0))
    private void forge$updateDimensionId(EntityPlayerMP par1EntityPlayerMP, int par2, boolean par3, CallbackInfoReturnable<EntityPlayerMP> cir,
                                         @Local(ordinal = 1) EntityPlayerMP var6) {
        var6.dimension = par2;
    }

    private boolean transferPlayerToDimension = false;
    private Teleporter transferPlayerToDimensionTeleporter;

    @WrapMethod(method = "transferPlayerToDimension")
    private void forge$transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Operation<Void> original) {
        if (transferPlayerToDimension) {
            original.call(par1EntityPlayerMP, par2);
            transferPlayerToDimensionTeleporter = null;
            transferPlayerToDimension = false;
        } else {
            transferPlayerToDimension(par1EntityPlayerMP, par2, new Teleporter());
        }
    }

    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter) {
        transferPlayerToDimension = true;
        transferPlayerToDimensionTeleporter = teleporter;
        transferPlayerToDimension(par1EntityPlayerMP, par2);
    }

    @ModifyExpressionValue(method = "transferPlayerToDimension", at = {
            @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerMP;posX:D", ordinal = 0, opcode = Opcodes.GETFIELD),
            @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerMP;posZ:D", ordinal = 0, opcode = Opcodes.GETFIELD)
    })
    private double forge$transferPlayerToDimension$modifyMoveFactor(double original,
                                                                    @Local(ordinal = 0, argsOnly = true) int par2,
                                                                    @Local(ordinal = 1) int var3) {
        if (transferPlayerToDimension) {
            WorldProvider pOld = DimensionManager.getProvider(var3);
            WorldProvider pNew = DimensionManager.getProvider(par2);
            double moveFactor = ((WorldProviderExtension) pOld).getMovementFactor() / ((WorldProviderExtension) pNew).getMovementFactor();
            return original * moveFactor;
        } else {
            return original;
        }
    }

    @Definition(id = "dimension", field = "Lnet/minecraft/src/EntityPlayerMP;dimension:I")
    @Expression("?.dimension == ?")
    @WrapOperation(method = "transferPlayerToDimension", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$transferPlayerToDimension$customDimensionHandling(int left, int right, Operation<Boolean> original) {
        return !transferPlayerToDimension && original.call(left, right);
    }

    @Definition(id = "Teleporter", type = Teleporter.class)
    @Expression("new Teleporter()")
    @WrapOperation(method = "transferPlayerToDimension", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Teleporter forge$transferPlayerToDimension$reuseTeleporter(Operation<Teleporter> original) {
        return transferPlayerToDimension ? transferPlayerToDimensionTeleporter : original.call();
    }
}
