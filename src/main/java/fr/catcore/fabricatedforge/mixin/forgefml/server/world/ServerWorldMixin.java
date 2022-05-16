package fr.catcore.fabricatedforge.mixin.forgefml.server.world;

import fr.catcore.fabricatedforge.mixininterface.IServerWorld;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.TickableEntry;
import net.minecraft.util.class_797;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ServerChunkProvider;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.feature.BonusChestFeature;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.WorldSaveException;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.*;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements IServerWorld {
    @Shadow private boolean ready;

    @Shadow private Set entityNavigations;

    @Shadow private TreeSet field_2812;

    @Shadow private int idleTimeout;

    @Shadow @Final private MinecraftServer server;

    @Shadow protected abstract void method_2132();

    @Shadow private class_797[] field_2815;

    @Shadow private int field_2816;

    @Shadow public ServerChunkProvider chunkCache;

    public ServerWorldMixin(SaveHandler saveHandler, String string, Dimension dimension, LevelInfo levelInfo, Profiler profiler) {
        super(saveHandler, string, dimension, levelInfo, profiler);
    }

    @Unique
    protected Set<ChunkPos> doneChunks = new HashSet<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(MinecraftServer par1MinecraftServer, SaveHandler par2ISaveHandler, String par3Str, int par4, LevelInfo par5WorldSettings, Profiler par6Profiler, CallbackInfo ci) {
        DimensionManager.setWorld(par4, (ServerWorld)(Object) this);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private void resetWeather() {
        this.dimension.resetRainAndThunder();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean isReady() {
        if (this.ready && !this.isClient) {
            for (Object playerEntity : this.playerEntities) {
                PlayerEntity var2 = (PlayerEntity) playerEntity;
                if (!var2.isSleepingLongEnough()) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    protected void tickBlocks() {
        super.tickBlocks();
        int var1 = 0;
        int var2 = 0;
        Iterator var3 = this.field_4530.iterator();
        this.doneChunks.retainAll(this.field_4530);
        if (this.doneChunks.size() == this.field_4530.size()) {
            this.doneChunks.clear();
        }

        long time = -System.currentTimeMillis();

        while(var3.hasNext()) {
            ChunkPos var4 = (ChunkPos)var3.next();
            int var5 = var4.x * 16;
            int var6 = var4.z * 16;
            this.profiler.push("getChunk");
            Chunk var7 = this.getChunk(var4.x, var4.z);
            this.method_3605(var5, var6, var7);
            this.profiler.swap("tickChunk");
            if (System.currentTimeMillis() + time <= 4L && this.doneChunks.add(var4)) {
                var7.method_3919();
            }

            this.profiler.swap("thunder");
            int var8;
            int var9;
            int var10;
            int var11;
            if (this.dimension.canDoLightning(var7) && this.random.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
                this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
                var8 = this.lcgBlockSeed >> 2;
                var9 = var5 + (var8 & 15);
                var10 = var6 + (var8 >> 8 & 15);
                var11 = this.method_3703(var9, var10);
                if (this.method_3580(var9, var11, var10)) {
                    this.addEntity(new LightningBoltEntity(this, (double)var9, (double)var11, (double)var10));
                    this.field_4553 = 2;
                }
            }

            this.profiler.swap("iceandsnow");
            int var13;
            if (this.dimension.canDoRainSnowIce(var7) && this.random.nextInt(16) == 0) {
                this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
                var8 = this.lcgBlockSeed >> 2;
                var9 = var8 & 15;
                var10 = var8 >> 8 & 15;
                var11 = this.method_3703(var9 + var5, var10 + var6);
                if (this.method_3732(var9 + var5, var11 - 1, var10 + var6)) {
                    this.method_3690(var9 + var5, var11 - 1, var10 + var6, Block.ICE.id);
                }

                if (this.isRaining() && this.method_3734(var9 + var5, var11, var10 + var6)) {
                    this.method_3690(var9 + var5, var11, var10 + var6, Block.SNOW_LAYER.id);
                }

                if (this.isRaining()) {
                    Biome var12 = this.method_3773(var9 + var5, var10 + var6);
                    if (var12.method_3830()) {
                        var13 = this.getBlock(var9 + var5, var11 - 1, var10 + var6);
                        if (var13 != 0) {
                            Block.BLOCKS[var13].method_457(this, var9 + var5, var11 - 1, var10 + var6);
                        }
                    }
                }
            }

            this.profiler.swap("tickTiles");
            ChunkSection[] var19 = var7.getBlockStorage();
            var9 = var19.length;

            for(var10 = 0; var10 < var9; ++var10) {
                ChunkSection var21 = var19[var10];
                if (var21 != null && var21.hasTickableBlocks()) {
                    for(int var20 = 0; var20 < 3; ++var20) {
                        this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
                        var13 = this.lcgBlockSeed >> 2;
                        int var14 = var13 & 15;
                        int var15 = var13 >> 8 & 15;
                        int var16 = var13 >> 16 & 15;
                        int var17 = var21.method_3926(var14, var16, var15);
                        ++var2;
                        Block var18 = Block.BLOCKS[var17];
                        if (var18 != null && var18.ticksRandomly()) {
                            ++var1;
                            var18.method_436(this, var14 + var5, var16 + var21.getYOffset(), var15 + var6, this.random);
                        }
                    }
                }
            }

            this.profiler.pop();
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_3599(int par1, int par2, int par3, int par4, int par5) {
        TickableEntry var6 = new TickableEntry(par1, par2, par3, par4);
        boolean isForced = this.getPersistentChunks().containsKey(new ChunkPos(var6.x >> 4, var6.z >> 4));
        int var7 = isForced ? 0 : 8;
        if (this.immediateUpdates) {
            if (this.isRegionLoaded(var6.x - var7, var6.y - var7, var6.z - var7, var6.x + var7, var6.y + var7, var6.z + var7)) {
                int var8 = this.getBlock(var6.x, var6.y, var6.z);
                if (var8 == var6.blockId && var8 > 0) {
                    Block.BLOCKS[var8].method_436(this, var6.x, var6.y, var6.z, this.random);
                }
            }
        } else if (this.isRegionLoaded(par1 - var7, par2 - var7, par3 - var7, par1 + var7, par2 + var7, par3 + var7)) {
            if (par4 > 0) {
                var6.setTime((long)par5 + this.levelProperties.getTimeOfDay());
            }

            if (!this.entityNavigations.contains(var6)) {
                this.entityNavigations.add(var6);
                this.field_2812.add(var6);
            }
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void tickEntities() {
        if (this.playerEntities.isEmpty() && this.getPersistentChunks().isEmpty()) {
            if (this.idleTimeout++ >= 60) {
                return;
            }
        } else {
            this.idleTimeout = 0;
        }

        super.tickEntities();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3644(boolean par1) {
        int var2 = this.field_2812.size();
        if (var2 != this.entityNavigations.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (var2 > 1000) {
                var2 = 1000;
            }

            for(int var3 = 0; var3 < var2; ++var3) {
                TickableEntry var4 = (TickableEntry)this.field_2812.first();
                if (!par1 && var4.time > this.levelProperties.getTimeOfDay()) {
                    break;
                }

                this.field_2812.remove(var4);
                this.entityNavigations.remove(var4);
                boolean isForced = this.getPersistentChunks().containsKey(new ChunkPos(var4.x >> 4, var4.z >> 4));
                int var5 = isForced ? 0 : 8;
                if (this.isRegionLoaded(var4.x - var5, var4.y - var5, var4.z - var5, var4.x + var5, var4.y + var5, var4.z + var5)) {
                    int var6 = this.getBlock(var4.x, var4.y, var4.z);
                    if (var6 == var4.blockId && var6 > 0) {
                        Block.BLOCKS[var6].method_436(this, var4.x, var4.y, var4.z, this.random);
                    }
                }
            }

            return !this.field_2812.isEmpty();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public List method_2134(int par1, int par2, int par3, int par4, int par5, int par6) {
        ArrayList var7 = new ArrayList<>();

        for(int x = par1 >> 4; x <= par4 >> 4; ++x) {
            for(int z = par3 >> 4; z <= par6 >> 4; ++z) {
                Chunk chunk = this.getChunk(x, z);
                if (chunk != null) {
                    for (Object obj : chunk.blockEntities.values()) {
                        BlockEntity entity = (BlockEntity) obj;
                        if (!entity.isRemoved() && entity.x >= par1 && entity.y >= par2 && entity.z >= par3 && entity.x <= par4 && entity.y <= par5 && entity.z <= par6) {
                            var7.add(entity);
                        }
                    }
                }
            }
        }

        return var7;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3637(PlayerEntity par1EntityPlayer, int par2, int par3, int par4) {
        return super.method_3637(par1EntityPlayer, par2, par3, par4);
    }

    @Override
    public boolean canMineBlockBody(PlayerEntity par1EntityPlayer, int par2, int par3, int par4) {
        int var5 = MathHelper.abs(par2 - this.levelProperties.getSpawnX());
        int var6 = MathHelper.abs(par4 - this.levelProperties.getSpawnZ());
        if (var5 > var6) {
            var6 = var5;
        }

        return var6 > this.server.spawnProtectionSize || this.server.getPlayerManager().canCheat(par1EntityPlayer.username) || this.server.isSinglePlayer();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    protected void placeBonusChest() {
        BonusChestFeature var1 = new BonusChestFeature(ChestGenHooks.getItems("bonusChest"), ChestGenHooks.getCount("bonusChest", this.random));

        for(int var2 = 0; var2 < 10; ++var2) {
            int var3 = this.levelProperties.getSpawnX() + this.random.nextInt(6) - this.random.nextInt(6);
            int var4 = this.levelProperties.getSpawnZ() + this.random.nextInt(6) - this.random.nextInt(6);
            int var5 = this.method_3708(var3, var4) + 1;
            if (var1.method_4028(this, this.random, var3, var5, var4)) {
                break;
            }
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2138(boolean par1, ProgressListener par2IProgressUpdate) {
        if (this.chunkProvider.isSavingEnabled()) {
            if (par2IProgressUpdate != null) {
                par2IProgressUpdate.setTitle("Saving level");
            }

            this.method_2132();
            if (par2IProgressUpdate != null) {
                par2IProgressUpdate.setTask("Saving chunks");
            }

            this.chunkProvider.saveChunks(par1, par2IProgressUpdate);
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Save(this));
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_3654(int par1, int par2, int par3, int par4, int par5, int par6) {
        BlockAction var7 = new BlockAction(par1, par2, par3, par4, par5, par6);

        for (Object o : this.field_2815[this.field_2816]) {
            BlockAction var9 = (BlockAction) o;
            if (var9.equals(var7)) {
                return;
            }
        }

        this.field_2815[this.field_2816].add(var7);
    }

    @Override
    public File getChunkSaveLocation() {
        return ((ThreadedAnvilChunkStorage)this.chunkCache.chunkWriter).saveLocation;
    }
}
