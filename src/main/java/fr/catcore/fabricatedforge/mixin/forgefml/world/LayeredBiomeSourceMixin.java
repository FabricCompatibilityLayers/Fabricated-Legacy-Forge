package fr.catcore.fabricatedforge.mixin.forgefml.world;

import fr.catcore.fabricatedforge.forged.ReflectionUtils;
import fr.catcore.fabricatedforge.mixininterface.ILayeredBiomeSource;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.Layer;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LayeredBiomeSource.class)
public class LayeredBiomeSourceMixin implements ILayeredBiomeSource {
    @Shadow private List<Biome> biomes;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void replaceDefaultBiomes(CallbackInfo ci) {
        this.biomes.clear();
        this.biomes.addAll(ReflectionUtils.LayeredBiomeSource_allowedBiomes);
    }

    @Redirect(method = "<init>(JLnet/minecraft/world/level/LevelGeneratorType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/layer/Layer;method_146(JLnet/minecraft/world/level/LevelGeneratorType;)[Lnet/minecraft/world/biome/layer/Layer;"))
    private Layer[] addModded(long l, LevelGeneratorType levelGeneratorType) {
        Layer[] layers = Layer.method_146(l, levelGeneratorType);
        return this.getModdedBiomeGenerators(levelGeneratorType, l, layers);
    }

    @Override
    public Layer[] getModdedBiomeGenerators(LevelGeneratorType worldType, long seed, Layer[] original) {
        WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeGens;
    }
}
