package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(value = Entity.class, priority = 1001)
public interface EntityAccessor {
    @Accessor("captureDrops")
    boolean isCaptureDrops();
    @Accessor("captureDrops")
    void setCaptureDrops(boolean captureDrops);

    @Accessor("capturedDrops")
    ArrayList<EntityItem> getCapturedDrops();
}
