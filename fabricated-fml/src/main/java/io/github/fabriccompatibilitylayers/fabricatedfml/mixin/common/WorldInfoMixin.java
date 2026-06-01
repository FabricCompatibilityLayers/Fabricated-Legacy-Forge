/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.WorldInfoExtension;
import net.minecraft.src.NBTBase;
import net.minecraft.src.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(WorldInfo.class)
public class WorldInfoMixin implements WorldInfoExtension {
    private Map<String, NBTBase> additionalProperties;

    /**
     * Allow access to additional mod specific world based properties
     * Used by FML to store mod list associated with a world, and maybe an id map
     * Used by Forge to store the dimensions available to a world
     * @param additionalProperties
     */
    @Override
    public void setAdditionalProperties(Map<String, NBTBase> additionalProperties)
    {
        // one time set for this
        if (this.additionalProperties == null)
        {
            this.additionalProperties = additionalProperties;
        }
    }

    @Override
    public NBTBase getAdditionalProperty(String additionalProperty)
    {
        return this.additionalProperties!=null? this.additionalProperties.get(additionalProperty) : null;
    }
}
