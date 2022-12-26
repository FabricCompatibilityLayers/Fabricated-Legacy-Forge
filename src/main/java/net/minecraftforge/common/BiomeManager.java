package net.minecraftforge.common;

import net.minecraft.structure.StrongholdStructure;
import net.minecraft.structure.VillageStructure;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;

public class BiomeManager {
    public BiomeManager() {
    }

    public static void addVillageBiome(Biome biome, boolean canSpawn) {
        if (!VillageStructure.BIOMES.contains(biome)) {
            ArrayList<Biome> biomes = new ArrayList(VillageStructure.BIOMES);
            biomes.add(biome);
            VillageStructure.BIOMES = biomes;
        }
    }

    public static void removeVillageBiome(Biome biome) {
        if (VillageStructure.BIOMES.contains(biome)) {
            ArrayList<Biome> biomes = new ArrayList(VillageStructure.BIOMES);
            biomes.remove(biome);
            VillageStructure.BIOMES = biomes;
        }
    }

    public static void addStrongholdBiome(Biome biome) {
        if (!StrongholdStructure.allowedBiomes.contains(biome)) {
            StrongholdStructure.allowedBiomes.add(biome);
        }
    }

    public static void removeStrongholdBiome(Biome biome) {
        if (StrongholdStructure.allowedBiomes.contains(biome)) {
            StrongholdStructure.allowedBiomes.remove(biome);
        }
    }

    public static void addSpawnBiome(Biome biome) {
        if (!LayeredBiomeSource.allowedBiomes.contains(biome)) {
            LayeredBiomeSource.allowedBiomes.add(biome);
        }
    }

    public static void removeSpawnBiome(Biome biome) {
        if (LayeredBiomeSource.allowedBiomes.contains(biome)) {
            LayeredBiomeSource.allowedBiomes.remove(biome);
        }
    }
}
