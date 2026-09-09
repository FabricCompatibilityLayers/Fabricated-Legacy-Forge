/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint;

import buildcraft.api.blueprints.BptBlock;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BptBlockItemMapper implements IdMapper {
    private final DynamicArray<BptBlock> registry;
    private final Set<BptBlock> missing;

    private boolean applied;

    public static BptBlockItemMapper of(Supplier<BptBlock[]> getter, Consumer<BptBlock[]> setter) {
        return new BptBlockItemMapper(DynamicArray.of(getter, setter));
    }

    private BptBlockItemMapper(DynamicArray<BptBlock> registry) {
        this.registry = registry;
        this.missing = new HashSet<>();
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        this.fixAdditionalItemIds(registryMappings::remap, true);

        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            this.fixAdditionalItemIds(registryMappings::unmap, false);
            this.missing.clear();

            this.applied = false;
        }
    }

    private void fixAdditionalItemIds(Int2IntFunction mapper, boolean storeMissing) {
        for (int i = 0; i < registry.length(); i++) {
            BptBlock block = registry.get(i);

            if (block instanceof BptBlockItemRemap && !this.missing.contains(block)) {
                int oldId = ((BptBlockItemRemap) block).osl$getItemId();
                int newId = mapper.applyAsInt(oldId);

                if (newId >= 0) {
                    ((BptBlockItemRemap) block).osl$setItemId(newId);
                } else if (!storeMissing) {
                    this.missing.add(block);
                }
            }
        }
    }
}
