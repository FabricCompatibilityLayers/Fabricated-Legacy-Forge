/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.terraingen;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraftforge.event.Event;

public class BiomeEvent extends Event {
    public final Biome biome;

    public BiomeEvent(Biome biome) {
        this.biome = biome;
    }

    @SideOnly(Side.CLIENT)
    public static class BiomeColor extends BiomeEvent {
        public final int originalColor;
        public int newColor;

        public BiomeColor(Biome biome, int original) {
            super(biome);
            this.originalColor = original;
            this.newColor = original;
        }
    }

    public static class BlockReplacement extends BiomeEvent {
        public final int original;
        public int replacement;

        public BlockReplacement(Biome biome, int original, int replacement) {
            super(biome);
            this.original = original;
            this.replacement = replacement;
        }
    }

    public static class CreateDecorator extends BiomeEvent {
        public final BiomeDecorator originalBiomeDecorator;
        public BiomeDecorator newBiomeDecorator;

        public CreateDecorator(Biome biome, BiomeDecorator original) {
            super(biome);
            this.originalBiomeDecorator = original;
            this.newBiomeDecorator = original;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class GetFoliageColor extends BiomeEvent.BiomeColor {
        public GetFoliageColor(Biome biome, int original) {
            super(biome, original);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class GetGrassColor extends BiomeEvent.BiomeColor {
        public GetGrassColor(Biome biome, int original) {
            super(biome, original);
        }
    }

    @HasResult
    public static class GetVillageBlockID extends BiomeEvent.BlockReplacement {
        public GetVillageBlockID(Biome biome, int original, int replacement) {
            super(biome, original, replacement);
        }
    }

    @HasResult
    public static class GetVillageBlockMeta extends BiomeEvent.BlockReplacement {
        public GetVillageBlockMeta(Biome biome, int original, int replacement) {
            super(biome, original, replacement);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class GetWaterColor extends BiomeEvent.BiomeColor {
        public GetWaterColor(Biome biome, int original) {
            super(biome, original);
        }
    }
}
