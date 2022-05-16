package fr.catcore.fabricatedforge.mixin.forgefml.util.hit;

import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HitResult.class)
public class HitResultMixin {

    @Unique
    public int subHit = -1;
}
