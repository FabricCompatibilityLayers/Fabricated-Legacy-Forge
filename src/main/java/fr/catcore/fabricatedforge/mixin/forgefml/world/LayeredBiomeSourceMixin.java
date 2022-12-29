package fr.catcore.fabricatedforge.mixin.forgefml.world;

import fr.catcore.fabricatedforge.forged.ReflectionUtils;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LayeredBiomeSource.class)
public class LayeredBiomeSourceMixin {
    @Shadow private List<Biome> biomes;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void replaceDefaultBiomes(CallbackInfo ci) {
        this.biomes.clear();
        this.biomes.addAll(ReflectionUtils.LayeredBiomeSource_allowedBiomes);
    }
}
