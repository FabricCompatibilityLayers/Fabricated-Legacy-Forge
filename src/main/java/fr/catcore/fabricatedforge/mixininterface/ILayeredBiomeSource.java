package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.world.biome.layer.Layer;
import net.minecraft.world.level.LevelGeneratorType;

public interface ILayeredBiomeSource {
    Layer[] getModdedBiomeGenerators(LevelGeneratorType worldType, long seed, Layer[] original);
}
