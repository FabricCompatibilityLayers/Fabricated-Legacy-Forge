/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import net.minecraft.src.BlockDispenser;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockDispenser.class)
public class BlockDispenserMixin {
    // The provided patch is outdated and not applied
//    @Inject(method = "func_72283_a", at = @At(value = "FIELD", target = "Lnet/minecraft/src/ItemStack;field_77993_c:I", ordinal = 0), cancellable = true)
//    private static void fml$tryDispense(TileEntityDispenser p_72283_0_, World p_72283_1_, ItemStack p_72283_2_, Random p_72283_3_, int p_72283_4_, int p_72283_5_, int p_72283_6_, int p_72283_7_, int p_72283_8_, double p_72283_9_, double p_72283_11_, double p_72283_13_, CallbackInfoReturnable<Integer> cir) {
//        int modDispense = GameRegistry.tryDispense(p_72283_1_, p_72283_4_, p_72283_5_, p_72283_6_, p_72283_7_, p_72283_8_, p_72283_2_, p_72283_3_, p_72283_9_, p_72283_11_, p_72283_13_);
//        if (modDispense > -1)
//        {
//            cir.setReturnValue(modDispense);
//        }
//    }
}
