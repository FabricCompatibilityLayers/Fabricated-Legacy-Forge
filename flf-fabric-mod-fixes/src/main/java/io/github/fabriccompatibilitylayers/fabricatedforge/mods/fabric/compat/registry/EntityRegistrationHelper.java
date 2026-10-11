/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry;

import com.google.common.collect.Maps;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.IdUtils;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.entities.WrappedEntityTypeRegistryAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.api.entity.EntityType;
import net.ornithemc.osl.entities.impl.entity.EntityTypeImpl;

import java.util.Map;

public class EntityRegistrationHelper {
    private static final Map<Class<? extends Entity>, String> entityIdentifiers = Maps.newHashMap();
    private static final Map<Class<? extends Entity>, Integer> entityIds = Maps.newHashMap();

    public static boolean register(Class <? extends Entity> entityClass, String entityName, int id) {
        String modId = IdUtils.getModId();
        String path = IdUtils.pascalCaseToSnakeCase(entityName);

        String entityIdentifier = modId + ":" + path;
        entityIdentifiers.put(entityClass, entityIdentifier);
        entityIds.put(entityClass, id);

        if (FabricLoader.getInstance().isModLoaded("osl-entities")) {
            NamespacedIdentifier identifier = NamespacedIdentifiers.from(modId, path);
            EntityType.Builder<?> type = EntityTypeImpl.Builder.of(entityClass);

            ((WrappedEntityTypeRegistryAccessor) EntityTypeRegistry.REGISTRY).getLegacyIds().put(identifier, entityName);
            EntityTypeRegistry.register(id, identifier, type);

            return false;
        } else {
            return true;
        }
    }

    public static boolean register(Class <? extends Entity> entityClass, String entityName, int id, int backgroundEggColour, int foregroundEggColour) {
        String modId = IdUtils.getModId();
        String path = IdUtils.pascalCaseToSnakeCase(entityName);

        String entityIdentifier = modId + ":" + path;
        entityIdentifiers.put(entityClass, entityIdentifier);
        entityIds.put(entityClass, id);

        if (FabricLoader.getInstance().isModLoaded("osl-entities")) {
            NamespacedIdentifier identifier = NamespacedIdentifiers.from(modId, path);
            EntityType.Builder<?> type = EntityTypeImpl.Builder.of(entityClass);

            ((WrappedEntityTypeRegistryAccessor) EntityTypeRegistry.REGISTRY).getLegacyIds().put(identifier, entityName);
            EntityTypeRegistry.registerSpawnEgg(EntityTypeRegistry.register(id, identifier, type), backgroundEggColour, foregroundEggColour);

            return false;
        } else {
            return true;
        }
    }

    public static void write(NBTTagCompound nbt) {
        for (Map.Entry<Class<? extends Entity>, String> entry : entityIdentifiers.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue().isEmpty()) continue;

            nbt.setInteger(entry.getValue(), entityIds.get(entry.getKey()));
        }
    }
}
