package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = World.class, priority = 1001)
public interface WorldAccessor {
    @Accessor("MAX_ENTITY_RADIUS")
    static double getMaxEntityRadius() {
        return 0.0;
    }
}
