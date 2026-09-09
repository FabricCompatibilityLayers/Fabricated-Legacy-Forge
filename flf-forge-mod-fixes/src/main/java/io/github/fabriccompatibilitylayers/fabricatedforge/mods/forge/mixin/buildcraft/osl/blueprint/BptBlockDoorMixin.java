/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.blueprint;

import buildcraft.api.bptblocks.BptBlockDoor;
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
@Mixin(BptBlockDoor.class)
public class BptBlockDoorMixin implements BptBlockItemRemap {
    @Shadow
    @Final
    ItemStack stack;

    @Override
    public int osl$getItemId() {
        return stack.itemID;
    }

    @Override
    public void osl$setItemId(int id) {
        stack.itemID = id;
    }
}
