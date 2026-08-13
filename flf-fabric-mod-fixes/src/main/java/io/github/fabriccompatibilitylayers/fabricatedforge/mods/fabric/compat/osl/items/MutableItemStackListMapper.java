/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.src.ItemStack;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.List;
import java.util.Objects;

public class MutableItemStackListMapper implements IdMapper {
    private final List<ItemStack> list;
    private final Int2ObjectMap<ItemStack> missing;
    private boolean applied;

    public static MutableItemStackListMapper of(List<ItemStack> list) {
        return new MutableItemStackListMapper(list);
    }

    public MutableItemStackListMapper(List<ItemStack> list) {
        this.list = list;
        this.missing = new Int2ObjectOpenHashMap<>();
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

            for (Int2ObjectMap.Entry<ItemStack> entry : missing.int2ObjectEntrySet()) {
                list.add(entry.getIntKey(), entry.getValue());
            }

            this.missing.clear();
        }

        this.applied = false;
    }

    private void fixItemStacks(Int2IntFunction mapper, boolean storeMissing) {
        for (int i = list.size() - 1; i >= 0; i--) {
            ItemStack stack = list.get(i);

            int oldId = stack.itemID;
            int newId = mapper.applyAsInt(oldId);

            if (newId >= 0) {
                stack.itemID = newId;
            } else if (storeMissing) {
                missing.put(i, stack);
                list.remove(i);
            }
        }
    }
}
