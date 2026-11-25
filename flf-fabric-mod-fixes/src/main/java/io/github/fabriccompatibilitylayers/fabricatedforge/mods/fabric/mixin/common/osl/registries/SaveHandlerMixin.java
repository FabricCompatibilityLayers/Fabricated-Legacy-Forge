/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.registries;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.OSLRegisterHelper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.FLFCompatRegistries;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.File;

@Conditional(modLoaded = @Mod("osl-registries"))
@Mixin(value = RegistryMappingStorage.class)
public abstract class SaveHandlerMixin {
    @WrapOperation(method = "loadRegistryMappings", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/registries/impl/registry/sync/RegistryMappingStorage;readRegistryMappings(Ljava/io/File;Ljava/io/File;Z)Z", ordinal = 1))
    private static boolean lf$readFLFRegistryMappings(File dir, File file, boolean throwOnException, Operation<Boolean> original) {
        if (!original.call(dir, file, throwOnException)) {
            File file2 = new File(dir, FLFCompatRegistries.FILE_NAME);

            return OSLRegisterHelper.loadFLFFile(file2);
        }

        return true;
    }
}
