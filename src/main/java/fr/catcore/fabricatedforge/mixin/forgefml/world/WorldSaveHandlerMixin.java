package fr.catcore.fabricatedforge.mixin.forgefml.world;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin {

    @Shadow @Final private File worldDir;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public LevelProperties getLevelProperties() {
        File var1 = new File(this.worldDir, "level.dat");
        LevelProperties worldInfo = null;
        if (var1.exists()) {
            try {
                NbtCompound var2 = NbtIo.readCompressed(Files.newInputStream(var1.toPath()));
                NbtCompound var3 = var2.getCompound("Data");
                worldInfo = new LevelProperties(var3);
                FMLCommonHandler.instance().handleWorldDataLoad((WorldSaveHandler)(Object) this, worldInfo, var2);
                return worldInfo;
            } catch (Exception var7) {
                if (FMLCommonHandler.instance().shouldServerBeKilledQuietly()) {
                    throw (RuntimeException)var7;
                }
                var7.printStackTrace();
            }
        }

        var1 = new File(this.worldDir, "level.dat_old");
        if (var1.exists()) {
            try {
                NbtCompound var2 = NbtIo.readCompressed(Files.newInputStream(var1.toPath()));
                NbtCompound var3 = var2.getCompound("Data");
                worldInfo = new LevelProperties(var3);
                FMLCommonHandler.instance().handleWorldDataLoad((WorldSaveHandler)(Object) this, worldInfo, var2);
                return worldInfo;
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        return null;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void saveWorld(LevelProperties par1WorldInfo, NbtCompound par2NBTTagCompound) {
        NbtCompound var3 = par1WorldInfo.toNbt(par2NBTTagCompound);
        NbtCompound var4 = new NbtCompound();
        var4.put("Data", var3);
        FMLCommonHandler.instance().handleWorldDataSave((WorldSaveHandler)(Object) this, par1WorldInfo, var4);

        try {
            File var5 = new File(this.worldDir, "level.dat_new");
            File var6 = new File(this.worldDir, "level.dat_old");
            File var7 = new File(this.worldDir, "level.dat");
            NbtIo.writeCompressed(var4, Files.newOutputStream(var5.toPath()));
            if (var6.exists()) {
                var6.delete();
            }

            var7.renameTo(var6);
            if (var7.exists()) {
                var7.delete();
            }

            var5.renameTo(var7);
            if (var5.exists()) {
                var5.delete();
            }
        } catch (Exception var81) {
            var81.printStackTrace();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void saveWorld(LevelProperties par1WorldInfo) {
        NbtCompound var2 = par1WorldInfo.toNbt();
        NbtCompound var3 = new NbtCompound();
        var3.put("Data", var2);
        FMLCommonHandler.instance().handleWorldDataSave((WorldSaveHandler)(Object) this, par1WorldInfo, var3);

        try {
            File var4 = new File(this.worldDir, "level.dat_new");
            File var5 = new File(this.worldDir, "level.dat_old");
            File var6 = new File(this.worldDir, "level.dat");
            NbtIo.writeCompressed(var3, Files.newOutputStream(var4.toPath()));
            if (var5.exists()) {
                var5.delete();
            }

            var6.renameTo(var5);
            if (var6.exists()) {
                var6.delete();
            }

            var4.renameTo(var6);
            if (var4.exists()) {
                var4.delete();
            }
        } catch (Exception var71) {
            var71.printStackTrace();
        }
    }
}
