/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.extension.client;

public interface MinecraftAppletExtension {
    void setRelaunched(boolean relaunched);

    boolean isRelaunched();

    void fmlInitReentry();

    void fmlStartReentry();
}
