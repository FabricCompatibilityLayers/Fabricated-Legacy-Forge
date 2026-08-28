/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.blueprint;

import buildcraft.api.blueprints.BlueprintManager;
import buildcraft.api.blueprints.BptBlock;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint.BptBlockBlockMapper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint.BptBlockIdFixer;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint.BptBlockItemMapper;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Conditional(modLoaded = {@Mod("osl-items"), @Mod("osl-blocks")})
@Mixin(BlueprintManager.class)
public class BlueprintManagerMixin {
    @Shadow
    public static BptBlock[] blockBptProps;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMappersAndFixers(CallbackInfo ci) {
        SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("buildcraft", "blueprint_manager/block_bpt_props"), BptBlockBlockMapper.of(
                () -> blockBptProps,
                a -> blockBptProps = a
        ));
        SyncedRegistries.registerFixer(RegistryKeys.BLOCK, NamespacedIdentifiers.from("buildcraft", "bpt_block/block_id"), new BptBlockIdFixer());

        SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("buildcraft", "blueprint_manager/block_bpt_props"), BptBlockItemMapper.of(
                () -> blockBptProps,
                a -> blockBptProps = a
        ));
    }
}
