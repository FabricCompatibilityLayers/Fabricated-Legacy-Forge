/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.compat.lfapi;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.Arrays;
import java.util.List;

public class LFAPIMixinCanceller implements MixinCanceller {
    private static final List<String> DISABLED = Arrays.asList(
            "net.legacyfabric.fabric.mixin.client.keybinding.ControlsOptionsScreenMixin",
            "net.legacyfabric.fabric.mixin.item.group.client.MixinCreativePlayerInventoryGui"
    );

    @Override
    public boolean shouldCancel(List<String> list, String s) {
        return DISABLED.contains(s);
    }
}
