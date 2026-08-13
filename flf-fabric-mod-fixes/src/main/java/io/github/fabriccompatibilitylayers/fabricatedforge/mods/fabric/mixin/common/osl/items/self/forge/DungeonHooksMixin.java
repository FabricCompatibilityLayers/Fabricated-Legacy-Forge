/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemStackContainerListMapper;
import net.minecraftforge.common.DungeonHooks;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(DungeonHooks.class)
public class DungeonHooksMixin {
    @Shadow
    private static ArrayList<DungeonHooks.DungeonLoot> dungeonLoot;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("forge", "dungeon_hooks/dungeon_loot"),
                ItemStackContainerListMapper.of(dungeonLoot, (loot) -> ((DungeonHooks_DungeonLootAccessor) loot).getItemStack())
        );
    }
}
