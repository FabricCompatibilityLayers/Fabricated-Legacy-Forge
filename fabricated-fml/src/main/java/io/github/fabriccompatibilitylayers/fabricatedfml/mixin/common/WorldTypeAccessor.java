package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = WorldType.class, priority = 1001)
@Pseudo
public interface WorldTypeAccessor {
    @Accessor(value = "base11Biomes", remap = false)
    static BiomeGenBase[] getBase11Biomes() {
        return null;
    }

    @Accessor(value = "base12Biomes", remap = false)
    static BiomeGenBase[] getBase12Biomes() {
        return null;
    }
}
