package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common.WorldMixin;
import net.minecraft.src.MapStorage;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public abstract class WorldClientMixin extends WorldMixin {

    // Pattern A (two @Inject calls): inject #1 fires before the setSpawnLocation() INVOKE to set
    // mapStorage and isRemote before finishSetup() initialises the dimension — matching the order
    // the Forge patch requires. inject #2 posts WorldEvent.Load at constructor TAIL.
    // Logic delta: vanilla also writes mapStorage after setSpawnLocation (same value); the second
    // write is harmless and is NOT removed, so mapStorage ends up set correctly either way.
    @Inject(
        method = "<init>(Lnet/minecraft/src/NetClientHandler;Lnet/minecraft/src/WorldSettings;IILnet/minecraft/src/Profiler;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldClient;setSpawnLocation(III)V")
    )
    private void forge$clientWorldSetup(CallbackInfo ci,
            @Local(argsOnly = true, ordinal = 0) NetClientHandler par1NetClientHandler) {
        this.mapStorage = par1NetClientHandler.mapStorage;
        this.isRemote = true;
        this.finishSetup();
    }

    @Definition(id = "mapStorage", field = "Lnet/minecraft/src/WorldClient;mapStorage:Lnet/minecraft/src/MapStorage;")
    @Expression("this.mapStorage = ?")
    @WrapOperation(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void forge$disableOriginalCall(WorldClient instance, MapStorage value, Operation<Void> original) {
        // Do nothing
    }

    @Inject(
        method = "<init>(Lnet/minecraft/src/NetClientHandler;Lnet/minecraft/src/WorldSettings;IILnet/minecraft/src/Profiler;)V",
        at = @At("TAIL")
    )
    private void forge$postWorldLoadEvent(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load((World)(Object)this));
    }

    // Pattern D (@Overwrite): WorldClient.updateWeather() is a separate bytecode override that
    // bypassed WorldMixin's provider delegation on World; calling super routes it back through
    // World.updateWeather() → provider.updateWeather() → updateWeatherBody().
    /**
     * @author FabricCompatibilityLayers
     * @reason Routes WorldClient.updateWeather() through the provider delegation chain;
     * WorldMixin @Overwrites World.updateWeather() to call provider.updateWeather()
     * but WorldClient's own override bypassed it — super restores the chain.
     */
    @Overwrite
    protected void updateWeather() {
        super.updateWeather();
    }

    // Pattern J (extension interface override): provides the WorldClient-specific updateWeatherBody()
    // (lightning countdown + rain/thunder strength interpolation only, no time management),
    // overriding WorldMixin's server-side body for WorldClient instances.
    // Logic delta: 1:1 translation of the original WorldClient.updateWeather() body.
    @Override
    public void updateWeatherBody() {
        if (!this.provider.hasNoSky) {
            if (this.lastLightningBolt > 0) {
                --this.lastLightningBolt;
            }

            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
                this.rainingStrength = (float)((double)this.rainingStrength + 0.01);
            } else {
                this.rainingStrength = (float)((double)this.rainingStrength - 0.01);
            }

            if (this.rainingStrength < 0.0F) {
                this.rainingStrength = 0.0F;
            }

            if (this.rainingStrength > 1.0F) {
                this.rainingStrength = 1.0F;
            }

            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01);
            } else {
                this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01);
            }

            if (this.thunderingStrength < 0.0F) {
                this.thunderingStrength = 0.0F;
            }

            if (this.thunderingStrength > 1.0F) {
                this.thunderingStrength = 1.0F;
            }

        }
    }

}