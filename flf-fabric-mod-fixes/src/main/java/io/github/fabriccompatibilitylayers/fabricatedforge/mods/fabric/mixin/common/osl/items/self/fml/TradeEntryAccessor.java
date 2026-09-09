/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.fml;

import net.minecraft.src.TradeEntry;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(TradeEntry.class)
public interface TradeEntryAccessor {
    @Mutable
    @Accessor("id")
    void setId(int id);
}
