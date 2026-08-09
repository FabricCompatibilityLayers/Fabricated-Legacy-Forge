/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.Arrays;
import java.util.List;

public class OSLMixinCanceller implements MixinCanceller {
    private static final List<String> DISABLED = Arrays.asList(
            "net.ornithemc.osl.items.impl.mixin.client.MinecraftMixinMid",
            "net.ornithemc.osl.items.impl.mixin.client.MinecraftMixinNew"
    );

    @Override
    public boolean shouldCancel(List<String> list, String s) {
        return DISABLED.contains(s);
    }
}
