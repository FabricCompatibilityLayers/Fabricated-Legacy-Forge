package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.FMLWorldTypeExtension;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.GenLayer;
import net.minecraft.src.GenLayerBiome;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GenLayerBiome.class)
public abstract class GenLayerBiomeMixin extends GenLayer {
    @Shadow private BiomeGenBase[] field_75914_b;

    public GenLayerBiomeMixin(long p_i3891_1_) {
        super(p_i3891_1_);
    }

    @ShadowSuperConstructor
    public abstract void superConstructor(long p_i3891_1_);

    @ReplaceConstructor
    public void constructor(long p_i3888_1_, GenLayer p_i3888_3_, WorldType p_i3888_4_) {
        superConstructor(p_i3888_1_);
        this.field_75914_b = ((FMLWorldTypeExtension) p_i3888_4_).getBiomesForWorldType();
        this.field_75909_a = p_i3888_3_;
    }
}
