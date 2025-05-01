package io.github.fabriccompatibilitylayers.fabricatedfml.extension.common;

import net.minecraft.src.NBTBase;

import java.util.Map;

public interface WorldInfoExtension {
    void setAdditionalProperties(Map<String, NBTBase> additionalProperties);

    NBTBase getAdditionalProperty(String additionalProperty);
}
