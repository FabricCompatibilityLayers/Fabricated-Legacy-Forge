package fr.catcore.fabricatedforge.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.biome.BiomeDecorator;

public interface IBiome {
    BiomeDecorator getModdedBiomeDecorator(BiomeDecorator original);

    @Environment(EnvType.CLIENT)
    int getWaterColorMultiplier();

    @Environment(EnvType.CLIENT)
    int getModdedBiomeGrassColor(int original);

    @Environment(EnvType.CLIENT)
    int getModdedBiomeFoliageColor(int original);
}
