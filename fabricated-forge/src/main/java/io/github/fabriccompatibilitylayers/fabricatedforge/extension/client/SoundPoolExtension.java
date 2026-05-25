package io.github.fabriccompatibilitylayers.fabricatedforge.extension.client;

import net.minecraft.src.SoundPoolEntry;

import java.net.URL;

public interface SoundPoolExtension {
    SoundPoolEntry addSound(String par1Str, URL url);
}