/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.relauncher.ArgsWrapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.WorldServer;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow public WorldServer[] field_71305_c;

    @Shadow private boolean field_71317_u;

    @Inject(method = "run", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 0, remap = false))
    private void fml$handleServerStarted(CallbackInfo ci) {
        FMLCommonHandler.instance().handleServerStarted();
    }

    @WrapOperation(method = "run", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 0, remap = false))
    private long fml$onWorldLoadTick(Operation<Long> original) {
        long result = original.call();
        FMLCommonHandler.instance().onWorldLoadTick(field_71305_c);
        return result;
    }

    @WrapOperation(method = "run", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V", remap = false))
    private void fml$handleServerStopping(long l, Operation<Void> original) {
        original.call(l);

        if (!this.field_71317_u) {
            FMLCommonHandler.instance().handleServerStopping();
        }
    }

    @Inject(method = "func_71217_p", at = @At("HEAD"))
    private void fml$rescheduleTicks(CallbackInfo ci) {
        FMLCommonHandler.instance().rescheduleTicks(Side.SERVER);
    }

    @Inject(method = "func_71217_p", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;field_71315_w:I", ordinal = 0))
    private void fml$onPreServerTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPreServerTick();
    }

    @Inject(method = "func_71217_p", at = @At("RETURN"))
    private void fml$onPostServerTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPostServerTick();
    }

    @Conditional(modAbsent = @Mod("fabricated-forge"))
    @Inject(method = "func_71190_q", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;func_72835_b()V"))
    private void fml$onPreWorldTick(CallbackInfo ci, @Local WorldServer var4) {
        FMLCommonHandler.instance().onPreWorldTick(var4);
    }

    @Conditional(modAbsent = @Mod("fabricated-forge"))
    @Inject(method = "func_71190_q", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76319_b()V", ordinal = 2))
    private void fml$onPostWorldTick(CallbackInfo ci, @Local WorldServer var4) {
        FMLCommonHandler.instance().onPostWorldTick(var4);
    }

    @WrapMethod(method = "main")
    @Environment(EnvType.SERVER)
    private static void fml$main(String[] p_main_0_, Operation<Void> original) {
        if (FMLRelauncher.handleServerRelaunch(new ArgsWrapper(p_main_0_))) {
            original.call(p_main_0_);
        }
    }
}
