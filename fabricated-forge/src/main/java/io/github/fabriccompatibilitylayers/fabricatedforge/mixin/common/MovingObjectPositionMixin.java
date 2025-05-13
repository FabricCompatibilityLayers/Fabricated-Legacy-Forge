package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MovingObjectPosition.class)
public class MovingObjectPositionMixin {
    /** Used to determine what sub-segment is hit */
    public int subHit = -1;
}
