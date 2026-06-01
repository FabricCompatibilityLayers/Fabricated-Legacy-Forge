/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.ServerListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Level;

@Mixin(ServerListenThread.class)
public class ServerListenThreadMixin {
    @Inject(method = "func_71766_a", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;log(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false))
    private void fml$log(CallbackInfo ci, @Local NetLoginHandler var3, @Local Exception var6) {
        FMLLog.log(Level.SEVERE, var6, "Error handling login related packet - connection from %s refused", var3.field_72543_h);
    }
}