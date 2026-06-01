/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.ItemStack;

public interface CreativeTabsExtension {
    int getTabPage();

    ItemStack getIconItemStack();
}
