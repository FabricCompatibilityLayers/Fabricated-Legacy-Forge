/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import fr.catcore.fabricatedforge.forged.reflection.ReflectedLayeredBiomeSource;
import fr.catcore.fabricatedforge.forged.reflection.ReflectedStrongholdStructure;
import net.minecraft.structure.VillageStructure;
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
        if (!ReflectedStrongholdStructure.allowedBiomes.contains(biome)) {
            ReflectedStrongholdStructure.allowedBiomes.add(biome);
        }
    }

    public static void removeStrongholdBiome(Biome biome) {
        if (ReflectedStrongholdStructure.allowedBiomes.contains(biome)) {
            ReflectedStrongholdStructure.allowedBiomes.remove(biome);
        }
    }

    public static void addSpawnBiome(Biome biome) {
        if (!ReflectedLayeredBiomeSource.allowedBiomes.contains(biome)) {
            ReflectedLayeredBiomeSource.allowedBiomes.add(biome);
        }
    }

    public static void removeSpawnBiome(Biome biome) {
        if (ReflectedLayeredBiomeSource.allowedBiomes.contains(biome)) {
            ReflectedLayeredBiomeSource.allowedBiomes.remove(biome);
        }
    }
}
