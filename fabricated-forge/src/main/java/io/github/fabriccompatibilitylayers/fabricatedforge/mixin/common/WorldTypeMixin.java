/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldTypeExtension;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldType.class)
public abstract class WorldTypeMixin implements WorldTypeExtension {
    /**
     * Gets the spawn fuzz for players who join the world.
     * Useful for void world types.
     * @return Fuzz for entity initial spawn in blocks.
     */
    @Override
    public int getSpawnFuzz()
    {
        return 20;
    }
}
