/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.entities;

import com.google.common.collect.BiMap;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.impl.WrappedEntityTypeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Conditional(modLoaded = @Mod("osl-entities"))
@Mixin(WrappedEntityTypeRegistry.class)
public interface WrappedEntityTypeRegistryAccessor {
    @Accessor
    BiMap<NamespacedIdentifier, String> getLegacyIds();
}
