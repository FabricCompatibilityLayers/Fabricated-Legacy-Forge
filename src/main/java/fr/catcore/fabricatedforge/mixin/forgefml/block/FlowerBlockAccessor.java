package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FlowerBlock.class)
public interface FlowerBlockAccessor {

    @Invoker("method_283")
    boolean method_283_invoker(int i);
}
