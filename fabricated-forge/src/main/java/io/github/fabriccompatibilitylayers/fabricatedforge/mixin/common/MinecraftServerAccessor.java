package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = MinecraftServer.class, priority = 1001)
public interface MinecraftServerAccessor {
    @Accessor(value = "spawnProtectionSize", remap = false)
    int getSpawnProtectionSize();
}
