package fr.catcore.fabricatedforge.mixin.forgefml.world.biome;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.DeadbushFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PumpkinFeature;
import net.minecraft.world.gen.feature.SpringFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(BiomeDecorator.class)
public abstract class BiomeDecoratorMixin {
    @Shadow public World world;

    @Shadow public Random random;

    @Shadow public int field_4689;

    @Shadow public int field_4690;

    @Shadow public int sandDisksPerChunk;

    @Shadow public Feature sandDiskFeature;

    @Shadow public int clayPerChunk;

    @Shadow public Feature clayFeature;

    @Shadow public int gravelDisksPerChunk;

    @Shadow public int treesPerChunk;

    @Shadow public Biome field_4691;

    @Shadow public int hugeMushroomsPerChunk;

    @Shadow public Feature hugeMushroomFeature;

    @Shadow public int flowersPerChunk;

    @Shadow public Feature field_4703;

    @Shadow public Feature field_4704;

    @Shadow public int grassPerChunk;

    @Shadow public int deadBushesPerChunk;

    @Shadow public int lilyPadsPerChunk;

    @Shadow public Feature lilyPadFeature;

    @Shadow public int mushroomsPerChunk;

    @Shadow public Feature brownMushroomFeature;

    @Shadow public Feature redMushroomFeature;

    @Shadow public int sugarcanePerChunk;

    @Shadow public Feature sugarcaneFeature;

    @Shadow public int cactusPerChunk;

    @Shadow public Feature cactusFeature;

    @Shadow public boolean generateLakes;

    @Shadow public Feature dirtFeature;

    @Shadow public abstract void generateOre(int count, Feature feature, int minHeight, int maxHieght);

    @Shadow public Feature gravelFeature;

    @Shadow public Feature coalOreFeature;

    @Shadow public Feature ironOreFeature;

    @Shadow public Feature goldOreFeature;

    @Shadow public Feature field_4700;

    @Shadow public Feature field_4701;

    @Shadow public Feature lapisOreFeature;

