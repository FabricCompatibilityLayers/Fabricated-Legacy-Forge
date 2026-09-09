/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.fuels;

import buildcraft.api.fuels.IronEngineCoolant;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.liquid.LiquidStackContainerListMapper;
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

import java.util.LinkedList;

@Pseudo
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(IronEngineCoolant.class)
public class IronEngineCoolantMixin {
    @Shadow
    public static LinkedList<IronEngineCoolant> coolants;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("buildcraft", "iron_engine_coolant/liquid"),
                LiquidStackContainerListMapper.of(coolants, c -> c.liquid));
    }
}
