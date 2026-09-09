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
import java.util.function.Function;

public class ItemStackContainerListMapper<T> implements IdMapper {
    private final List<T> chestContents;
    private final Function<T, ItemStack> stackGetter;
    private final Set<T> missing;
    private boolean applied;

    public static <T> ItemStackContainerListMapper<T> of(List<T> chestContents, Function<T, ItemStack> stackGetter) {
        return new ItemStackContainerListMapper<>(chestContents, stackGetter);
    }

    public ItemStackContainerListMapper(List<T> chestContents, Function<T, ItemStack> stackGetter) {
        this.chestContents = chestContents;
        this.stackGetter = stackGetter;
        this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        this.missing.clear();
        Objects.requireNonNull(registryMappings);
        this.fixContainerItems(registryMappings::remap, true);
        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            Objects.requireNonNull(registryMappings);
            this.fixContainerItems(registryMappings::unmap, false);
            this.missing.clear();
        }

        this.applied = false;
    }

    private void fixContainerItems(Int2IntFunction mapper, boolean storeMissing) {
        for (T container : this.chestContents) {
            if (!this.missing.contains(container)) {
                int oldId = stackGetter.apply(container).itemID;
                int newId = mapper.applyAsInt(oldId);

                if (newId >= 0) {
                    stackGetter.apply(container).itemID = newId;
                } else if (storeMissing) {
                    this.missing.add(container);
                }
            }
        }
    }
}
