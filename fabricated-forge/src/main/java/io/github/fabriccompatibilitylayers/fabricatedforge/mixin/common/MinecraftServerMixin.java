/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.MinecraftServerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Hashtable;
import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerExtension {

    @Shadow @Final public Profiler theProfiler;
    @Shadow @Final private List<IUpdatePlayerListBox> playersOnline;
    @Shadow private ServerConfigurationManager serverConfigManager;

    @Shadow public abstract NetworkListenThread getNetworkThread();

    @Shadow private int tickCounter;

    @Shadow public abstract boolean getAllowNether();

    @Shadow protected abstract void initialWorldChunkLoad();

    @Shadow public abstract void setDifficultyForAllWorlds(int par1);

    @Shadow public abstract int getDifficulty();

    @Shadow public WorldServer[] worldServers;

    @Shadow public abstract EnumGameType getGameType();

    @Shadow public abstract boolean isSinglePlayer();

    @Shadow public abstract boolean isDemo();

    @Shadow private boolean enableBonusChest;

    @Shadow public abstract boolean canStructuresSpawn();

    @Shadow @Final private ISaveFormat anvilConverterForAnvilFile;

    @Shadow protected abstract void setUserMessage(String par1Str);

    @Shadow protected abstract void convertMapIfNeeded(String par1Str);

    @Shadow public abstract boolean isHardcore();

    @Shadow public abstract ISaveFormat getActiveAnvilConverter();

    @Shadow public abstract ServerConfigurationManager getConfigurationManager();

    // Forge Fields
    public Hashtable<Integer, long[]> worldTickTimes = new Hashtable<>();

    /**
     * @author
     * @reason full rewrite of loop
     */
    @Overwrite
    public void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str) {
        this.convertMapIfNeeded(par1Str);
        this.setUserMessage("menu.loadingLevel");
        ISaveHandler var7 = this.anvilConverterForAnvilFile.getSaveLoader(par1Str, true);
        WorldInfo var9 = var7.loadWorldInfo();
        WorldSettings var8;
        if (var9 == null) {
            var8 = new WorldSettings(par3, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), par5WorldType);
            var8.func_82750_a(par6Str);
        } else {
            var8 = new WorldSettings(var9);
        }

        if (this.enableBonusChest) {
            var8.enableBonusChest();
        }

        WorldServer overWorld = (isDemo() ? new DemoWorldServer((MinecraftServer) (Object) this, var7, par2Str, 0, theProfiler) : new WorldServer((MinecraftServer) (Object) this, var7, par2Str, 0, var8, theProfiler));
        for(int dim : DimensionManager.getStaticDimensionIDs()) {
            WorldServer world = (dim == 0 ? overWorld : new WorldServerMulti((MinecraftServer) (Object) this, var7, par2Str, dim, var8, overWorld, theProfiler));
            world.addWorldAccess(new WorldManager((MinecraftServer) (Object) this, world));

            if (!this.isSinglePlayer()) {
                world.getWorldInfo().setGameType(this.getGameType());
            }

            this.serverConfigManager.setPlayerManager(this.worldServers);

            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }

        this.serverConfigManager.setPlayerManager(new WorldServer[]{ overWorld });
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    @WrapOperation(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;flush()V"))
    private void forge$unloadDimension(WorldServer var4, Operation<Void> original) {
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(var4));
        original.call(var4);
        DimensionManager.setWorld(var4.provider.dimensionId, null);
    }

    /**
     * @author
     * @reason full rewrite of loop
     */
    @Overwrite
    public void updateTimeLightAndEntities() {
        this.theProfiler.startSection("levels");

        for(Integer id : DimensionManager.getIDs()) {
            long var2 = System.nanoTime();
            if (id == 0 || this.getAllowNether()) {
                WorldServer var4 = DimensionManager.getWorld(id);
                this.theProfiler.startSection(var4.getWorldInfo().getWorldName());
                this.theProfiler.startSection("pools");
                var4.func_82732_R().clear();
                this.theProfiler.endSection();
                if (this.tickCounter % 20 == 0) {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(var4.func_82737_E(), var4.getWorldTime()), var4.provider.dimensionId);
                    this.theProfiler.endSection();
                }

                this.theProfiler.startSection("tick");
                var4.tick();
                var4.updateEntities();
                this.theProfiler.endSection();
                this.theProfiler.startSection("tracker");
                var4.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }

            worldTickTimes.get(id)[this.tickCounter % 100] = System.nanoTime() - var2;
        }

        this.theProfiler.endStartSection("dim_unloading");
        DimensionManager.unloadWorlds(worldTickTimes);
        this.theProfiler.endStartSection("connection");
        this.getNetworkThread().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.sendPlayerInfoToAllPlayers();
        this.theProfiler.endStartSection("tickables");

        for(IUpdatePlayerListBox var6 : this.playersOnline) {
            var6.update();
        }

        this.theProfiler.endSection();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public WorldServer worldServerForDimension(int par1)
    {
        WorldServer ret = DimensionManager.getWorld(par1);
        if (ret == null) {
            DimensionManager.initDimension(par1);
            ret = DimensionManager.getWorld(par1);
        }
        return ret;
    }

    @WrapOperation(method = "deleteWorldAndStopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;flush()V"))
    private void forge$deleteWorld(WorldServer var2, Operation<Void> original) {
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(var2));
        var2.flush();
    }
}
