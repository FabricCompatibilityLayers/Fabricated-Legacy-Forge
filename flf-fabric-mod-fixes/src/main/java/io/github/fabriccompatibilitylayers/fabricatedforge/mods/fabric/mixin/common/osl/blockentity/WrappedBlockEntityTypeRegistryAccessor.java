/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blockentity;

import com.google.common.collect.BiMap;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.blockentities.impl.WrappedBlockEntityTypeRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Conditional(modLoaded = @Mod("osl-blockentities"))
@Mixin(WrappedBlockEntityTypeRegistry.class)
public interface WrappedBlockEntityTypeRegistryAccessor {
    @Accessor
    BiMap<NamespacedIdentifier, String> getLegacyIds();
}
