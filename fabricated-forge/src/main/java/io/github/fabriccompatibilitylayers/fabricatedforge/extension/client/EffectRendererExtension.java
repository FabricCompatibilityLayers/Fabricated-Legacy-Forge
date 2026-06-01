/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.client;

import net.minecraft.src.EntityFX;
import net.minecraft.src.MovingObjectPosition;

public interface EffectRendererExtension {
    void addEffect(EntityFX effect, Object obj);

    void addBlockHitEffects(int x, int y, int z, MovingObjectPosition target);
}