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
    @Shadow @Final
    public File saveLocation;

    @Shadow protected abstract Chunk getChunk(World world, NbtCompound nbt);

    @Shadow protected abstract void registerChunkChecker(ChunkPos pos, NbtCompound nbt);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public Chunk validateChunk(World par1World, int par2, int par3, NbtCompound par4NBTTagCompound) {
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
    public void putChunk(Chunk par1Chunk, World par2World, NbtCompound par3NBTTagCompound) {
        par3NBTTagCompound.putInt("xPos", par1Chunk.chunkX);
        par3NBTTagCompound.putInt("zPos", par1Chunk.chunkZ);
        par3NBTTagCompound.putLong("LastUpdate", par2World.getLastUpdateTime());
        par3NBTTagCompound.putIntArray("HeightMap", par1Chunk.heightmap);
        par3NBTTagCompound.putBoolean("TerrainPopulated", par1Chunk.terrainPopulated);
        ChunkSection[] var4 = par1Chunk.getBlockStorage();
        NbtList var5 = new NbtList("Sections");
        boolean var6 = !par2World.dimension.isNether;

        for(ChunkSection var10 : var4) {
            if (var10 != null) {
                NbtCompound var11 = new NbtCompound();
                var11.putByte("Y", (byte)(var10.getYOffset() >> 4 & 0xFF));
                var11.putByteArray("Blocks", var10.getBlocks());
                if (var10.method_3944() != null) {
                    var11.putByteArray("Add", var10.method_3944().bytes);
                }

                var11.putByteArray("Data", var10.getBlockData().bytes);
                var11.putByteArray("BlockLight", var10.getBlockLight().bytes);
                if (var6) {
                    var11.putByteArray("SkyLight", var10.getSkyLight().bytes);
                } else {
                    var11.putByteArray("SkyLight", new byte[var10.getBlockLight().bytes.length]);
                }

                var5.method_1217(var11);
            }
        }

        par3NBTTagCompound.put("Sections", var5);
        par3NBTTagCompound.putByteArray("Biomes", par1Chunk.getBiomeArray());
        par1Chunk.containsEntities = false;
        NbtList var16 = new NbtList();

        for(int var22 = 0; var22 < par1Chunk.entities.length; ++var22) {
            for(Entity var21 : (List<Entity>) par1Chunk.entities[var22]) {
                par1Chunk.containsEntities = true;
                NbtCompound var11 = new NbtCompound();

                try {
                    if (var21.saveToNbt(var11)) {
                        var16.method_1217(var11);
                    }
                } catch (Exception var23) {
                    FMLLog.log(
                            Level.SEVERE,
                            var23,
                            "An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
                            new Object[]{var21.getClass().getName()}
                    );
                }
            }
        }

        par3NBTTagCompound.put("Entities", var16);
        NbtList var17 = new NbtList();

        for(BlockEntity var22 : (Collection<BlockEntity>) par1Chunk.blockEntities.values()) {
            NbtCompound var11 = new NbtCompound();

            try {
                var22.toNbt(var11);
                var17.method_1217(var11);
            } catch (Exception var201) {
                FMLLog.log(
                        Level.SEVERE,
                        var201,
                        "A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
                        new Object[]{var22.getClass().getName()}
                );
            }
        }

        par3NBTTagCompound.put("TileEntities", var17);
        List<ScheduledTick> var20 = par2World.getScheduledTicks(par1Chunk, false);
        if (var20 != null) {
            long var19 = par2World.getLastUpdateTime();
            NbtList var12 = new NbtList();

            for(ScheduledTick var14 : var20) {
                NbtCompound var15 = new NbtCompound();
                var15.putInt("i", var14.blockId);
                var15.putInt("x", var14.x);
                var15.putInt("y", var14.y);
                var15.putInt("z", var14.z);
                var15.putInt("t", (int)(var14.time - var19));
                var12.method_1217(var15);
            }

            par3NBTTagCompound.put("TileTicks", var12);
        }
    }

    @Override
    public File getSaveLocation() {
        return this.saveLocation;
    }
}
