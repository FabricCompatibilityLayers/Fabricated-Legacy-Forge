package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.Block;
import net.minecraft.src.StatList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StatList.class)
public class StatListMixin {
    @ModifyConstant(method = "initMinableStats", constant = @Constant(intValue = 256))
    private static int forge$dehardcodeBlockLimit(int constant) {
        return Block.blocksList.length;
    }
}
