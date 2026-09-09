/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import net.minecraft.src.ItemStack;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MutableItemStackListMapMapper<T> implements IdMapper {
    private final Map<T, ? extends List<ItemStack>> itemStackListMap;
    private final Map<T, MutableItemStackListMapper> mappers;
    private boolean applied;

    public static MutableItemStackListMapMapper<?> of(Map<?, ? extends List<ItemStack>> itemStackListMap) {
        return new MutableItemStackListMapMapper<>(itemStackListMap);
    }

    public MutableItemStackListMapMapper(Map<T, ? extends List<ItemStack>> itemStackListMap) {
        this.itemStackListMap = itemStackListMap;
        this.mappers = new HashMap<>();
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        Objects.requireNonNull(registryMappings);

        for (Map.Entry<T, ? extends List<ItemStack>> entry : itemStackListMap.entrySet()) {
            MutableItemStackListMapper mapper = mappers.computeIfAbsent(entry.getKey(), k -> MutableItemStackListMapper.of(entry.getValue()));
            mapper.apply(registryMappings);
        }

        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            Objects.requireNonNull(registryMappings);
            for (MutableItemStackListMapper mapper : mappers.values()) {
                mapper.undo(registryMappings);
            }
        }

        this.applied = false;
    }
}
