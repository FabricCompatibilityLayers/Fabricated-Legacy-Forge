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

public class Object2ItemStackMapMapper<T> implements IdMapper {
    private final Map<T, ItemStack> stackMap;
    private final Set<T> missing;
    private boolean applied;

    public static Object2ItemStackMapMapper<?> of(Map<?, ItemStack> stackMap) {
        return new Object2ItemStackMapMapper<>(stackMap);
    }

    public Object2ItemStackMapMapper(Map<T, ItemStack> stackMap) {
        this.stackMap = stackMap;
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
        for (Map.Entry<T, ItemStack> entry : this.stackMap.entrySet()) {
            if (!this.missing.contains(entry.getKey())) {
                int oldId = entry.getValue().itemID;
                int newId = mapper.applyAsInt(oldId);

                if (newId >= 0) {
                    entry.getValue().itemID = newId;
                } else if (storeMissing) {
                    this.missing.add(entry.getKey());
                }
            }
        }
    }
}
