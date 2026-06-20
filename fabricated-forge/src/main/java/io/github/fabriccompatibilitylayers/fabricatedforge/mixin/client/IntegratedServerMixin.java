/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common.MinecraftServerMixin;
import net.minecraft.src.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServerMixin {

    @Shadow @Final private WorldSettings theWorldSettings;

    // Pattern D (@Overwrite): complete rewrite of loadAllWorlds to use DimensionManager-based
    // dimension loading with WorldEvent.Load — mirrors MinecraftServerMixin but retains
    // IntegratedServer-specific demo-mode handling and theWorldSettings field.
    /**
     * @author FabricCompatibilityLayers
     * @reason Replace fixed 3-world loop with DimensionManager-based loading and WorldEvent.Load
     */
    @Overwrite
    public void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str) {
        this.convertMapIfNeeded(par1Str);
        ISaveHandler var7 = this.getActiveAnvilConverter().getSaveLoader(par1Str, true);

        WorldServer overWorld = (isDemo() ? new DemoWorldServer((IntegratedServer) (Object) this, var7, par2Str, 0, theProfiler) : new WorldServer((IntegratedServer) (Object) this, var7, par2Str, 0, theWorldSettings, theProfiler));
        for(int dim : DimensionManager.getStaticDimensionIDs()) {
            WorldServer world = (dim == 0 ? overWorld : new WorldServerMulti((IntegratedServer) (Object) this, var7, par2Str, dim, theWorldSettings, overWorld, theProfiler));
            world.addWorldAccess(new WorldManager((IntegratedServer) (Object) this, world));
            if (!this.isSinglePlayer()) {
                world.getWorldInfo().setGameType(this.getGameType());
            }

            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }

        this.getConfigurationManager().setPlayerManager(new WorldServer[]{ overWorld });
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }
}