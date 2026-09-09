/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.FLFCompatRegistries;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.SaveHandlerAccessor;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.SaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Mixin(SaveHandler.class)
public abstract class SaveHandlerMixin implements SaveHandlerAccessor {
    @Shadow
    protected abstract File getSaveDirectory();

    @Override
    public void flf$saveRegistries() {
        File dir = this.getSaveDirectory();
        File file = new File(dir, FLFCompatRegistries.FILE_NAME);

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("format", 1);
        NBTTagCompound registries = new NBTTagCompound();

        FLFCompatRegistries.write(registries);
        nbt.setTag("registries", registries);

        if (file.exists()) file.delete();

        try(OutputStream os = Files.newOutputStream(file.toPath())) {
            CompressedStreamTools.writeCompressed(nbt, os);
        } catch (IOException e) {

        }
    }
}
