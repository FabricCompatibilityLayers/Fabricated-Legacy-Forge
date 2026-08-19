/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.SaveHandlerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    public WorldServer[] worldServers;

    @Inject(method = "saveAllWorlds", at = @At("HEAD"))
    private void flf$saveRegistries(boolean par1, CallbackInfo ci) {
        if (this.worldServers != null && this.worldServers.length > 0) {
            ISaveHandler handler = this.worldServers[0].getSaveHandler();

            if (handler instanceof SaveHandlerAccessor) {
                ((SaveHandlerAccessor) handler).flf$saveRegistries();
            }
        }
    }
}
