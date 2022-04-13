package fr.catcore.fabricatedforge.mixin.modloader.common;

import fr.catcore.fabricatedforge.utils.SetBaseBiomesLayerData;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.Layer;
import net.minecraft.world.biome.layer.SetBaseBiomesLayer;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SetBaseBiomesLayer.class)
public class SetBaseBiomesLayerMixin {

    @Shadow private Biome[] field_166;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void useModLoaderBiomePool(long l, Layer layer, LevelGeneratorType levelGeneratorType, CallbackInfo ci) {
        if (levelGeneratorType != LevelGeneratorType.DEFAULT_1_1) this.field_166 = SetBaseBiomesLayerData.biomeArray;
    }
}
