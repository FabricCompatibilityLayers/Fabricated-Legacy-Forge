/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.liquid;

import buildcraft.api.liquids.LiquidStack;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemUtils;
import net.minecraft.src.Block;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(LiquidStack.class)
public class LiquidStackMixin {
    @Redirect(method = "<init>(Lnet/minecraft/src/Block;I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;blockID:I", opcode = Opcodes.GETFIELD))
    private static int osl$fixItemBlockId(Block instance) {
        return ItemUtils.itemId(instance);
    }
}
