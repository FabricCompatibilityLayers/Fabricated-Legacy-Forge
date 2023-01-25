package fr.catcore.fabricatedforge.mixin.forgefml.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.relauncher.ArgsWrapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import fr.catcore.fabricatedforge.mixininterface.IMinecraftServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.class_738;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.PacketListenerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerWorldManager;
import net.minecraft.stat.Stats;
import net.minecraft.util.Tickable;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.snooper.Snoopable;
import net.minecraft.util.snooper.Snooper;
import net.minecraft.world.*;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorageAccess;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.*;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements Runnable, Snoopable, CommandSource, IMinecraftServer {

    @Shadow protected abstract void upgradeWorld(String name);

    @Shadow protected abstract void setServerOperation(String operation);

    @Shadow @Final private LevelStorageAccess saveStorage;

    @Shadow public abstract GameMode method_3026();

    @Shadow public abstract boolean shouldGenerateStructures();

    @Shadow public abstract boolean isHardcore();

    @Shadow private boolean forceWorldUpgrade;

    @Shadow public abstract boolean isDemo();

    @Shadow @Final public Profiler profiler;

    @Shadow public abstract boolean isSinglePlayer();

    @Shadow public ServerWorld[] worlds;
    @Shadow private PlayerManager playerManager;

    @Shadow public abstract void method_3016(int i);

    @Shadow public abstract int method_3029();

    @Shadow public static Logger field_3848;

    @Shadow private boolean shouldResetWorld;

    @Shadow public abstract PacketListenerManager method_3005();

    @Shadow protected abstract void saveWorlds(boolean silent);

    @Shadow @Final private Snooper snooper;

    @Shadow protected abstract boolean setupServer();

    @Shadow private boolean running;
    @Shadow private boolean loading;
    @Shadow private long lastWarnTime;

    @Shadow protected abstract void setCrashReport(CrashReport report);

    @Shadow public abstract CrashReport populateCrashReport(CrashReport report);

    @Shadow protected abstract File getRunDirectory();

    @Shadow private boolean stopped;

    @Shadow protected abstract void exit();

    @Shadow private int ticks;
    @Shadow private boolean profiling;

    @Shadow @Final public long[] lastTickLengths;
    @Shadow @Final public long[] field_3853;
    @Shadow private long field_3832;
    @Shadow @Final public long[] field_3854;
    @Shadow private long field_3833;
    @Shadow @Final public long[] field_3855;
    @Shadow private long field_3834;
    @Shadow @Final public long[] field_3856;
    @Shadow private long field_3835;

    @Shadow public abstract boolean isNetherAllowed();

    @Shadow @Final private List<Tickable> tickables;

    @Shadow public abstract LevelStorageAccess getSaveStorage();

    @Shadow public abstract void stopRunning();

    @Shadow protected abstract void prepareWorlds();

    public Hashtable<Integer, long[]> worldTickTimes = new Hashtable<>();

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    protected void setupWorld(String par1Str, String par2Str, long par3, LevelGeneratorType par5WorldType, String par6Str) {
        this.upgradeWorld(par1Str);
        this.setServerOperation("menu.loadingLevel");
        SaveHandler var7 = this.saveStorage.createSaveHandler(par1Str, true);
        LevelProperties var9 = var7.getLevelProperties();
        LevelInfo var8;
        if (var9 == null) {
            var8 = new LevelInfo(par3, this.method_3026(), this.shouldGenerateStructures(), this.isHardcore(), par5WorldType);
            var8.setGeneratorOptions(par6Str);
        } else {
            var8 = new LevelInfo(var9);
        }

        if (this.forceWorldUpgrade) {
            var8.setBonusChest();
        }

        ServerWorld overWorld = (ServerWorld)(this.isDemo()
                ? new DemoServerWorld((MinecraftServer)(Object) this, var7, par2Str, 0, this.profiler)
                : new ServerWorld((MinecraftServer)(Object) this, var7, par2Str, 0, var8, this.profiler));
        Integer[] arr$ = DimensionManager.getStaticDimensionIDs();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int dim = arr$[i$];
            ServerWorld world = (ServerWorld)(dim == 0 ? overWorld : new MultiServerWorld((MinecraftServer)(Object) this, var7, par2Str, dim, var8, overWorld, this.profiler));
            world.addListener(new ServerWorldManager((MinecraftServer)(Object) this, world));
            if (!this.isSinglePlayer()) {
                world.getLevelProperties().getGameMode(this.method_3026());
            }

            this.playerManager.setMainWorld(this.worlds);
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }

        this.playerManager.setMainWorld(new ServerWorld[]{overWorld});
        this.method_3016(this.method_3029());
        this.prepareWorlds();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void stopServer() {
        if (!this.shouldResetWorld) {
            field_3848.info("Stopping server");
            if (this.method_3005() != null) {
                this.method_3005().stop();
            }

            if (this.playerManager != null) {
                field_3848.info("Saving players");
                this.playerManager.saveAllPlayerData();
                this.playerManager.disconnectAllPlayers();
            }

            field_3848.info("Saving worlds");
            this.saveWorlds(false);

            for(int var1 = 0; var1 < this.worlds.length; ++var1) {
                ServerWorld var2 = this.worlds[var1];
                MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(var2));
                var2.close();
                DimensionManager.setWorld(var2.dimension.dimensionType, null);
            }

            if (this.snooper != null && this.snooper.isActive()) {
                this.snooper.concel();
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void run() {
        try {
            if (this.setupServer()) {
                FMLCommonHandler.instance().handleServerStarted();
                long var1 = System.currentTimeMillis();
                FMLCommonHandler.instance().onWorldLoadTick(this.worlds);

                for(long var50 = 0L; this.running; this.loading = true) {
                    long var5 = System.currentTimeMillis();
                    long var7 = var5 - var1;
                    if (var7 > 2000L && var1 - this.lastWarnTime >= 15000L) {
                        field_3848.warning("Can't keep up! Did the system time change, or is the server overloaded?");
                        var7 = 2000L;
                        this.lastWarnTime = var1;
                    }

                    if (var7 < 0L) {
                        field_3848.warning("Time ran backwards! Did the system time change?");
                        var7 = 0L;
                    }

                    var50 += var7;
                    var1 = var5;
                    if (this.worlds[0].isReady()) {
                        this.setupWorld();
                        var50 = 0L;
                    } else {
                        while(var50 > 50L) {
                            var50 -= 50L;
                            this.setupWorld();
                        }
                    }

                    Thread.sleep(1L);
                }

                FMLCommonHandler.instance().handleServerStopping();
            } else {
                this.setCrashReport((CrashReport)null);
            }
        } catch (Throwable var48) {
            var48.printStackTrace();
            field_3848.log(Level.SEVERE, "Encountered an unexpected exception " + var48.getClass().getSimpleName(), var48);
            CrashReport var2 = null;
            if (var48 instanceof CrashException) {
                var2 = this.populateCrashReport(((CrashException)var48).getReport());
            } else {
                var2 = this.populateCrashReport(new CrashReport("Exception in server tick loop", var48));
            }

            File var3 = new File(
                    new File(this.getRunDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt"
            );
            if (var2.saveTo(var3)) {
                field_3848.severe("This crash report has been saved to: " + var3.getAbsolutePath());
            } else {
                field_3848.severe("We were unable to save this crash report to disk.");
            }

            this.setCrashReport(var2);
        } finally {
            try {
                this.stopServer();
                this.stopped = true;
            } catch (Throwable var461) {
                var461.printStackTrace();
            } finally {
                this.exit();
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void setupWorld() {
        FMLCommonHandler.instance().rescheduleTicks(Side.SERVER);
        long var1 = System.nanoTime();
        Box.getLocalPool().tick();
        FMLCommonHandler.instance().onPreServerTick();
        ++this.ticks;
        if (this.profiling) {
            this.profiling = false;
            this.profiler.enabled = true;
            this.profiler.reset();
        }

        this.profiler.push("root");
        this.tick();
        if (this.ticks % 900 == 0) {
            this.profiler.push("save");
            this.playerManager.saveAllPlayerData();
            this.saveWorlds(true);
            this.profiler.pop();
        }

        this.profiler.push("tallying");
        this.lastTickLengths[this.ticks % 100] = System.nanoTime() - var1;
        this.field_3853[this.ticks % 100] = Packet.totalPacketsSent - this.field_3832;
        this.field_3832 = Packet.totalPacketsSent;
        this.field_3854[this.ticks % 100] = Packet.totalBytesSent - this.field_3833;
        this.field_3833 = Packet.totalBytesSent;
        this.field_3855[this.ticks % 100] = Packet.totalPacketsReceived - this.field_3834;
        this.field_3834 = Packet.totalPacketsReceived;
        this.field_3856[this.ticks % 100] = Packet.totalBytesReceived - this.field_3835;
        this.field_3835 = Packet.totalBytesReceived;
        this.profiler.pop();
        this.profiler.push("snooper");
        if (!this.snooper.isActive() && this.ticks > 100) {
            this.snooper.setActive();
        }

        if (this.ticks % 6000 == 0) {
            this.snooper.addCpuInfo();
        }

        this.profiler.pop();
        this.profiler.pop();
        FMLCommonHandler.instance().onPostServerTick();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void tick() {
        this.profiler.push("levels");
        Integer[] ids = DimensionManager.getIDs();

        for(int x = 0; x < ids.length; ++x) {
            int id = ids[x];
            long var2 = System.nanoTime();
            if (id == 0 || this.isNetherAllowed()) {
                ServerWorld var4 = DimensionManager.getWorld(id);
                this.profiler.push(var4.getLevelProperties().getLevelName());
                this.profiler.push("pools");
                var4.getVectorPool().tick();
                this.profiler.pop();
                if (this.ticks % 20 == 0) {
                    this.profiler.push("timeSync");
                    this.playerManager
                            .sendToDimension(new WorldTimeUpdateS2CPacket(var4.getLastUpdateTime(), var4.getTimeOfDay()), var4.dimension.dimensionType);
                    this.profiler.pop();
                }

                this.profiler.push("tick");
                FMLCommonHandler.instance().onPreWorldTick(var4);

                try {
                    var4.tick();
                } catch (Throwable var11) {
                    CrashReport var6 = CrashReport.create(var11, "Exception ticking world");
                    var4.addToCrashReport(var6);
                    throw new CrashException(var6);
                }

                try {
                    var4.tickEntities();
                } catch (Throwable var10) {
                    CrashReport var6 = CrashReport.create(var10, "Exception ticking world entities");
                    var4.addToCrashReport(var6);
                    throw new CrashException(var6);
                }

                FMLCommonHandler.instance().onPostWorldTick(var4);
                this.profiler.pop();
                this.profiler.push("tracker");
                var4.getEntityTracker().method_2095();
                this.profiler.pop();
                this.profiler.pop();
            }

            ((long[])this.worldTickTimes.get(id))[this.ticks % 100] = System.nanoTime() - var2;
        }

        this.profiler.swap("dim_unloading");
        DimensionManager.unloadWorlds(this.worldTickTimes);
        this.profiler.swap("connection");
        this.method_3005().handlePackets();
        this.profiler.swap("players");
        this.playerManager.updatePlayerLatency();
        this.profiler.swap("tickables");

        for(int var1 = 0; var1 < this.tickables.size(); ++var1) {
            ((Tickable)this.tickables.get(var1)).tick();
        }

        this.profiler.pop();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ServerWorld getWorld(int par1) {
        ServerWorld ret = DimensionManager.getWorld(par1);
        if (ret == null) {
            DimensionManager.initDimension(par1);
            ret = DimensionManager.getWorld(par1);
        }

        return ret;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public String getServerModName() {
        return "forge,fml on fabric";
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2980() {
        this.shouldResetWorld = true;
        this.getSaveStorage().clearAll();

        for(int var1 = 0; var1 < this.worlds.length; ++var1) {
            ServerWorld var2 = this.worlds[var1];
            if (var2 != null) {
                MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(var2));
                var2.close();
            }
        }

        this.getSaveStorage().deleteLevel(this.worlds[0].getSaveHandler().getWorldName());
        this.stopRunning();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.SERVER)
    @Overwrite
    public static void main(String[] par0ArrayOfStr) {
        FMLRelauncher.handleServerRelaunch(new ArgsWrapper(par0ArrayOfStr));
    }

    @Environment(EnvType.SERVER)
    protected static void fmlReentry(ArgsWrapper wrap) {
        String[] par0ArrayOfStr = wrap.args;
        Stats.method_2269();

        try {
            boolean var1 = !GraphicsEnvironment.isHeadless();
            String var2 = null;
            String var3 = ".";
            String var4 = null;
            boolean var5 = false;
            boolean var6 = false;
            int var7 = -1;

            for(int var8 = 0; var8 < par0ArrayOfStr.length; ++var8) {
                String var9 = par0ArrayOfStr[var8];
                String var10 = var8 == par0ArrayOfStr.length - 1 ? null : par0ArrayOfStr[var8 + 1];
                boolean var11 = false;
                if (var9.equals("nogui") || var9.equals("--nogui")) {
                    var1 = false;
                } else if (var9.equals("--port") && var10 != null) {
                    var11 = true;

                    try {
                        var7 = Integer.parseInt(var10);
                    } catch (NumberFormatException var141) {
                    }
                } else if (var9.equals("--singleplayer") && var10 != null) {
                    var11 = true;
                    var2 = var10;
                } else if (var9.equals("--universe") && var10 != null) {
                    var11 = true;
                    var3 = var10;
                } else if (var9.equals("--world") && var10 != null) {
                    var11 = true;
                    var4 = var10;
                } else if (var9.equals("--demo")) {
                    var5 = true;
                } else if (var9.equals("--bonusChest")) {
                    var6 = true;
                }

                if (var11) {
                    ++var8;
                }
            }

            MinecraftDedicatedServer var15 = new MinecraftDedicatedServer(new File(var3));
            if (var2 != null) {
                var15.setUserName(var2);
            }

            if (var4 != null) {
                var15.setLevelName(var4);
            }

            if (var7 >= 0) {
                var15.setServerPort(var7);
            }

            if (var5) {
                var15.setDemo(true);
            }

            if (var6) {
                var15.setForceWorldUpgrade(true);
            }

            if (var1) {
                var15.createGui();
            }

            var15.startServerThread();
            Runtime.getRuntime().addShutdownHook(new class_738(var15));
        } catch (Exception var151) {
            field_3848.log(Level.SEVERE, "Failed to start the minecraft server", var151);
        }
    }

    @Override
    public Hashtable<Integer, long[]> getWorldTickTimes() {
        return this.worldTickTimes;
    }
}
