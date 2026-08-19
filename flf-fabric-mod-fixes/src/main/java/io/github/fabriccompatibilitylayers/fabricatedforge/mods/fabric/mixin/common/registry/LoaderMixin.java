/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry;

import cpw.mods.fml.common.Loader;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.BlockRegistrationHelper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.ItemRegistrationHelper;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Conditional(modAbsent = @Mod("osl-registries"))
@Mixin(Loader.class)
public class LoaderMixin {
    @Inject(method = "loadMods", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/LoadController;transition(Lcpw/mods/fml/common/LoaderState;)V", ordinal = 0))
    private void flf$isReady(CallbackInfo ci) {
        BlockRegistrationHelper.ready = true;
        ItemRegistrationHelper.ready = true;
    }
}
