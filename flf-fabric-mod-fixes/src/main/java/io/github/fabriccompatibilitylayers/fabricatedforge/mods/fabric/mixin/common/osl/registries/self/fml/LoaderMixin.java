/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.registries.self.fml;

import cpw.mods.fml.common.Loader;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Conditional(modLoaded = @Mod("osl-registries"))
@Mixin(Loader.class)
public class LoaderMixin {
    @Inject(method = "initializeMods", at = @At("RETURN"), remap = false)
    private void osl$updateRegistryMappings(CallbackInfo ci) {
        SyncedRegistriesImpl.init();
    }
}