    @Shadow public abstract void method_3850(int count, Feature feature, int minHeight, int maxHeight);

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void method_3846() {
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(this.world, this.random, this.field_4689, this.field_4690));
        this.generateOres();
        boolean doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.SAND);

        for(int var1 = 0; doGen && var1 < this.sandDisksPerChunk; ++var1) {
            int var2 = this.field_4689 + this.random.nextInt(16) + 8;
            int var3 = this.field_4690 + this.random.nextInt(16) + 8;
            this.sandDiskFeature.method_4028(this.world, this.random, var2, this.world.method_3708(var2, var3), var3);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.CLAY);

        for(int var8 = 0; doGen && var8 < this.clayPerChunk; ++var8) {
            int var2 = this.field_4689 + this.random.nextInt(16) + 8;
            int var3 = this.field_4690 + this.random.nextInt(16) + 8;
            this.clayFeature.method_4028(this.world, this.random, var2, this.world.method_3708(var2, var3), var3);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.SAND_PASS2);

        for(int var9 = 0; doGen && var9 < this.gravelDisksPerChunk; ++var9) {
            int var2 = this.field_4689 + this.random.nextInt(16) + 8;
            int var3 = this.field_4690 + this.random.nextInt(16) + 8;
            this.sandDiskFeature.method_4028(this.world, this.random, var2, this.world.method_3708(var2, var3), var3);
        }

        int var10 = this.treesPerChunk;
        if (this.random.nextInt(10) == 0) {
            ++var10;
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.TREE);

        for(int var2 = 0; doGen && var2 < var10; ++var2) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            Feature var5 = this.field_4691.method_3822(this.random);
            var5.method_4025(1.0, 1.0, 1.0);
            var5.method_4028(this.world, this.random, var3, this.world.getHighestBlockY(var3, var4), var4);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM);

        for(int var14 = 0; doGen && var14 < this.hugeMushroomsPerChunk; ++var14) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            this.hugeMushroomFeature.method_4028(this.world, this.random, var3, this.world.getHighestBlockY(var3, var4), var4);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.FLOWERS);

        for(int var15 = 0; doGen && var15 < this.flowersPerChunk; ++var15) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.random.nextInt(128);
            int var7 = this.field_4690 + this.random.nextInt(16) + 8;
            this.field_4703.method_4028(this.world, this.random, var3, var4, var7);
            if (this.random.nextInt(4) == 0) {
                var3 = this.field_4689 + this.random.nextInt(16) + 8;
                var4 = this.random.nextInt(128);
                var7 = this.field_4690 + this.random.nextInt(16) + 8;
                this.field_4704.method_4028(this.world, this.random, var3, var4, var7);
            }
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.GRASS);

        for(int var16 = 0; doGen && var16 < this.grassPerChunk; ++var16) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.random.nextInt(128);
            int var7 = this.field_4690 + this.random.nextInt(16) + 8;
            Feature var6 = this.field_4691.method_3828(this.random);
            var6.method_4028(this.world, this.random, var3, var4, var7);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.DEAD_BUSH);

        for(int var17 = 0; doGen && var17 < this.deadBushesPerChunk; ++var17) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.random.nextInt(128);
            int var7 = this.field_4690 + this.random.nextInt(16) + 8;
            new DeadbushFeature(Block.DEADBUSH.id).method_4028(this.world, this.random, var3, var4, var7);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.LILYPAD);

        for(int var18 = 0; doGen && var18 < this.lilyPadsPerChunk; ++var18) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            int var7 = this.random.nextInt(128);

            while(var7 > 0 && this.world.getBlock(var3, var7 - 1, var4) == 0) {
                --var7;
            }

            this.lilyPadFeature.method_4028(this.world, this.random, var3, var7, var4);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.SHROOM);

        for(int var19 = 0; doGen && var19 < this.mushroomsPerChunk; ++var19) {
            if (this.random.nextInt(4) == 0) {
                int var3 = this.field_4689 + this.random.nextInt(16) + 8;
                int var4 = this.field_4690 + this.random.nextInt(16) + 8;
                int var7 = this.world.getHighestBlockY(var3, var4);
                this.brownMushroomFeature.method_4028(this.world, this.random, var3, var7, var4);
            }

            if (this.random.nextInt(8) == 0) {
                int var3 = this.field_4689 + this.random.nextInt(16) + 8;
                int var4 = this.field_4690 + this.random.nextInt(16) + 8;
                int var7 = this.random.nextInt(128);
                this.redMushroomFeature.method_4028(this.world, this.random, var3, var7, var4);
            }
        }

        if (doGen && this.random.nextInt(4) == 0) {
            int var20 = this.field_4689 + this.random.nextInt(16) + 8;
            int var3 = this.random.nextInt(128);
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            this.brownMushroomFeature.method_4028(this.world, this.random, var20, var3, var4);
        }

        if (doGen && this.random.nextInt(8) == 0) {
            int var21 = this.field_4689 + this.random.nextInt(16) + 8;
            int var3 = this.random.nextInt(128);
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            this.redMushroomFeature.method_4028(this.world, this.random, var21, var3, var4);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.REED);

        for(int var22 = 0; doGen && var22 < this.sugarcanePerChunk; ++var22) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            int var7 = this.random.nextInt(128);
            this.sugarcaneFeature.method_4028(this.world, this.random, var3, var7, var4);
        }

        for(int var23 = 0; doGen && var23 < 10; ++var23) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.random.nextInt(128);
            int var7 = this.field_4690 + this.random.nextInt(16) + 8;
            this.sugarcaneFeature.method_4028(this.world, this.random, var3, var4, var7);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.PUMPKIN);
        if (doGen && this.random.nextInt(32) == 0) {
            int var24 = this.field_4689 + this.random.nextInt(16) + 8;
            int var3 = this.random.nextInt(128);
            int var4 = this.field_4690 + this.random.nextInt(16) + 8;
            new PumpkinFeature().method_4028(this.world, this.random, var24, var3, var4);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.CACTUS);

        for(int var25 = 0; doGen && var25 < this.cactusPerChunk; ++var25) {
            int var3 = this.field_4689 + this.random.nextInt(16) + 8;
            int var4 = this.random.nextInt(128);
            int var7 = this.field_4690 + this.random.nextInt(16) + 8;
            this.cactusFeature.method_4028(this.world, this.random, var3, var4, var7);
        }

        doGen = TerrainGen.decorate(this.world, this.random, this.field_4689, this.field_4690, DecorateBiomeEvent.Decorate.EventType.LAKE);
        if (doGen && this.generateLakes) {
            for(int var26 = 0; var26 < 50; ++var26) {
                int var3 = this.field_4689 + this.random.nextInt(16) + 8;
                int var4 = this.random.nextInt(this.random.nextInt(120) + 8);
                int var7 = this.field_4690 + this.random.nextInt(16) + 8;
                new SpringFeature(Block.field_334.id).method_4028(this.world, this.random, var3, var4, var7);
            }

            for(int var27 = 0; var27 < 20; ++var27) {
                int var3 = this.field_4689 + this.random.nextInt(16) + 8;
                int var4 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
                int var7 = this.field_4690 + this.random.nextInt(16) + 8;
                new SpringFeature(Block.field_336.id).method_4028(this.world, this.random, var3, var4, var7);
            }
        }

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(this.world, this.random, this.field_4689, this.field_4690));
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void generateOres() {
        MinecraftForge.ORE_GEN_BUS.post(new net.minecraftforge.event.terraingen.OreGenEvent.Pre(this.world, this.random, this.field_4689, this.field_4690));
        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.dirtFeature,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIRT
        )) {
            this.generateOre(20, this.dirtFeature, 0, 128);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.gravelFeature,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GRAVEL
        )) {
            this.generateOre(10, this.gravelFeature, 0, 128);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.coalOreFeature,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.COAL
        )) {
            this.generateOre(20, this.coalOreFeature, 0, 128);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.ironOreFeature,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.IRON
        )) {
            this.generateOre(20, this.ironOreFeature, 0, 64);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.goldOreFeature,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GOLD
        )) {
            this.generateOre(2, this.goldOreFeature, 0, 32);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.field_4700,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.REDSTONE
        )) {
            this.generateOre(8, this.field_4700, 0, 16);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.field_4701,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIAMOND
        )) {
            this.generateOre(1, this.field_4701, 0, 16);
        }

        if (TerrainGen.generateOre(
                this.world,
                this.random,
                this.lapisOreFeature,
                this.field_4689,
                this.field_4690,
                net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.LAPIS
        )) {
            this.method_3850(1, this.lapisOreFeature, 16, 16);
        }

        MinecraftForge.ORE_GEN_BUS.post(new net.minecraftforge.event.terraingen.OreGenEvent.Post(this.world, this.random, this.field_4689, this.field_4690));
    }
}
