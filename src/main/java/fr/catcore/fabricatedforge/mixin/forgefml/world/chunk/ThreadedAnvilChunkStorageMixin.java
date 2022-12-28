package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import cpw.mods.fml.common.FMLLog;
import fr.catcore.fabricatedforge.mixininterface.IThreadedAnvilChunkStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ScheduledTick;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
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
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin implements IThreadedAnvilChunkStorage {
    @Shadow @Final private File saveLocation;

    @Shadow protected abstract Chunk getChunk(World world, NbtCompound nbt);

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

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    private void putChunk(Chunk par1Chunk, World par2World, NbtCompound par3NBTTagCompound) {
        par3NBTTagCompound.putInt("xPos", par1Chunk.chunkX);
        par3NBTTagCompound.putInt("zPos", par1Chunk.chunkZ);
        par3NBTTagCompound.putLong("LastUpdate", par2World.getLastUpdateTime());
        par3NBTTagCompound.putIntArray("HeightMap", par1Chunk.heightmap);
        par3NBTTagCompound.putBoolean("TerrainPopulated", par1Chunk.terrainPopulated);
        ChunkSection[] var4 = par1Chunk.getBlockStorage();
        NbtList var5 = new NbtList("Sections");

        for(ChunkSection var9 : var4) {
            if (var9 != null) {
                NbtCompound var10 = new NbtCompound();
                var10.putByte("Y", (byte)(var9.getYOffset() >> 4 & 0xFF));
                var10.putByteArray("Blocks", var9.getBlocks());
                if (var9.method_3944() != null) {
                    var10.putByteArray("Add", var9.method_3944().bytes);
                }

                var10.putByteArray("Data", var9.getBlockData().bytes);
                var10.putByteArray("SkyLight", var9.getSkyLight().bytes);
                var10.putByteArray("BlockLight", var9.getBlockLight().bytes);
                var5.method_1217(var10);
            }
        }

        par3NBTTagCompound.put("Sections", var5);
        par3NBTTagCompound.putByteArray("Biomes", par1Chunk.getBiomeArray());
        par1Chunk.containsEntities = false;
        NbtList var15 = new NbtList();

        for(int var21 = 0; var21 < par1Chunk.entities.length; ++var21) {
            for(Entity var19 : (List<Entity>) par1Chunk.entities[var21]) {
                par1Chunk.containsEntities = true;
                NbtCompound var10 = new NbtCompound();

                try {
                    if (var19.saveToNbt(var10)) {
                        var15.method_1217(var10);
                    }
                } catch (Exception var201) {
                    FMLLog.log(
                            Level.SEVERE,
                            var201,
                            "An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
                            new Object[]{var19.getClass().getName()}
                    );
                }
            }
        }

        par3NBTTagCompound.put("Entities", var15);
        NbtList var16 = new NbtList();

        for(BlockEntity var21 : (Collection<BlockEntity>) par1Chunk.blockEntities.values()) {
            NbtCompound var10 = new NbtCompound();

            try {
                var21.toNbt(var10);
                var16.method_1217(var10);
            } catch (Exception var19) {
                FMLLog.log(
                        Level.SEVERE,
                        var19,
                        "A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
                        new Object[]{var21.getClass().getName()}
                );
            }
        }

        par3NBTTagCompound.put("TileEntities", var16);
        List<ScheduledTick> var18 = par2World.getScheduledTicks(par1Chunk, false);
        if (var18 != null) {
            long var20 = par2World.getLastUpdateTime();
            NbtList var11 = new NbtList();

            for(ScheduledTick var13 : var18) {
                NbtCompound var14 = new NbtCompound();
                var14.putInt("i", var13.blockId);
                var14.putInt("x", var13.x);
                var14.putInt("y", var13.y);
                var14.putInt("z", var13.z);
                var14.putInt("t", (int)(var13.time - var20));
                var11.method_1217(var14);
            }

            par3NBTTagCompound.put("TileTicks", var11);
        }
    }

    @Override
    public File getSaveLocation() {
        return this.saveLocation;
    }
}
