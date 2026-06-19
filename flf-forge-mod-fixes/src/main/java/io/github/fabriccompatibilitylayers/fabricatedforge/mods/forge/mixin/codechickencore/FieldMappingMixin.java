/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.MappingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationManager.FieldMapping.class)
public class FieldMappingMixin {
    @Shadow(remap = false) public String owner;

    @Shadow(remap = false) public String name;

    @Shadow(remap = false) public String type;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void remap(String declaringclass, String fieldname, String type, CallbackInfo ci) {
        if (!this.owner.contains(".")) {
            this.owner = MappingsHelper.mapClass(this.owner);
        }
        MappingUtils.ClassMember pair = MappingsHelper.mapFieldFromRemappedClass(this.owner, this.name, this.type);
        this.name = pair.getName();
        this.type = MappingsHelper.mapDescriptor(pair.getDesc());
    }
}
