/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import net.minecraft.src.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SoundManager.class, priority = 1001)
public interface SoundManagerAccessor {
    @Accessor("MUSIC_INTERVAL")
    static int getMusicInterval() {
        return 0;
    }

    @Accessor("MUSIC_INTERVAL")
    static void setMusicInterval(int value) {

    }
}
