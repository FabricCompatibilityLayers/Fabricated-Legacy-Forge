/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl;

import buildcraft.api.core.BuildCraftAPI;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.BooleanArrayMapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Conditional(modLoaded = {@Mod("osl-blocks")})
@Mixin(BuildCraftAPI.class)
public class BuildCraftAPIMixin {
    @Shadow
    public static boolean[] softBlocks;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("buildcraft", "buildcraft_api/soft_blocks"), BooleanArrayMapper.of(
                () -> softBlocks,
                a -> softBlocks = a
        ));
    }
}
