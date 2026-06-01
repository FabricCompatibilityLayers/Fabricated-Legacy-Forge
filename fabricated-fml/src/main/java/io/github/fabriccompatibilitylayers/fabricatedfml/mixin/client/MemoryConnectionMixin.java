/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.src.MemoryConnection;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MemoryConnection.class)
public abstract class MemoryConnectionMixin implements NetworkManager {
    @Shadow private boolean field_74441_e;

    @Shadow @Final private List field_74442_b;

    @Shadow private NetHandler field_74440_d;

    @Inject(method = "func_74428_b", at = @At("RETURN"))
    private void fml$onConnectionClosed(CallbackInfo ci) {
        if (this.field_74441_e && this.field_74442_b.isEmpty()) {
            FMLNetworkHandler.onConnectionClosed(this, ((NetHandlerExtension) this.field_74440_d).getPlayer());
        }
    }
}
