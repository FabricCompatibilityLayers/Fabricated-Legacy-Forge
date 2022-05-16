package fr.catcore.fabricatedforge.mixin.forgefml.client.world;

import net.minecraft.client.class_469;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ClientChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {

    @Shadow private ClientChunkProvider clientChunkCache;

    @Shadow private Set world;

    @Shadow private Set field_1658;

    public ClientWorldMixin(SaveHandler saveHandler, String string, Dimension dimension, LevelInfo levelInfo, Profiler profiler) {
        super(saveHandler, string, dimension, levelInfo, profiler);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;method_3578(III)V"))
    private void ctrFix1(class_469 par1NetClientHandler, LevelInfo par2WorldSettings, int par3, int par4, Profiler par5Profiler, CallbackInfo ci) {
        this.persistentStateManager = par1NetClientHandler.stateManager;
        this.isClient = true;
        this.finishSetup();
    }

    @Inject(method = "<init>", cancellable = true, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/world/ClientWorld;method_3578(III)V"))
    private void ctrFix2(class_469 par1NetClientHandler, LevelInfo par2WorldSettings, int par3, int par4, Profiler par5Profiler, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(this));
        ci.cancel();
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_1252(int par1, int par2, boolean par3) {
        if (par3) {
            this.clientChunkCache.getOrGenerateChunk(par1, par2);
        } else {
            this.clientChunkCache.unloadChunk(par1, par2);
        }

        if (!par3) {
            this.onRenderRegionUpdate(par1 * 16, 0, par2 * 16, par1 * 16 + 15, 256, par2 * 16 + 15);
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    protected void tickWeather() {
        super.tickWeather();
    }

    @Override
    public void updateWeatherBody() {
        if (!this.dimension.isNether) {
            if (this.field_4553 > 0) {
                --this.field_4553;
            }

            this.rainGradientPrev = this.rainGradient;
            if (this.levelProperties.isRaining()) {
                this.rainGradient = (float)((double)this.rainGradient + 0.01);
            } else {
                this.rainGradient = (float)((double)this.rainGradient - 0.01);
            }

            if (this.rainGradient < 0.0F) {
                this.rainGradient = 0.0F;
            }

            if (this.rainGradient > 1.0F) {
                this.rainGradient = 1.0F;
            }

            this.thunderGradientPrev = this.thunderGradient;
            if (this.levelProperties.isThundering()) {
                this.thunderGradient = (float)((double)this.thunderGradient + 0.01);
            } else {
                this.thunderGradient = (float)((double)this.thunderGradient - 0.01);
            }

            if (this.thunderGradient < 0.0F) {
                this.thunderGradient = 0.0F;
            }

            if (this.thunderGradient > 1.0F) {
                this.thunderGradient = 1.0F;
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    static Set method_1254(ClientWorld par0WorldClient) {
        return ((ClientWorldMixin)(Object)par0WorldClient).world;
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    static Set method_1256(ClientWorld par0WorldClient) {
        return ((ClientWorldMixin)(Object)par0WorldClient).field_1658;
    }
}
