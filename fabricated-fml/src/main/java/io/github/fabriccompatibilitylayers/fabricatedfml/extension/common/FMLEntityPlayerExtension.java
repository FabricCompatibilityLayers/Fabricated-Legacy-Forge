/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.extension.common;

import net.minecraft.src.World;

public interface FMLEntityPlayerExtension {
    void openGui(Object mod, int modGuiId, World world, int x, int y, int z);
}
