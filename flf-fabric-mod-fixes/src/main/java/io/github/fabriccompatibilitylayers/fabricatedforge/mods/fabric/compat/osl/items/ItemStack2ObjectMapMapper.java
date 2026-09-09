/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.src.ItemStack;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.*;

public class ItemStack2ObjectMapMapper<T> implements IdMapper {
    private final Map<ItemStack, T> objectMap;
    private final Set<ItemStack> missing;
    private boolean applied;

    public static ItemStack2ObjectMapMapper<?> of(Map<ItemStack, ?> objectMap) {
        return new ItemStack2ObjectMapMapper<>(objectMap);
    }

    public ItemStack2ObjectMapMapper(Map<ItemStack, T> objectMap) {
        this.objectMap = objectMap;
        this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        this.missing.clear();
        Objects.requireNonNull(registryMappings);
        this.fixItemStacks(registryMappings::remap, true);
        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            Objects.requireNonNull(registryMappings);
            this.fixItemStacks(registryMappings::unmap, false);
            this.missing.clear();
        }

        this.applied = false;
    }

    private void fixItemStacks(Int2IntFunction mapper, boolean storeMissing) {
        for (Map.Entry<ItemStack, T> entry : this.objectMap.entrySet()) {
            if (!this.missing.contains(entry.getKey())) {
                int oldId = entry.getKey().itemID;
                int newId = mapper.applyAsInt(oldId);

                if (newId >= 0) {
                    entry.getKey().itemID = newId;
                } else if (storeMissing) {
                    this.missing.add(entry.getKey());
                }
            }
        }
    }
}
