/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.NetworkListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Level;

@Mixin(NetworkListenThread.class)
public class NetworkListenThreadMixin {
    @Inject(method = "func_71747_b", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;log(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false))
    private void fml$log(CallbackInfo ci, @Local NetServerHandler var2, @Local Exception var4) {
        FMLLog.log(Level.SEVERE, var4, "A critical server error occured handling a packet, kicking %s", var2);
    }
}
