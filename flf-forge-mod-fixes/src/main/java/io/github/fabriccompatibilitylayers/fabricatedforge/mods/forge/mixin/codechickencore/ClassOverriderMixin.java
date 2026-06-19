/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.codechickencore;

import codechicken.core.asm.ClassOverrider;
import codechicken.core.asm.ObfuscationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ClassOverrider.class)
public class ClassOverriderMixin {
    @Inject(method = "overrideBytes", remap = false, at = @At("HEAD"), cancellable = true)
    private static void disableClassOverwriting(String name, byte[] bytes, ObfuscationManager.ClassMapping classMapping, File location, CallbackInfoReturnable<byte[]> cir) {
        if (classMapping.classname.equals(name)) {
            System.out.println("[ClassOverrider] Canceled class overwrite of " + name);
        }

        cir.setReturnValue(bytes);
    }
}
