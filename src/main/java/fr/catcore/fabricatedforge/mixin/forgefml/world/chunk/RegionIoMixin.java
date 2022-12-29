package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.minecraft.world.chunk.RegionFileFormat;
import net.minecraft.world.chunk.RegionIo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Mixin(RegionIo.class)
public abstract class RegionIoMixin {
    @Shadow @Final private static Map<File, RegionFileFormat> FORMATS;

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    public static synchronized RegionFileFormat create(File par0File, int par1, int par2) {
        File var3 = new File(par0File, "region");
        File var4 = new File(var3, "r." + (par1 >> 5) + "." + (par2 >> 5) + ".mca");
        RegionFileFormat var6 = (RegionFileFormat)FORMATS.get(var4);
        if (var6 != null) {
            return var6;
        } else {
            if (!var3.exists()) {
                var3.mkdirs();
            }

            if (FORMATS.size() >= 256) {
                clearRegionFormats();
            }

            var6 = new RegionFileFormat(var4);
            FORMATS.put(var4, var6);
            return var6;
        }
    }

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    public static synchronized void clearRegionFormats() {
        for(RegionFileFormat var2 : FORMATS.values()) {
            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (Exception var20) {
                var20.printStackTrace();
            }
        }

        FORMATS.clear();
    }
}
