/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.server;

import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedfml.utils.ServerImplementation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.*;
import java.nio.file.Files;

@Environment(EnvType.SERVER)
@Mixin(CompressedStreamTools.class)
public abstract class CompressedStreamToolsMixin {
    @Shadow
    public static void write(NBTTagCompound par0NBTTagCompound, DataOutput par1DataOutput) {
    }

    @Shadow
    public static NBTTagCompound read(DataInput par0DataInput) {
        return null;
    }

    // Readd this client-only method to the server
    @ServerImplementation("b")
    @Public
    private static void write(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
        DataOutputStream var2 = new DataOutputStream(Files.newOutputStream(par1File.toPath()));

        try {
            write(par0NBTTagCompound, var2);
        } finally {
            var2.close();
        }
    }

    // Readd this client-only method to the server
    @ServerImplementation("a")
    @Public
    private static NBTTagCompound read(File par0File) throws IOException {
        if (!par0File.exists()) {
            return null;
        } else {
            DataInputStream var1 = new DataInputStream(Files.newInputStream(par0File.toPath()));

            NBTTagCompound var2;
            try {
                var2 = read(var1);
            } finally {
                var1.close();
            }

            return var2;
        }
    }
}
