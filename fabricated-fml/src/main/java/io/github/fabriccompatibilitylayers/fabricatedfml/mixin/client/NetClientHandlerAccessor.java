/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import net.minecraft.src.NetClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = NetClientHandler.class, priority = 1001)
@Pseudo
public interface NetClientHandlerAccessor {
    @Invoker(remap = false, value = "setConnectionCompatibilityLevel")
    static void callSetConnectionCompatibilityLevel(byte connectionCompatibilityLevel) {

    }

    @Invoker(remap = false, value = "getConnectionCompatibilityLevel")
    static byte callGetConnectionCompatibilityLevel() {
        return 0;
    }
}
