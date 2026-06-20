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
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ServerConfigurationManagerExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerConfigurationManager.class)
public abstract class ServerConfigurationManagerMixin implements ServerConfigurationManagerExtension {
    @Shadow @Final private MinecraftServer mcServer;

    @Shadow
    public abstract void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2);

    @Shadow
    public abstract void func_82448_a(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer);

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

    @Override
    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter) {
        transferPlayerToDimension = true;
        transferPlayerToDimensionTeleporter = teleporter;
        transferPlayerToDimension(par1EntityPlayerMP, par2);
    }

    @WrapOperation(method = "transferPlayerToDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerConfigurationManager;func_82448_a(Lnet/minecraft/src/Entity;ILnet/minecraft/src/WorldServer;Lnet/minecraft/src/WorldServer;)V"))
    private void forge$transferPlayerToDimension$teleporterAwareMethod(ServerConfigurationManager instance, Entity par2, int par3WorldServer, WorldServer par4WorldServer, WorldServer worldServer, Operation<Void> original) {
        if (transferPlayerToDimension) {
            this.func_82448_a(par2, par3WorldServer, par4WorldServer, worldServer, transferPlayerToDimensionTeleporter);
        } else {
            original.call(instance, par2, par3WorldServer, par4WorldServer, worldServer);
        }
    }

    private boolean func_82448_a = false;
    private Teleporter func_82448_aTeleporter = null;

    @WrapMethod(method = "func_82448_a")
    private void forge$func_82448_a(Entity par2, int par3WorldServer, WorldServer par4WorldServer, WorldServer par4, Operation<Void> original) {
        if (func_82448_a) {
            original.call(par2, par3WorldServer, par4WorldServer, par4);
            func_82448_a = false;
            func_82448_aTeleporter = null;
        } else {
            func_82448_a(par2, par3WorldServer, par4WorldServer, par4, new Teleporter());
        }
    }

    @Override
    public void func_82448_a(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer, Teleporter teleporter)
    {
        func_82448_a = true;
        func_82448_aTeleporter = teleporter;
        this.func_82448_a(par1Entity, par2, par3WorldServer, par4WorldServer);
    }

    @ModifyExpressionValue(method = "func_82448_a", at = {
            @At(value = "FIELD", target = "Lnet/minecraft/src/Entity;posX:D", ordinal = 0, opcode = Opcodes.GETFIELD),
            @At(value = "FIELD", target = "Lnet/minecraft/src/Entity;posZ:D", ordinal = 0, opcode = Opcodes.GETFIELD)
    })
    private double forge$transferPlayerToDimension$modifyMoveFactor(double original,
                                                                    @Local(ordinal = 0) WorldServer par3WorldServer,
                                                                    @Local(ordinal = 1) WorldServer par4WorldServer) {
        if (func_82448_a) {
            WorldProvider pOld = par3WorldServer.provider;
            WorldProvider pNew = par4WorldServer.provider;
            double moveFactor = ((WorldProviderExtension) pOld).getMovementFactor() / ((WorldProviderExtension) pNew).getMovementFactor();
            return original * moveFactor;
        } else {
            return original;
        }
    }

    @Definition(id = "dimension", field = "Lnet/minecraft/src/Entity;dimension:I")
    @Expression("?.dimension == ?")
    @WrapOperation(method = "func_82448_a", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$transferPlayerToDimension$customDimensionHandling(int left, int right, Operation<Boolean> original) {
        return !func_82448_a && original.call(left, right);
    }

    @Definition(id = "Teleporter", type = Teleporter.class)
    @Expression("new Teleporter()")
    @WrapOperation(method = "func_82448_a", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Teleporter forge$transferPlayerToDimension$reuseTeleporter(Operation<Teleporter> original) {
        return func_82448_a ? func_82448_aTeleporter : original.call();
    }
}
