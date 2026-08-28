/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.liquid;

import buildcraft.api.liquids.LiquidData;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class LiquidDataMapper implements IdMapper {
    private final LinkedList<LiquidData> liquidData;
    private final Set<LiquidData> missing;
    private boolean applied;

    public static LiquidDataMapper of(LinkedList<LiquidData> liquidData) {
        return new LiquidDataMapper(liquidData);
    }

    private LiquidDataMapper(LinkedList<LiquidData> liquidData) {
        this.liquidData = liquidData;
        this.missing = new HashSet<>();
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        fixLiquidData(registryMappings::remap, true);
        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            fixLiquidData(registryMappings::unmap, false);
            this.missing.clear();

            this.applied = false;
        }
    }

    private void fixLiquidData(Int2IntFunction mapper, boolean storeMissing) {
        for (LiquidData liquidData : this.liquidData) {
            if (!missing.contains(liquidData)) {
                if (canFixLiquidData(liquidData, mapper)) {
                    liquidData.stillLiquid.itemID = mapper.applyAsInt(liquidData.stillLiquid.itemID);
                    liquidData.movingLiquid.itemID = mapper.applyAsInt(liquidData.movingLiquid.itemID);
                    liquidData.container.itemID = mapper.applyAsInt(liquidData.container.itemID);
                    liquidData.filled.itemID = mapper.applyAsInt(liquidData.filled.itemID);
                } else if (storeMissing) {
                    missing.add(liquidData);
                }
            }
        }
    }

    private boolean canFixLiquidData(LiquidData data, Int2IntFunction mapper) {
        return mapper.applyAsInt(data.stillLiquid.itemID) >= 0 &&
                mapper.applyAsInt(data.movingLiquid.itemID) >= 0 &&
                mapper.applyAsInt(data.container.itemID) >= 0 &&
                mapper.applyAsInt(data.filled.itemID) >= 0;
    }
}
