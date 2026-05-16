package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Hashtable;

@Mixin(value = MinecraftServer.class, priority = 1001)
public interface MinecraftServerAccessor {
    @Accessor(value = "spawnProtectionSize", remap = false)
    int getSpawnProtectionSize();

    @Accessor(value = "worldTickTimes", remap = false)
    Hashtable<Integer, long[]> getWorldTickTimes();
}
