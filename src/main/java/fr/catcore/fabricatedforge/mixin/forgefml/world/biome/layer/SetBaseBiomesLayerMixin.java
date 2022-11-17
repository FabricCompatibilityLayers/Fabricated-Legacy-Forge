package fr.catcore.fabricatedforge.mixin.forgefml.world.biome.layer;

import fr.catcore.fabricatedforge.mixininterface.ILevelGeneratorType;
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
public abstract class SetBaseBiomesLayerMixin extends Layer {
    @Shadow private Biome[] field_166;

    public SetBaseBiomesLayerMixin(long l) {
        super(l);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(long par1, Layer par3GenLayer, LevelGeneratorType par4WorldType, CallbackInfo ci) {
        this.field_166 = ((ILevelGeneratorType)par4WorldType).getBiomesForWorldType();
        this.field_172 = par3GenLayer;
    }
}
