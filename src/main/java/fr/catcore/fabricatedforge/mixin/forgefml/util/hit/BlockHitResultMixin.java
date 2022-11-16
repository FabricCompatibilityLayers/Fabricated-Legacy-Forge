package fr.catcore.fabricatedforge.mixin.forgefml.util.hit;

import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockHitResult.class)
public class BlockHitResultMixin {
    public int subHit = -1;
}
