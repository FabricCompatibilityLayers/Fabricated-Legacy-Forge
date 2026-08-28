/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint;

import buildcraft.api.blueprints.BptBlock;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BptBlockBlockMapper implements IdMapper {
    private final DynamicArray<BptBlock> registry;
    private final ArrayMapper arrayMapper;
    private final Set<BptBlock> missing;
    private boolean applied;

    public static BptBlockBlockMapper of(Supplier<BptBlock[]> getter, Consumer<BptBlock[]> setter) {
        return new BptBlockBlockMapper(DynamicArray.of(getter, setter));
    }

    private BptBlockBlockMapper(DynamicArray<BptBlock> registry) {
        this.registry = registry;
        this.arrayMapper = ArrayMapper.of(registry);
        this.missing = new HashSet<>();
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        this.arrayMapper.apply(registryMappings);
        this.fixAdditionalBlockIds(registryMappings::remap, true);

        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            this.arrayMapper.undo(registryMappings);
            this.fixAdditionalBlockIds(registryMappings::unmap, false);
            this.missing.clear();

            this.applied = false;
        }
    }

    private void fixAdditionalBlockIds(Int2IntFunction mapper, boolean storeMissing) {
        for (int i = 0; i < registry.length(); i++) {
            BptBlock block = registry.get(i);

            if (block instanceof BptBlockBlockRemap && !this.missing.contains(block)) {
                int oldId = ((BptBlockBlockRemap) block).osl$getBlockId();
                int newId = mapper.applyAsInt(oldId);

                if (newId >= 0) {
                    ((BptBlockBlockRemap) block).osl$setBlockId(newId);
                } else if (storeMissing) {
                    missing.add(block);
                }
            }
        }
    }
}
