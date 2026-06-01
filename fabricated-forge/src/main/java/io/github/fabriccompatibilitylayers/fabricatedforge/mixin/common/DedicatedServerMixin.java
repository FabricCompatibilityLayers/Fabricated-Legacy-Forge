/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.MinecraftServerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.PropertyManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer implements MinecraftServerExtension {
    @Shadow private PropertyManager settings;

    public DedicatedServerMixin(File par1File) {
        super(par1File);
    }

    @Inject(method = "startServer", at = @At(value = "FIELD", target = "Lnet/minecraft/src/DedicatedServer;canSpawnStructures:Z"))
    private void forge$setSpawnProtectionSize(CallbackInfoReturnable<Boolean> cir) {
        this.setSpawnProtectionSize(this.settings.getIntProperty("spawn-protection-size", 16));
    }
}
