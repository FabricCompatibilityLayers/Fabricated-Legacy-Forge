/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.forged;

import net.minecraft.src.Block;

import java.util.Objects;

public final class HarvestData {
    public final Block block;
    public final int metadata;
    public final String toolClass;

    public HarvestData(Block block, int metadata, String toolClass) {
        this.block = block;
        this.metadata = metadata;
        this.toolClass = toolClass;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HarvestData)) return false;
        HarvestData that = (HarvestData) o;
        return metadata == that.metadata && Objects.equals(block, that.block) && Objects.equals(toolClass, that.toolClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, metadata, toolClass);
    }
}
