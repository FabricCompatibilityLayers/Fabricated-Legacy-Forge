/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.FMLEntityPlayerExtension;
import net.minecraft.src.Block;

public interface EntityPlayerExtension extends EntityLivingExtension, FMLEntityPlayerExtension {
    float getCurrentPlayerStrVsBlock(Block par1Block, int meta);
}
