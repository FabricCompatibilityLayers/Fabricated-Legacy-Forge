package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.server;

import fr.catcore.cursedmixinextensions.annotations.Public;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.*;

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

    private static void forge$server$write(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
        DataOutputStream var2 = new DataOutputStream(new FileOutputStream(par1File));

        try {
            write(par0NBTTagCompound, var2);
        } finally {
            var2.close();
        }
    }

    // Readd this client-only method to the server
    // mcp
    @Public
    private static void write(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
        forge$server$write(par0NBTTagCompound, par1File);
    }

    // legacy fabric 1
    @Public
    private static void method_1352(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
        forge$server$write(par0NBTTagCompound, par1File);
    }

    // ornithe gen 1
    @Public
    private static void m_7680559(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
        forge$server$write(par0NBTTagCompound, par1File);
    }

    private static NBTTagCompound forge$server$read(File par0File) throws IOException {
        if (!par0File.exists()) {
            return null;
        } else {
            DataInputStream var1 = new DataInputStream(new FileInputStream(par0File));

            NBTTagCompound var2;
            try {
                var2 = read(var1);
            } finally {
                var1.close();
            }

            return var2;
        }
    }

    // Readd this client-only method to the server
    // mcp
    @Public
    private static NBTTagCompound read(File par0File) throws IOException {
        return forge$server$read(par0File);
    }

    // legacy fabric 1
    @Public
    private static NBTTagCompound method_1349(File par0File) throws IOException {
        return forge$server$read(par0File);
    }

    // ornithe gen 1
    @Public
    private static NBTTagCompound m_3179139(File par0File) throws IOException {
        return forge$server$read(par0File);
    }
}
