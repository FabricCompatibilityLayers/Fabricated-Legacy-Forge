/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.liquid;

import buildcraft.api.liquids.LiquidStack;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.*;
import java.util.function.Function;

public class LiquidStackContainerListMapper<T> implements IdMapper {
    private final List<T> containerList;
    private final Function<T, LiquidStack> stackGetter;
    private final Set<T> missing;
    private boolean applied;

    public static <T> LiquidStackContainerListMapper<T> of(List<T> containerList, Function<T, LiquidStack> stackGetter) {
        return new LiquidStackContainerListMapper<>(containerList, stackGetter);
    }

    public LiquidStackContainerListMapper(List<T> containerList, Function<T, LiquidStack> stackGetter) {
        this.containerList = containerList;
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
        for (T container : this.containerList) {
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
