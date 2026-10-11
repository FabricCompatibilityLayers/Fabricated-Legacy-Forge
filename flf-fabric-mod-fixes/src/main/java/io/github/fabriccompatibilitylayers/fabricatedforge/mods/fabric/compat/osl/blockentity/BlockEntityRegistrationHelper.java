/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.blockentity;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.IdUtils;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blockentity.WrappedBlockEntityTypeRegistryAccessor;
import net.minecraft.src.TileEntity;
import net.ornithemc.osl.blockentities.api.BlockEntityTypeRegistry;
import net.ornithemc.osl.blockentities.api.blockentity.BlockEntityType;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public class BlockEntityRegistrationHelper {
    public static void register(Class<? extends TileEntity> type, String name) {
        String modId = IdUtils.getModId();
        String path = Util.pascalCaseToSnakeCase(name);

        NamespacedIdentifier id = NamespacedIdentifiers.from(modId, path);

        Constructor<? extends TileEntity> constructor;
        try {
            constructor = type.getConstructor();
        } catch (NoSuchMethodException var3) {
            throw new IllegalArgumentException("Invalid class " + type + ": no constructor taking no arguments");
        }

        Supplier<? extends TileEntity> factory = () -> {
            try {
                return (TileEntity)constructor.newInstance();
            } catch (Throwable t) {
                throw new IllegalStateException("error creating block entity of type " + type, t);
            }
        };

        ((WrappedBlockEntityTypeRegistryAccessor) BlockEntityTypeRegistry.REGISTRY).getLegacyIds().put(id, name);

        BlockEntityTypeRegistry.register(
                id,
                BlockEntityType.Builder.of(type, factory)
        );
    }
}
