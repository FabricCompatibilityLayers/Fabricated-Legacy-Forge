/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.ornithemc.osl.registries.impl.mixin.common.NbtCompoundAccess;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingException;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingSource;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesNbtSerializer;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class OSLRegisterHelper {
    public static boolean loadFLFFile(File file) {
        if (file.exists()) {
            try(InputStream is = Files.newInputStream(file.toPath())) {
                NBTTagCompound nbt = CompressedStreamTools.readCompressed(is);
                NBTTagCompound readRegistries = nbt.getCompoundTag("registries");

                NBTTagCompound complete = new NBTTagCompound();
                SyncedRegistriesNbtSerializer.serialize(nbt);
                NBTTagCompound registries = complete.getCompoundTag("registries");

                for (Map.Entry<String, NBTBase> entry : ((NbtCompoundAccess) registries).accessElements().entrySet()) {
                    String key = entry.getKey();
                    NBTTagCompound entries = ((NBTTagCompound) entry.getValue());

                    if (readRegistries.hasKey(key)) {
                        for (Map.Entry<String, NBTBase> regEntry : ((NbtCompoundAccess) entries).accessElements().entrySet()) {
                            if (regEntry.getKey().startsWith("minecraft:")) {
                                readRegistries.getCompoundTag(key).setTag(regEntry.getKey(), regEntry.getValue().copy());
                            }
                        }
                    } else {
                        readRegistries.setTag(key, entries.copy());
                    }
                }

                SyncedRegistriesNbtSerializer.deserialize(nbt, RegistryMappingSource.WORLD_SAVE);
                SyncedRegistriesImpl.applyMappings();

                return true;
            } catch (IOException | RegistryMappingException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
