/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.relauncher.ArgsWrapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import io.github.fabriccompatibilitylayers.fabricatedfml.forged.ForgedGuiErrorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow private Timer field_71428_T;

    @Shadow public boolean field_71454_w;

    @Shadow public GuiScreen field_71462_r;

    @Inject(method = "func_71384_a", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;field_74363_ab:Ljava/lang/String;", ordinal = 0))
    private void fml$beginMinecraftLoading(CallbackInfo ci) {
        FMLClientHandler.instance().beginMinecraftLoading((Minecraft) (Object) this);
    }

    @Inject(method = "func_71384_a", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;field_71452_i:Lnet/minecraft/src/EffectRenderer;", shift = At.Shift.AFTER))
    private void fml$finishMinecraftLoading(CallbackInfo ci) {
        FMLClientHandler.instance().finishMinecraftLoading();
    }

    @Inject(method = "func_71384_a", at = @At("RETURN"))
    private void fml$onInitializationComplete(CallbackInfo ci) {
        FMLClientHandler.instance().onInitializationComplete();
    }

    @Inject(method = "func_71411_J", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76318_c(Ljava/lang/String;)V", ordinal = 3))
    private void fml$onRenderTickStart(CallbackInfo ci) {
        FMLCommonHandler.instance().onRenderTickStart(this.field_71428_T.field_74281_c);
    }

    @Inject(method = "func_71411_J", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glFlush()V", remap = false))
    private void fml$onRenderTickEnd(CallbackInfo ci) {
        if (!this.field_71454_w) FMLCommonHandler.instance().onRenderTickEnd(this.field_71428_T.field_74281_c);
    }

    @Inject(method = "func_71407_l", at = @At("HEAD"))
    private void fml$rescheduleTicks(CallbackInfo ci) {
        FMLCommonHandler.instance().rescheduleTicks(Side.CLIENT);
    }

    @Inject(method = "func_71407_l", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76320_a(Ljava/lang/String;)V"))
    private void fml$onPreClientTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPreClientTick();
    }

    @Inject(method = "func_71407_l", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76319_b()V"))
    private void fml$onPostClientTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPostClientTick();
    }

    @Inject(method = "func_71373_a", at = @At("HEAD"), cancellable = true)
    private void fml$hackyGuiErrorScreen(GuiScreen par1, CallbackInfo ci) {
        if (this.field_71462_r instanceof ForgedGuiErrorScreen) ci.cancel();
    }

    @WrapMethod(method = "main")
    private static void fml$main(String[] p_main_0_, Operation<Void> original)
    {
        if (FMLRelauncher.handleClientRelaunch(new ArgsWrapper(p_main_0_))) {
            original.call(p_main_0_);
        }
    }
}
