/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.fml.TradeEntryAccessor;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.src.TradeEntry;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.*;

public class TradeEntriesMapper implements IdMapper {
    private final List<TradeEntry> tradeEntries;
    private final Set<TradeEntry> missing;
    private boolean applied;

    public static TradeEntriesMapper of(List<TradeEntry> tradeEntries) {
        return new TradeEntriesMapper(tradeEntries);
    }

    private TradeEntriesMapper(List<TradeEntry> tradeEntries) {
        this.tradeEntries = tradeEntries;
        this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    public void apply(RegistryMappings mappings) {
        this.missing.clear();
        Objects.requireNonNull(mappings);
        this.fixAchievementIcons(mappings::remap, true);
        this.applied = true;
    }

    public void undo(RegistryMappings mappings) {
        if (this.applied) {
            Objects.requireNonNull(mappings);
            this.fixAchievementIcons(mappings::unmap, false);
            this.missing.clear();
        }

        this.applied = false;
    }

    private void fixAchievementIcons(Int2IntFunction mapper, boolean storeMissing) {
        for(TradeEntry tradeEntry : this.tradeEntries) {
            if (!this.missing.contains(tradeEntry)) {
                int oldId = tradeEntry.id;
                int newId = mapper.applyAsInt(oldId);
                if (newId >= 0) {
                    ((TradeEntryAccessor) tradeEntry).setId(newId);
                } else if (storeMissing) {
                    this.missing.add(tradeEntry);
                }
            }
        }

    }
}
