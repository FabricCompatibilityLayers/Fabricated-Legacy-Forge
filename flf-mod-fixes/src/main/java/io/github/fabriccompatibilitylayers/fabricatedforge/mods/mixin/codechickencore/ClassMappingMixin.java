/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.mixin.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationManager.ClassMapping.class)
public class ClassMappingMixin {
    @Shadow(remap = false) public String classname;

    @Inject(method = "<init>", remap = false, at = @At("RETURN"))
    private void remapClassName(String classname, CallbackInfo ci) {
        if (!this.classname.contains(".")) {
            this.classname = MappingsHelper.mapClass(this.classname).replace("/", ".");
        }
    }
}
