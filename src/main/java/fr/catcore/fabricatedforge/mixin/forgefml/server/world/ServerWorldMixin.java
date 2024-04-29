package fr.catcore.fabricatedforge.mixin.forgefml.server.world;

import fr.catcore.fabricatedforge.mixininterface.IServerChunkProvider;
import fr.catcore.fabricatedforge.mixininterface.IServerWorld;
import fr.catcore.fabricatedforge.mixininterface.IThreadedAnvilChunkStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.MobSpawnerHelper;
import net.minecraft.entity.PortalTeleporter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerWorldManager;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.ScheduledTick;
import net.minecraft.util.class_797;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
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
    @Shadow private Set<ScheduledTick> field_2811;

    @Shadow private TreeSet<ScheduledTick> scheduledTicks;

    @Shadow private int idleTimeout;

    @Shadow @Final private MinecraftServer server;

    @Shadow public abstract void resetIdleTimeout();

    @Shadow protected abstract void method_2131();

    @Shadow @Final private PortalTeleporter portalTeleporter;

    @Shadow @Final private PlayerWorldManager playerWorldManager;

    @Shadow protected abstract void awakenPlayers();

    @Shadow public abstract boolean isReady();

    public ServerWorldMixin(SaveHandler saveHandler, String string, Dimension dimension, LevelInfo levelInfo, Profiler profiler) {
        super(saveHandler, string, dimension, levelInfo, profiler);
    }

    protected Set<ChunkPos> doneChunks = new HashSet<>();
    public List<PortalTeleporter> customTeleporters = new ArrayList<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(MinecraftServer par1MinecraftServer, SaveHandler par2ISaveHandler, String par3Str, int par4, LevelInfo par5WorldSettings, Profiler par6Profiler, CallbackInfo ci) {
        DimensionManager.setWorld(par4, (ServerWorld)(Object) this);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void tick() {
        super.tick();
        if (this.getLevelProperties().isHardcore() && this.difficulty < 3) {
            this.difficulty = 3;
        }

        this.dimension.biomeSource.method_3859();
        if (this.isReady()) {
            boolean var1 = false;
            if (this.spawnAnimals && this.difficulty >= 1) {
            }

            if (!var1) {
                long var2 = this.levelProperties.getTimeOfDay() + 24000L;
                this.levelProperties.setDayTime(var2 - var2 % 24000L);
                this.awakenPlayers();
            }
        }

        this.profiler.push("mobSpawner");
        if (this.getGameRules().getBoolean("doMobSpawning")) {
            MobSpawnerHelper.tickSpawners((ServerWorld)(Object) this, this.spawnAnimals, this.spawnMonsters, this.levelProperties.getTime() % 400L == 0L);
        }

        this.profiler.swap("chunkSource");
        this.chunkProvider.tickChunks();
        int var4 = this.method_3597(1.0F);
        if (var4 != this.ambientDarkness) {
            this.ambientDarkness = var4;
        }

        this.method_2131();
        this.levelProperties.setTime(this.levelProperties.getTime() + 1L);
        this.levelProperties.setDayTime(this.levelProperties.getTimeOfDay() + 1L);
        this.profiler.swap("tickPending");
        this.method_3644(false);
        this.profiler.swap("tickTiles");
        this.tickBlocks();
        this.profiler.swap("chunkMap");
        this.playerWorldManager.method_2111();
        this.profiler.swap("village");
        this.villageState.method_2839();
        this.zombieSiegeManager.method_2835();
        this.profiler.swap("portalForcer");
        this.portalTeleporter.method_4698(this.getLastUpdateTime());

        for(PortalTeleporter tele : this.customTeleporters) {
            tele.method_4698(this.getLastUpdateTime());
        }

        this.profiler.pop();
        this.method_2131();
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
    public void tickBlocks() {
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
                var7.tick();
            }

            this.profiler.swap("thunder");
            if (this.dimension.canDoLightning(var7) && this.random.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
                this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
                int var8 = this.lcgBlockSeed >> 2;
                int var9 = var5 + (var8 & 15);
                int var10 = var6 + (var8 >> 8 & 15);
                int var11 = this.getSurfaceY(var9, var10);
                if (this.isBeingRainedOn(var9, var11, var10)) {
                    this.addEntity(new LightningBoltEntity(this, (double)var9, (double)var11, (double)var10));
                    this.field_23088 = 2;
                }
            }

            this.profiler.swap("iceandsnow");
            if (this.dimension.canDoRainSnowIce(var7) && this.random.nextInt(16) == 0) {
                this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
                int var8 = this.lcgBlockSeed >> 2;
                int var9 = var8 & 15;
                int var10 = var8 >> 8 & 15;
                int var11 = this.getSurfaceY(var9 + var5, var10 + var6);
                if (this.method_3732(var9 + var5, var11 - 1, var10 + var6)) {
                    this.method_3690(var9 + var5, var11 - 1, var10 + var6, Block.ICE.id);
                }

                if (this.isRaining() && this.canSnowLand(var9 + var5, var11, var10 + var6)) {
                    this.method_3690(var9 + var5, var11, var10 + var6, Block.SNOW_LAYER.id);
                }

                if (this.isRaining()) {
                    Biome var12 = this.getBiome(var9 + var5, var10 + var6);
                    if (var12.method_3830()) {
                        int var13 = this.getBlock(var9 + var5, var11 - 1, var10 + var6);
                        if (var13 != 0) {
                            Block.BLOCKS[var13].method_457(this, var9 + var5, var11 - 1, var10 + var6);
                        }
                    }
                }
            }

            this.profiler.swap("tickTiles");

            for(ChunkSection var21 : var7.getBlockStorage()) {
                if (var21 != null && var21.hasTickableBlocks()) {
                    for(int var20 = 0; var20 < 3; ++var20) {
                        this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
                        int var13 = this.lcgBlockSeed >> 2;
                        int var14 = var13 & 15;
                        int var15 = var13 >> 8 & 15;
                        int var16 = var13 >> 16 & 15;
                        int var17 = var21.getBlock(var14, var16, var15);
                        ++var2;
                        Block var18 = Block.BLOCKS[var17];
                        if (var18 != null && var18.ticksRandomly()) {
                            ++var1;
                            var18.onTick(this, var14 + var5, var16 + var21.getYOffset(), var15 + var6, this.random);
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
    public void method_4682(int par1, int par2, int par3, int par4, int par5, int par6) {
        ScheduledTick var7 = new ScheduledTick(par1, par2, par3, par4);
        boolean isForced = this.getPersistentChunks().containsKey(new ChunkPos(var7.x >> 4, var7.z >> 4));
        byte var8 = (byte)(isForced ? 0 : 8);
        if (this.immediateUpdates && par4 > 0) {
            if (Block.BLOCKS[par4].doImmediateUpdates()) {
                if (this.isRegionLoaded(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8)) {
                    int var9 = this.getBlock(var7.x, var7.y, var7.z);
                    if (var9 == var7.blockId && var9 > 0) {
                        Block.BLOCKS[var9].onTick(this, var7.x, var7.y, var7.z, this.random);
                    }
                }

                return;
            }

            par5 = 1;
        }

        if (this.isRegionLoaded(par1 - var8, par2 - var8, par3 - var8, par1 + var8, par2 + var8, par3 + var8)) {
            if (par4 > 0) {
                var7.setTime((long)par5 + this.levelProperties.getTime());
                var7.setPriority(par6);
            }

            if (!this.field_2811.contains(var7)) {
                this.field_2811.add(var7);
                this.scheduledTicks.add(var7);
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
            if (this.idleTimeout++ >= 1200) {
                return;
            }
        } else {
            this.resetIdleTimeout();
        }

        super.tickEntities();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3644(boolean par1) {
        int var2 = this.scheduledTicks.size();
        if (var2 != this.field_2811.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (var2 > 1000) {
                var2 = 1000;
            }

            for(int var3 = 0; var3 < var2; ++var3) {
                ScheduledTick var4 = (ScheduledTick)this.scheduledTicks.first();
                if (!par1 && var4.time > this.levelProperties.getTime()) {
                    break;
                }

                this.scheduledTicks.remove(var4);
                this.field_2811.remove(var4);
                boolean isForced = this.getPersistentChunks().containsKey(new ChunkPos(var4.x >> 4, var4.z >> 4));
                byte var5 = (byte)(isForced ? 0 : 8);
                if (this.isRegionLoaded(var4.x - var5, var4.y - var5, var4.z - var5, var4.x + var5, var4.y + var5, var4.z + var5)) {
                    int var6 = this.getBlock(var4.x, var4.y, var4.z);
                    if (var6 == var4.blockId && var6 > 0) {
                        try {
                            Block.BLOCKS[var6].onTick(this, var4.x, var4.y, var4.z, this.random);
                        } catch (Throwable var14) {
                            CrashReport var8 = CrashReport.create(var14, "Exception while ticking a block");
                            CrashReportSection var9 = var8.addElement("Block being ticked");

                            int var10;
                            try {
                                var10 = this.getBlockData(var4.x, var4.y, var4.z);
                            } catch (Throwable var13) {
                                var10 = -1;
                            }

                            CrashReportSection.addBlock(var9, var4.x, var4.y, var4.z, var6, var10);
                            throw new CrashException(var8);
                        }
                    }
                }
            }

            return !this.scheduledTicks.isEmpty();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public List method_2134(int par1, int par2, int par3, int par4, int par5, int par6) {
        ArrayList var7 = new ArrayList();

        for(int x = par1 >> 4; x <= par4 >> 4; ++x) {
            for(int z = par3 >> 4; z <= par6 >> 4; ++z) {
                Chunk chunk = this.getChunk(x, z);
                if (chunk != null) {
                    for(Object obj : chunk.blockEntities.values()) {
                        BlockEntity entity = (BlockEntity)obj;
                        if (!entity.isRemoved()
                                && entity.x >= par1
                                && entity.y >= par2
                                && entity.z >= par3
                                && entity.x <= par4
                                && entity.y <= par5
                                && entity.z <= par6) {
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

        return var6 > this.server.getSpawnProtectionRadius()
                || this.server.getPlayerManager().canCheat(par1EntityPlayer.username)
                || this.server.isSinglePlayer();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void placeBonusChest() {
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
    public void method_2138(boolean par1, ProgressListener par2IProgressUpdate) throws WorldSaveException {
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
    protected void method_2132() throws WorldSaveException {
        this.readSaveLock();
        this.saveHandler.saveWorld(this.levelProperties, this.server.getPlayerManager().getUserData());
        this.persistentStateManager.save();
        this.getPerWorldStorage().save();
    }

    @Override
    public File getChunkSaveLocation() {
        return ((ThreadedAnvilChunkStorage)((IServerChunkProvider)this.chunkCache).getChunkWriter()).saveLocation;
    }
}
