/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.terraingen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

import java.util.Random;

public abstract class TerrainGen {
    public TerrainGen() {
    }

    public static NoiseGenerator[] getModdedNoiseGenerators(World world, Random rand, NoiseGenerator[] original) {
        InitNoiseGensEvent event = new InitNoiseGensEvent(world, rand, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newNoiseGens;
    }

    public static Carver getModdedMapGen(Carver original, InitMapGenEvent.EventType type) {
        InitMapGenEvent event = new InitMapGenEvent(type, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newGen;
    }

    public static boolean populate(
            ChunkProvider chunkProvider,
            World world,
            Random rand,
            int chunkX,
            int chunkZ,
            boolean hasVillageGenerated,
            net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType type
    ) {
        PopulateChunkEvent.Populate event = new PopulateChunkEvent.Populate(chunkProvider, world, rand, chunkX, chunkZ, hasVillageGenerated, type);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getResult() != Event.Result.DENY;
    }

    public static boolean decorate(
            World world, Random rand, int chunkX, int chunkZ, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType type
    ) {
        DecorateBiomeEvent.Decorate event = new DecorateBiomeEvent.Decorate(world, rand, chunkX, chunkZ, type);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getResult() != Event.Result.DENY;
    }

    public static boolean generateOre(
            World world, Random rand, Feature generator, int worldX, int worldZ, net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType type
    ) {
        OreGenEvent.GenerateMinable event = new OreGenEvent.GenerateMinable(world, rand, generator, worldX, worldZ, type);
        MinecraftForge.ORE_GEN_BUS.post(event);
        return event.getResult() != Event.Result.DENY;
    }

    public static boolean saplingGrowTree(World world, Random rand, int x, int y, int z) {
        SaplingGrowTreeEvent event = new SaplingGrowTreeEvent(world, rand, x, y, z);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getResult() != Event.Result.DENY;
    }
}
