/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import net.minecraft.src.ItemStack;
import net.minecraftforge.common.DungeonHooks;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(DungeonHooks.DungeonLoot.class)
public interface DungeonHooks_DungeonLootAccessor {
    @Accessor("itemStack")
    ItemStack getItemStack();
}
