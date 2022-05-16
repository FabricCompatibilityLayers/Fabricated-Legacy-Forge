package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.FileIoThread;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.RegionIo;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.class_1206;
import net.minecraft.world.level.storage.WorldSaveException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Shadow private Object field_4781;

    @Shadow private Set chunksBeingSaved;

    @Shadow private List field_4779;

    @Shadow @Final private File saveLocation;

    @Shadow protected abstract Chunk getChunk(World world, NbtCompound nbt);

    @Shadow protected abstract void putChunk(Chunk chunk, World world, NbtCompound nbt);

    @Shadow protected abstract void method_3975(class_1206 arg);

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public Chunk loadChunk(World par1World, int par2, int par3) throws IOException {
        NbtCompound var4 = null;
        ChunkPos var5 = new ChunkPos(par2, par3);
        Object var6 = this.field_4781;
        synchronized(this.field_4781) {
            if (this.chunksBeingSaved.contains(var5)) {
                Iterator var7 = this.field_4779.iterator();

                while(var7.hasNext()) {
                    class_1206 var8 = (class_1206)var7.next();
                    if (var8.field_4783.equals(var5)) {
                        var4 = var8.field_4784;
                        break;
                    }
                }
            }
        }

        if (var4 == null) {
            DataInputStream var11 = RegionIo.read(this.saveLocation, par2, par3);
            if (var11 == null) {
                return null;
            }

            var4 = NbtIo.read(var11);
        }

        return this.validateChunk(par1World, par2, par3, var4);
    }

    /**
     * @author Minecraft Forge
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
                System.out.println("Chunk file at " + par2 + "," + par3 + " is in the wrong location; relocating. (Expected " + par2 + ", " + par3 + ", got " + var5.chunkX + ", " + var5.chunkZ + ")");
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
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    protected void registerChunkChecker(ChunkPos par1ChunkCoordIntPair, NbtCompound par2NBTTagCompound) {
        Object var3 = this.field_4781;
        synchronized(this.field_4781) {
            if (this.chunksBeingSaved.contains(par1ChunkCoordIntPair)) {
                for(int var4 = 0; var4 < this.field_4779.size(); ++var4) {
                    if (((class_1206)this.field_4779.get(var4)).field_4783.equals(par1ChunkCoordIntPair)) {
                        this.field_4779.set(var4, new class_1206(par1ChunkCoordIntPair, par2NBTTagCompound));
                        return;
                    }
                }
            }

            this.field_4779.add(new class_1206(par1ChunkCoordIntPair, par2NBTTagCompound));
            this.chunksBeingSaved.add(par1ChunkCoordIntPair);
            FileIoThread.INSTANCE.registerCallback((ThreadedAnvilChunkStorage)(Object) this);
        }
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean saveNextChunk() {
        class_1206 var1 = null;
        Object var2 = this.field_4781;
        synchronized(this.field_4781) {
            if (this.field_4779.isEmpty()) {
                return false;
            }

            var1 = (class_1206)this.field_4779.remove(0);
            this.chunksBeingSaved.remove(var1.field_4783);
        }

        if (var1 != null) {
            try {
                this.method_3975(var1);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return true;
    }
}
