package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.entity.MobSpawnerHelper;
import net.minecraft.structure.MineshaftStructure;
import net.minecraft.structure.StrongholdStructure;
import net.minecraft.structure.TempleStructure;
import net.minecraft.structure.VillageStructure;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.SurfaceChunkGenerator;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.minecraft.world.gen.feature.LakesFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SurfaceChunkGenerator.class)
public abstract class SurfaceChunkGeneratorMixin implements ChunkProvider {
    @Shadow private Carver caveCarver;

    @Shadow private StrongholdStructure strongholdGenerator;

    @Shadow private VillageStructure village;

    @Shadow private MineshaftStructure mineshaft;

    @Shadow private TempleStructure witchHut;

    @Shadow private Carver ravineCarver;

    @Shadow private Random random;

    @Shadow private NoiseGenerator field_4832;

    @Shadow private NoiseGenerator field_4833;

    @Shadow private NoiseGenerator field_4834;

    @Shadow private NoiseGenerator field_4835;

    @Shadow public NoiseGenerator field_4821;

    @Shadow public NoiseGenerator field_4822;

    @Shadow public NoiseGenerator field_4823;

    @Shadow private double[] field_4839;

    @Shadow
    float[] field_4829;

    @Shadow
    double[] field_4827;

    @Shadow
    double[] field_4828;

    @Shadow
    double[] field_4824;

    @Shadow
    double[] field_4825;

    @Shadow
    double[] field_4826;

    @Shadow private Biome[] biomes;

    @Shadow private World world;

    @Shadow @Final private boolean hasStructures;

