/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import java.io.File;

public interface WorldServerExtension extends WorldExtension {
    File getChunkSaveLocation();
}