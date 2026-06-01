/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.extension.client;

import cpw.mods.fml.client.ITextureFX;

import java.util.logging.Logger;

public interface IFMLTextureFXExtension extends ITextureFX {
    void setup();

    void superSetup();
    int getTileSizeBase();
    int getTileSizeMask();
    int getTileSizeSquare();
    int getTileSizeSquareMask();
    Logger getLogger();
}
