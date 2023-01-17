package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.catcore.fabricatedforge.mixininterface.IServerChunkProvider;
import fr.catcore.fabricatedforge.mixininterface.IWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.LongObjectStorage;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ChunkStorage;
import net.minecraft.world.chunk.ServerChunkProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Set;

@Mixin(ServerChunkProvider.class)
public abstract class ServerChunkProviderMixin implements ChunkProvider, IServerChunkProvider {

    @Shadow private ServerWorld world;

    @Shadow private Set<Long> chunksToUnload;

    @Shadow private LongObjectStorage chunkMap;

    @Shadow protected abstract Chunk method_2129(int i, int j);

    @Shadow private ChunkProvider chunkGenerator;

    @Shadow private Chunk empty;

    @Shadow private List<Chunk> chunks;

    @Shadow protected abstract void method_2128(Chunk chunk);

    @Shadow protected abstract void method_2126(Chunk chunk);

    @Shadow private ChunkStorage chunkWriter;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void scheduleUnload(int par1, int par2) {
        if (this.world.dimension.containsWorldSpawn() && DimensionManager.shouldLoadSpawn(this.world.dimension.dimensionType)) {
            BlockPos var3 = this.world.getWorldSpawnPos();
            int var4 = par1 * 16 + 8 - var3.x;
            int var5 = par2 * 16 + 8 - var3.z;
            short var6 = 128;
            if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
                this.chunksToUnload.add(ChunkPos.getIdFromCoords(par1, par2));
            }
        } else {
            this.chunksToUnload.add(ChunkPos.getIdFromCoords(par1, par2));
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public Chunk getOrGenerateChunk(int par1, int par2) {
        long var3 = ChunkPos.getIdFromCoords(par1, par2);
        this.chunksToUnload.remove(var3);
        Chunk var5 = (Chunk)this.chunkMap.get(var3);
        if (var5 == null) {
            var5 = ForgeChunkManager.fetchDormantChunk(var3, this.world);
            if (var5 == null) {
                var5 = this.method_2129(par1, par2);
            }

            if (var5 == null) {
                if (this.chunkGenerator == null) {
                    var5 = this.empty;
                } else {
                    try {
                        var5 = this.chunkGenerator.getChunk(par1, par2);
                    } catch (Throwable var9) {
                        CrashReport var7 = CrashReport.create(var9, "Exception generating new chunk");
                        CrashReportSection var8 = var7.addElement("Chunk to be generated");
                        var8.add("Location", String.format("%d,%d", par1, par2));
                        var8.add("Position hash", var3);
                        var8.add("Generator", this.chunkGenerator.getChunkProviderName());
                        throw new CrashException(var7);
                    }
                }
            }

            this.chunkMap.set(var3, var5);
            this.chunks.add(var5);
            if (var5 != null) {
                var5.loadToWorld();
            }

            var5.decorateChunk(this, this, par1, par2);
        }

        return var5;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void decorateChunk(ChunkProvider par1IChunkProvider, int par2, int par3) {
        Chunk var4 = this.getChunk(par2, par3);
        if (!var4.terrainPopulated) {
            var4.terrainPopulated = true;
            if (this.chunkGenerator != null) {
                this.chunkGenerator.decorateChunk(par1IChunkProvider, par2, par3);
                GameRegistry.generateWorld(par2, par3, this.world, this.chunkGenerator, par1IChunkProvider);
                var4.setModified();
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean tickChunks() {
        if (!this.world.savingDisabled) {
            for(ChunkPos forced : ((IWorld)this.world).getPersistentChunks().keySet()) {
                this.chunksToUnload.remove(ChunkPos.getIdFromCoords(forced.x, forced.z));
            }

            for(int var1 = 0; var1 < 100; ++var1) {
                if (!this.chunksToUnload.isEmpty()) {
                    Long var2 = (Long)this.chunksToUnload.iterator().next();
                    Chunk var3 = (Chunk)this.chunkMap.get(var2);
                    var3.unloadFromWorld();
                    this.method_2128(var3);
                    this.method_2126(var3);
                    this.chunksToUnload.remove(var2);
                    this.chunkMap.remove(var2);
                    this.chunks.remove(var3);
                    ForgeChunkManager.putDormantChunk(ChunkPos.getIdFromCoords(var3.chunkX, var3.chunkZ), var3);
                    if (this.chunks.size() == 0
                            && ForgeChunkManager.getPersistentChunksFor(this.world).size() == 0
                            && !DimensionManager.shouldLoadSpawn(this.world.dimension.dimensionType)) {
                        DimensionManager.unloadWorld(this.world.dimension.dimensionType);
                        return this.chunkGenerator.tickChunks();
                    }
                }
            }

            if (this.chunkWriter != null) {
                this.chunkWriter.method_3950();
            }
        }

        return this.chunkGenerator.tickChunks();
    }

    @Override
    public ChunkStorage getChunkWriter() {
        return this.chunkWriter;
    }
}
