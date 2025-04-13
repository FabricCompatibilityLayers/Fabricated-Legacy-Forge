package fr.catcore.fabricatedforge.mixin.forgefml.util.hit;

import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockHitResult.class)
public class BlockHitResultMixin {

    @Unique
    public int subHit = -1;
}
