package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import fr.catcore.fabricatedforge.mixininterface.IThreadedAnvilChunkStorage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;
import net.minecraft.world.level.storage.WorldSaveException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.IOException;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin implements IThreadedAnvilChunkStorage {
    @Shadow @Final private File saveLocation;

    @Shadow protected abstract Chunk getChunk(World world, NbtCompound nbt);

    @Shadow protected abstract void putChunk(Chunk chunk, World world, NbtCompound nbt);

    @Shadow protected abstract void registerChunkChecker(ChunkPos pos, NbtCompound nbt);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    protected Chunk validateChunk(World par1World, int par2, int par3, NbtCompound par4NBTTagCompound) {
        if (!par4NBTTagCompound.contains("Level")) {
            System.out.println("Chunk file at " + par2 + "," + par3 + " is missing level data, skipping");
            return null;
        } else if (!par4NBTTagCompound.getCompound("Level").contains("Sections")) {
            System.out.println("Chunk file at " + par2 + "," + par3 + " is missing block data, skipping");
            return null;
        } else {
            Chunk var5 = this.getChunk(par1World, par4NBTTagCompound.getCompound("Level"));
            if (!var5.isChunkEqual(par2, par3)) {
                System.out
                        .println(
                                "Chunk file at "
                                        + par2
                                        + ","
                                        + par3
                                        + " is in the wrong location; relocating. (Expected "
                                        + par2
                                        + ", "
                                        + par3
                                        + ", got "
                                        + var5.chunkX
                                        + ", "
                                        + var5.chunkZ
                                        + ")"
                        );
                par4NBTTagCompound.putInt("xPos", par2);
                par4NBTTagCompound.putInt("zPos", par3);
                var5 = this.getChunk(par1World, par4NBTTagCompound.getCompound("Level"));
            }

            MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(var5, par4NBTTagCompound));
            return var5;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void writeChunk(World par1World, Chunk par2Chunk) throws WorldSaveException, IOException {
        par1World.readSaveLock();

        try {
            NbtCompound var3 = new NbtCompound();
            NbtCompound var4 = new NbtCompound();
            var3.put("Level", var4);
            this.putChunk(par2Chunk, par1World, var4);
            this.registerChunkChecker(par2Chunk.getChunkPos(), var3);
            MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Save(par2Chunk, var3));
        } catch (Exception var51) {
            var51.printStackTrace();
        }
    }

    @Override
    public File getSaveLocation() {
        return this.saveLocation;
    }
}
