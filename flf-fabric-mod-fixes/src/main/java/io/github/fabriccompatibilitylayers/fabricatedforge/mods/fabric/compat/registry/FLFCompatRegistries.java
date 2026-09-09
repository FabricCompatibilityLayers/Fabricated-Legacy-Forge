/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry;

import net.minecraft.src.NBTTagCompound;

import java.util.function.Consumer;

public enum FLFCompatRegistries {
    BLOCK("minecraft:block", BlockRegistrationHelper::write),
    ITEM("minecraft:item", ItemRegistrationHelper::write);

    private final String key;
    private final Consumer<NBTTagCompound> saveHandler;

    FLFCompatRegistries(String key, Consumer<NBTTagCompound> saveHandler) {
        this.key = key;
        this.saveHandler = saveHandler;
    }

    public static void write(NBTTagCompound nbt) {
        for (FLFCompatRegistries registry : values()) {
            NBTTagCompound registryNbt = new NBTTagCompound();
            registry.saveHandler.accept(registryNbt);
            nbt.setTag(registry.key, registryNbt);
        }
    }

    public static final String FILE_NAME = "flf_registry_mappings.dat";
}
