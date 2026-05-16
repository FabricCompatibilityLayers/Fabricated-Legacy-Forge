package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common.MinecraftServerMixin;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType) {
        this.convertMapIfNeeded(par1Str);
        ISaveHandler var6 = this.getActiveAnvilConverter().getSaveLoader(par1Str, true);

        WorldServer overWorld = this.isDemo()
            ? new DemoWorldServer((MinecraftServer) (Object) this, var6, par2Str, 0, this.theProfiler)
            : new WorldServer((MinecraftServer) (Object) this, var6, par2Str, 0, this.theWorldSettings, this.theProfiler);

        for (int dim : DimensionManager.getStaticDimensionIDs()) {
            WorldServer world = dim == 0 ? overWorld : new WorldServerMulti((MinecraftServer) (Object) this, var6, par2Str, dim, this.theWorldSettings, overWorld, this.theProfiler);
            world.addWorldAccess(new WorldManager((MinecraftServer) (Object) this, world));
            if (!this.isSinglePlayer()) {
                world.getWorldInfo().setGameType(this.getGameType());
            }
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }

        this.getConfigurationManager().setPlayerManager(new WorldServer[]{ overWorld });
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    // Pattern A (@Inject at RETURN): sets spawnProtectionSize = 0 just before returning from
    // startServer, disabling spawn protection for singleplayer. Field accessed directly because
    // we extend MinecraftServerMixin which declares it as public int spawnProtectionSize.
    @Inject(method = "startServer", at = @At("RETURN"))
    private void forge$disableSpawnProtection(CallbackInfoReturnable<Boolean> cir) {
        this.spawnProtectionSize = 0;
    }
}