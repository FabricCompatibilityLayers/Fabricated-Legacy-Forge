package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import java.io.File;

public interface WorldServerExtension extends WorldExtension {
    File getChunkSaveLocation();
}