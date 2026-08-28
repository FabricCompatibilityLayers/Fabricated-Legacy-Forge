/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.blueprint;

import buildcraft.api.bptblocks.BptBlockDelegate;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint.BptBlockBlockRemap;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.*;

@Pseudo
@Conditional(modLoaded = {@Mod("osl-blocks")})
@Mixin(BptBlockDelegate.class)
public class BptBlockDelegateMixin implements BptBlockBlockRemap {
    @Mutable
    @Shadow
    @Final
    int delegateTo;

    @Override
    public int osl$getBlockId() {
        return delegateTo;
    }

    @Override
    public void osl$setBlockId(int id) {
        delegateTo = id;
    }
}
