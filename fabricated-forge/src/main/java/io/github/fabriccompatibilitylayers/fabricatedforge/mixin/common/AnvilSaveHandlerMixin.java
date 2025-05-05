package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;

@Mixin(AnvilSaveHandler.class)
public class AnvilSaveHandlerMixin extends SaveHandler {
    public AnvilSaveHandlerMixin(File par1File, String par2Str, boolean par3) {
        super(par1File, par2Str, par3);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public IChunkLoader getChunkLoader(WorldProvider par1WorldProvider) {
        File var2 = this.getSaveDirectory();
        if (par1WorldProvider.getSaveFolder() != null)
        {
            File var3 = new File(var2, par1WorldProvider.getSaveFolder());
            var3.mkdirs();
            return new AnvilChunkLoader(var3);
        } else {
            return new AnvilChunkLoader(var2);
        }
    }
}
