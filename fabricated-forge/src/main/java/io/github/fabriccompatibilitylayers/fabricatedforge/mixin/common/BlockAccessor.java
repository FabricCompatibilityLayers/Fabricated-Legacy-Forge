package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = Block.class, priority = 1001)
@Pseudo
public interface BlockAccessor {
    @Invoker(value = "setBurnProperties", remap = false)
    static void callSetBurnProperties(int par1, int par2, int par3) {}
}
