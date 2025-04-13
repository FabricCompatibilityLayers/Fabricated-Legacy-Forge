package fr.catcore.fabricatedforge.mixin.forgefml.nbt;

import fr.catcore.cursedmixinextensions.annotations.Public;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.*;

@Environment(EnvType.SERVER)
@Mixin(NbtIo.class)
public abstract class Server_NbtIoMixin {

    @Shadow
    public static void method_1345(NbtCompound nbtCompound, DataOutput dataOutput) {
    }

    @Shadow
    public static NbtCompound read(DataInput input) {
        return null;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Public
    private static void method_1352(NbtCompound par0NBTTagCompound, File par1File) throws IOException {
        DataOutputStream var2 = new DataOutputStream(new FileOutputStream(par1File));

        try {
            method_1345(par0NBTTagCompound, var2);
        } finally {
            var2.close();
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Public
    private static NbtCompound method_1349(File par0File) throws IOException {
        if (!par0File.exists()) {
            return null;
        } else {
            DataInputStream var1 = new DataInputStream(new FileInputStream(par0File));

            NbtCompound var2;
            try {
                var2 = read(var1);
            } finally {
                var1.close();
            }

            return var2;
        }
    }
}
