package fr.catcore.fabricatedforge.mixin.forgefml.world;

import net.minecraft.world.AnvilWorldSaveHandler;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkStorage;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;

@Mixin(AnvilWorldSaveHandler.class)
public class AnvilWorldSaveHandlerMixin extends WorldSaveHandler {

    public AnvilWorldSaveHandlerMixin(File savesFolder, String worldName, boolean createPlayerDataDir) {
        super(savesFolder, worldName, createPlayerDataDir);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public ChunkStorage getChunkWriter(Dimension par1WorldProvider) {
        File var2 = this.method_198();
        if (par1WorldProvider.getSaveFolder() != null) {
            File var3 = new File(var2, par1WorldProvider.getSaveFolder());
            var3.mkdirs();
            return new ThreadedAnvilChunkStorage(var3);
        } else {
            return new ThreadedAnvilChunkStorage(var2);
        }
    }
}
