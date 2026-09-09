/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.forged;

import java.util.Objects;

public final class ToolData {
    public final String type;
    public final int level;

    public ToolData(String type, int level) {
        this.type = type;
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToolData)) return false;
        ToolData toolData = (ToolData) o;
        return level == toolData.level && Objects.equals(type, toolData.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level);
    }
}