    @Inject(method = "<init>", at = @At(value = "NEW", target = "java/util/Random"))
    private void ctrStructureInitialization(World par1World, long par2, boolean par4, CallbackInfo ci) {
        this.caveCarver = TerrainGen.getModdedMapGen(this.caveCarver, InitMapGenEvent.EventType.CAVE);
        this.strongholdGenerator = (StrongholdStructure)TerrainGen.getModdedMapGen(this.strongholdGenerator, InitMapGenEvent.EventType.STRONGHOLD);
        this.village = (VillageStructure)TerrainGen.getModdedMapGen(this.village, InitMapGenEvent.EventType.VILLAGE);
        this.mineshaft = (MineshaftStructure)TerrainGen.getModdedMapGen(this.mineshaft, InitMapGenEvent.EventType.MINESHAFT);
        this.witchHut = (TempleStructure)TerrainGen.getModdedMapGen(this.witchHut, InitMapGenEvent.EventType.SCATTERED_FEATURE);
        this.ravineCarver = TerrainGen.getModdedMapGen(this.ravineCarver, InitMapGenEvent.EventType.RAVINE);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ctrModded(World par1World, long par2, boolean par4, CallbackInfo ci) {
        NoiseGenerator[] noiseGens = new NoiseGenerator[]{
                this.field_4832, this.field_4833, this.field_4834, this.field_4835, this.field_4821, this.field_4822, this.field_4823
        };
        noiseGens = TerrainGen.getModdedNoiseGenerators(par1World, this.random, noiseGens);
        this.field_4832 = noiseGens[0];
        this.field_4833 = noiseGens[1];
        this.field_4834 = noiseGens[2];
        this.field_4835 = noiseGens[3];
        this.field_4821 = noiseGens[4];
        this.field_4822 = noiseGens[5];
        this.field_4823 = noiseGens[6];
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void method_4008(int par1, int par2, byte[] par3ArrayOfByte, Biome[] par4ArrayOfBiomeGenBase) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, par1, par2, par3ArrayOfByte, par4ArrayOfBiomeGenBase);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() != Event.Result.DENY) {
            byte var5 = 63;
            double var6 = 0.03125;
            this.field_4839 = this.field_4835.method_122(this.field_4839, par1 * 16, par2 * 16, 0, 16, 16, 1, var6 * 2.0, var6 * 2.0, var6 * 2.0);

            for(int var8 = 0; var8 < 16; ++var8) {
                for(int var9 = 0; var9 < 16; ++var9) {
                    Biome var10 = par4ArrayOfBiomeGenBase[var9 + var8 * 16];
                    float var11 = var10.getTemperatureValue();
                    int var12 = (int)(this.field_4839[var8 + var9 * 16] / 3.0 + 3.0 + this.random.nextDouble() * 0.25);
                    int var13 = -1;
                    byte var14 = var10.field_4617;
                    byte var15 = var10.field_4618;

                    for(int var16 = 127; var16 >= 0; --var16) {
                        int var17 = (var9 * 16 + var8) * 128 + var16;
                        if (var16 <= 0 + this.random.nextInt(5)) {
                            par3ArrayOfByte[var17] = (byte) Block.BEDROCK.id;
                        } else {
                            byte var18 = par3ArrayOfByte[var17];
                            if (var18 == 0) {
                                var13 = -1;
                            } else if (var18 == Block.STONE_BLOCK.id) {
                                if (var13 == -1) {
                                    if (var12 <= 0) {
                                        var14 = 0;
                                        var15 = (byte)Block.STONE_BLOCK.id;
                                    } else if (var16 >= var5 - 4 && var16 <= var5 + 1) {
                                        var14 = var10.field_4617;
                                        var15 = var10.field_4618;
                                    }

                                    if (var16 < var5 && var14 == 0) {
                                        if (var11 < 0.15F) {
                                            var14 = (byte)Block.ICE.id;
                                        } else {
                                            var14 = (byte)Block.WATER.id;
                                        }
                                    }

                                    var13 = var12;
                                    if (var16 >= var5 - 1) {
                                        par3ArrayOfByte[var17] = var14;
                                    } else {
                                        par3ArrayOfByte[var17] = var15;
                                    }
                                } else if (var13 > 0) {
                                    --var13;
                                    par3ArrayOfByte[var17] = var15;
                                    if (var13 == 0 && var15 == Block.SAND_BLOCK.id) {
                                        var13 = this.random.nextInt(4);
                                        var15 = (byte)Block.SANDSTONE.id;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public double[] method_4009(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7) {
        ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(this, par1ArrayOfDouble, par2, par3, par4, par5, par6, par7);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) {
            return event.noisefield;
        } else {
            if (par1ArrayOfDouble == null) {
                par1ArrayOfDouble = new double[par5 * par6 * par7];
            }

            if (this.field_4829 == null) {
                this.field_4829 = new float[25];

                for(int var8 = -2; var8 <= 2; ++var8) {
                    for(int var9 = -2; var9 <= 2; ++var9) {
                        float var10 = 10.0F / MathHelper.sqrt((float)(var8 * var8 + var9 * var9) + 0.2F);
                        this.field_4829[var8 + 2 + (var9 + 2) * 5] = var10;
                    }
                }
            }

            double var44 = 684.412;
            double var45 = 684.412;
            this.field_4827 = this.field_4821.method_121(this.field_4827, par2, par4, par5, par7, 1.121, 1.121, 0.5);
            this.field_4828 = this.field_4822.method_121(this.field_4828, par2, par4, par5, par7, 200.0, 200.0, 0.5);
            this.field_4824 = this.field_4834.method_122(this.field_4824, par2, par3, par4, par5, par6, par7, var44 / 80.0, var45 / 160.0, var44 / 80.0);
            this.field_4825 = this.field_4832.method_122(this.field_4825, par2, par3, par4, par5, par6, par7, var44, var45, var44);
            this.field_4826 = this.field_4833.method_122(this.field_4826, par2, par3, par4, par5, par6, par7, var44, var45, var44);
            boolean var43 = false;
            boolean var42 = false;
            int var12 = 0;
            int var13 = 0;

            for(int var14 = 0; var14 < par5; ++var14) {
                for(int var15 = 0; var15 < par7; ++var15) {
                    float var16 = 0.0F;
                    float var17 = 0.0F;
                    float var18 = 0.0F;
                    byte var19 = 2;
                    Biome var20 = this.biomes[var14 + 2 + (var15 + 2) * (par5 + 5)];

                    for(int var21 = -var19; var21 <= var19; ++var21) {
                        for(int var22 = -var19; var22 <= var19; ++var22) {
                            Biome var23 = this.biomes[var14 + var21 + 2 + (var15 + var22 + 2) * (par5 + 5)];
                            float var24 = this.field_4829[var21 + 2 + (var22 + 2) * 5] / (var23.depth + 2.0F);
                            if (var23.depth > var20.depth) {
                                var24 /= 2.0F;
                            }

                            var16 += var23.variationModifier * var24;
                            var17 += var23.depth * var24;
                            var18 += var24;
                        }
                    }

                    var16 /= var18;
                    var17 /= var18;
                    var16 = var16 * 0.9F + 0.1F;
                    var17 = (var17 * 4.0F - 1.0F) / 8.0F;
                    double var47 = this.field_4828[var13] / 8000.0;
                    if (var47 < 0.0) {
                        var47 = -var47 * 0.3;
                    }

                    var47 = var47 * 3.0 - 2.0;
                    if (var47 < 0.0) {
                        var47 /= 2.0;
                        if (var47 < -1.0) {
                            var47 = -1.0;
                        }

                        var47 /= 1.4;
                        var47 /= 2.0;
                    } else {
                        if (var47 > 1.0) {
                            var47 = 1.0;
                        }

                        var47 /= 8.0;
                    }

                    ++var13;

                    for(int var46 = 0; var46 < par6; ++var46) {
                        double var48 = (double)var17;
                        double var26 = (double)var16;
                        var48 += var47 * 0.2;
                        var48 = var48 * (double)par6 / 16.0;
                        double var28 = (double)par6 / 2.0 + var48 * 4.0;
                        double var30 = 0.0;
                        double var32 = ((double)var46 - var28) * 12.0 * 128.0 / 128.0 / var26;
                        if (var32 < 0.0) {
                            var32 *= 4.0;
                        }

                        double var34 = this.field_4825[var12] / 512.0;
                        double var36 = this.field_4826[var12] / 512.0;
                        double var38 = (this.field_4824[var12] / 10.0 + 1.0) / 2.0;
                        if (var38 < 0.0) {
                            var30 = var34;
                        } else if (var38 > 1.0) {
                            var30 = var36;
                        } else {
                            var30 = var34 + (var36 - var34) * var38;
                        }

                        var30 -= var32;
                        if (var46 > par6 - 4) {
                            double var40 = (double)((float)(var46 - (par6 - 4)) / 3.0F);
                            var30 = var30 * (1.0 - var40) + -10.0 * var40;
                        }

                        par1ArrayOfDouble[var12] = var30;
                        ++var12;
                    }
                }
            }

            return par1ArrayOfDouble;
        }
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void decorateChunk(ChunkProvider par1IChunkProvider, int par2, int par3) {
        SandBlock.field_318 = true;
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        Biome var6 = this.world.getBiome(var4 + 16, var5 + 16);
        this.random.setSeed(this.world.getSeed());
        long var7 = this.random.nextLong() / 2L * 2L + 1L;
        long var9 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)par2 * var7 + (long)par3 * var9 ^ this.world.getSeed());
        boolean var11 = false;
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(par1IChunkProvider, this.world, this.random, par2, par3, var11));
        if (this.hasStructures) {
            this.mineshaft.method_48(this.world, this.random, par2, par3);
            var11 = this.village.method_48(this.world, this.random, par2, par3);
            this.strongholdGenerator.method_48(this.world, this.random, par2, par3);
            this.witchHut.method_48(this.world, this.random, par2, par3);
        }

        if (TerrainGen.populate(
                par1IChunkProvider, this.world, this.random, par2, par3, var11, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE
        )
                && !var11
                && this.random.nextInt(4) == 0) {
            int var12 = var4 + this.random.nextInt(16) + 8;
            int var13 = this.random.nextInt(128);
            int var14 = var5 + this.random.nextInt(16) + 8;
            new LakesFeature(Block.WATER.id).method_4028(this.world, this.random, var12, var13, var14);
        }

        if (TerrainGen.populate(
                par1IChunkProvider, this.world, this.random, par2, par3, var11, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA
        )
                && !var11
                && this.random.nextInt(8) == 0) {
            int var12 = var4 + this.random.nextInt(16) + 8;
            int var13 = this.random.nextInt(this.random.nextInt(120) + 8);
            int var14 = var5 + this.random.nextInt(16) + 8;
            if (var13 < 63 || this.random.nextInt(10) == 0) {
                new LakesFeature(Block.LAVA.id).method_4028(this.world, this.random, var12, var13, var14);
            }
        }

        boolean doGen = TerrainGen.populate(
                par1IChunkProvider, this.world, this.random, par2, par3, var11, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON
        );

        for(int var12 = 0; doGen && var12 < 8; ++var12) {
            int var13 = var4 + this.random.nextInt(16) + 8;
            int var14 = this.random.nextInt(128);
            int var15 = var5 + this.random.nextInt(16) + 8;
            if (new DungeonFeature().method_4028(this.world, this.random, var13, var14, var15)) {
            }
        }

        var6.decorate(this.world, this.random, var4, var5);
        MobSpawnerHelper.spawnMobs(this.world, var6, var4 + 8, var5 + 8, 16, 16, this.random);
        var4 += 8;
        var5 += 8;
        doGen = TerrainGen.populate(
                par1IChunkProvider, this.world, this.random, par2, par3, var11, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE
        );

        for(int var21 = 0; doGen && var21 < 16; ++var21) {
            for(int var13 = 0; var13 < 16; ++var13) {
                int var14 = this.world.getSurfaceY(var4 + var21, var5 + var13);
                if (this.world.method_3730(var21 + var4, var14 - 1, var13 + var5)) {
                    this.world.method_3690(var21 + var4, var14 - 1, var13 + var5, Block.ICE.id);
                }

                if (this.world.canSnowLand(var21 + var4, var14, var13 + var5)) {
                    this.world.method_3690(var21 + var4, var14, var13 + var5, Block.SNOW_LAYER.id);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(par1IChunkProvider, this.world, this.random, par2, par3, var11));
        SandBlock.field_318 = false;
    }
}
