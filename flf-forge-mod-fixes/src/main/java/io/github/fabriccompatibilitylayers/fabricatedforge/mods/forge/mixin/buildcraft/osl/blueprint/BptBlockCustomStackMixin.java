/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.blueprint;

import buildcraft.api.bptblocks.BptBlockCustomStack;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint.BptBlockItemRemap;
import net.minecraft.src.ItemStack;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(BptBlockCustomStack.class)
public class BptBlockCustomStackMixin implements BptBlockItemRemap {
    @Shadow
    @Final
    ItemStack customStack;

    @Override
    public int osl$getItemId() {
        return customStack.itemID;
    }

    @Override
    public void osl$setItemId(int id) {
        customStack.itemID = id;
    }
}
