/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.registries;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.blocks.BlockRegistrationHelper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemRegistrationHelper;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Conditional(modLoaded = @Mod("osl-registries"))
@Mixin(RegistriesImpl.class)
public class RegistriesImplMixin {
    @WrapWithCondition(method = "init", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/registries/impl/registry/RegistriesImpl;freeze()V", remap = false), remap = false)
    private static boolean flf$cancelFreezing() {
        return false;
    }

    @Inject(method = "init", at = @At("RETURN"), remap = false)
    private static void flf$ready(CallbackInfo ci) {
        BlockRegistrationHelper.ready = true;
        ItemRegistrationHelper.ready = true;
    }
}
