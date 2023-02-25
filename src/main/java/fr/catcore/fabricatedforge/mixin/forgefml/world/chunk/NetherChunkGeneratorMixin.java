package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.structure.NetherFortressStructure;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.NetherChunkGenerator;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.terraingen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetherChunkGenerator.class)
public abstract class NetherChunkGeneratorMixin implements ChunkProvider {
    @Shadow public NetherFortressStructure fortressFeature;

    @Shadow private Carver cave;

    @Shadow private Random random;

    @Shadow private NoiseGenerator field_4807;

    @Shadow private NoiseGenerator field_4808;

    @Shadow private NoiseGenerator field_4809;

    @Shadow private NoiseGenerator field_4810;

    @Shadow private NoiseGenerator field_4811;

    @Shadow public NoiseGenerator field_4798;

    @Shadow public NoiseGenerator field_4799;

    @Shadow private double[] field_4814;

    @Shadow private double[] field_4815;

    @Shadow private double[] field_4816;

    @Shadow
    double[] field_4804;

    @Shadow
    double[] field_4805;

    @Shadow
    double[] field_4801;

    @Shadow
    double[] field_4802;

    @Shadow
    double[] field_4803;

    @Shadow private World world;

    @Inject(method = "<init>", at = @At(value = "NEW", target = "java/util/Random"))
    private void ctrStructureInitialization(World par1World, long par2, CallbackInfo ci) {
        this.fortressFeature = (NetherFortressStructure) TerrainGen.getModdedMapGen(this.fortressFeature, InitMapGenEvent.EventType.NETHER_BRIDGE);
        this.cave = TerrainGen.getModdedMapGen(this.cave, InitMapGenEvent.EventType.NETHER_CAVE);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ctrModded(World par1World, long par2, CallbackInfo ci) {
        NoiseGenerator[] noiseGens = new NoiseGenerator[]{
                this.field_4807, this.field_4808, this.field_4809, this.field_4810, this.field_4811, this.field_4798, this.field_4799
        };
        noiseGens = TerrainGen.getModdedNoiseGenerators(par1World, this.random, noiseGens);
        this.field_4807 = noiseGens[0];
        this.field_4808 = noiseGens[1];
        this.field_4809 = noiseGens[2];
        this.field_4810 = noiseGens[3];
        this.field_4811 = noiseGens[4];
        this.field_4798 = noiseGens[5];
        this.field_4799 = noiseGens[6];
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void method_4000(int par1, int par2, byte[] par3ArrayOfByte) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, par1, par2, par3ArrayOfByte, null);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() != Event.Result.DENY) {
            byte var4 = 64;
            double var5 = 0.03125;
            this.field_4814 = this.field_4810.method_122(this.field_4814, par1 * 16, par2 * 16, 0, 16, 16, 1, var5, var5, 1.0);
            this.field_4815 = this.field_4810.method_122(this.field_4815, par1 * 16, 109, par2 * 16, 16, 1, 16, var5, 1.0, var5);
            this.field_4816 = this.field_4811.method_122(this.field_4816, par1 * 16, par2 * 16, 0, 16, 16, 1, var5 * 2.0, var5 * 2.0, var5 * 2.0);

            for(int var7 = 0; var7 < 16; ++var7) {
                for(int var8 = 0; var8 < 16; ++var8) {
                    boolean var9 = this.field_4814[var7 + var8 * 16] + this.random.nextDouble() * 0.2 > 0.0;
                    boolean var10 = this.field_4815[var7 + var8 * 16] + this.random.nextDouble() * 0.2 > 0.0;
                    int var11 = (int)(this.field_4816[var7 + var8 * 16] / 3.0 + 3.0 + this.random.nextDouble() * 0.25);
                    int var12 = -1;
                    byte var13 = (byte) Block.NETHERRACK.id;
                    byte var14 = (byte)Block.NETHERRACK.id;

                    for(int var15 = 127; var15 >= 0; --var15) {
                        int var16 = (var8 * 16 + var7) * 128 + var15;
                        if (var15 < 127 - this.random.nextInt(5) && var15 > 0 + this.random.nextInt(5)) {
                            byte var17 = par3ArrayOfByte[var16];
                            if (var17 == 0) {
                                var12 = -1;
                            } else if (var17 == Block.NETHERRACK.id) {
                                if (var12 == -1) {
                                    if (var11 <= 0) {
                                        var13 = 0;
                                        var14 = (byte)Block.NETHERRACK.id;
                                    } else if (var15 >= var4 - 4 && var15 <= var4 + 1) {
                                        var13 = (byte)Block.NETHERRACK.id;
                                        var14 = (byte)Block.NETHERRACK.id;
                                        if (var10) {
                                            var13 = (byte)Block.GRAVEL_BLOCK.id;
                                        }

                                        if (var10) {
                                            var14 = (byte)Block.NETHERRACK.id;
                                        }

                                        if (var9) {
                                            var13 = (byte)Block.SOULSAND.id;
                                        }

                                        if (var9) {
                                            var14 = (byte)Block.SOULSAND.id;
                                        }
                                    }

                                    if (var15 < var4 && var13 == 0) {
                                        var13 = (byte)Block.LAVA.id;
                                    }

                                    var12 = var11;
                                    if (var15 >= var4 - 1) {
                                        par3ArrayOfByte[var16] = var13;
                                    } else {
                                        par3ArrayOfByte[var16] = var14;
                                    }
                                } else if (var12 > 0) {
                                    --var12;
                                    par3ArrayOfByte[var16] = var14;
                                }
                            }
                        } else {
                            par3ArrayOfByte[var16] = (byte)Block.BEDROCK.id;
                        }
                    }
                }
            }
        }
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public double[] method_3999(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7) {
        ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(this, par1ArrayOfDouble, par2, par3, par4, par5, par6, par7);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) {
            return event.noisefield;
        } else {
            if (par1ArrayOfDouble == null) {
                par1ArrayOfDouble = new double[par5 * par6 * par7];
            }

            double var8 = 684.412;
            double var10 = 2053.236;
            this.field_4804 = this.field_4798.method_122(this.field_4804, par2, par3, par4, par5, 1, par7, 1.0, 0.0, 1.0);
            this.field_4805 = this.field_4799.method_122(this.field_4805, par2, par3, par4, par5, 1, par7, 100.0, 0.0, 100.0);
            this.field_4801 = this.field_4809.method_122(this.field_4801, par2, par3, par4, par5, par6, par7, var8 / 80.0, var10 / 60.0, var8 / 80.0);
            this.field_4802 = this.field_4807.method_122(this.field_4802, par2, par3, par4, par5, par6, par7, var8, var10, var8);
            this.field_4803 = this.field_4808.method_122(this.field_4803, par2, par3, par4, par5, par6, par7, var8, var10, var8);
            int var12 = 0;
            int var13 = 0;
            double[] var14 = new double[par6];

            for(int var15 = 0; var15 < par6; ++var15) {
                var14[var15] = Math.cos((double)var15 * Math.PI * 6.0 / (double)par6) * 2.0;
                double var16 = (double)var15;
                if (var15 > par6 / 2) {
                    var16 = (double)(par6 - 1 - var15);
                }

                if (var16 < 4.0) {
                    var16 = 4.0 - var16;
                    var14[var15] -= var16 * var16 * var16 * 10.0;
                }
            }

            for(int var37 = 0; var37 < par5; ++var37) {
                for(int var36 = 0; var36 < par7; ++var36) {
                    double var17 = (this.field_4804[var13] + 256.0) / 512.0;
                    if (var17 > 1.0) {
                        var17 = 1.0;
                    }

                    double var19 = 0.0;
                    double var21 = this.field_4805[var13] / 8000.0;
                    if (var21 < 0.0) {
                        var21 = -var21;
                    }

                    var21 = var21 * 3.0 - 3.0;
                    if (var21 < 0.0) {
                        var21 /= 2.0;
                        if (var21 < -1.0) {
                            var21 = -1.0;
                        }

                        var21 /= 1.4;
                        var21 /= 2.0;
                        var17 = 0.0;
                    } else {
                        if (var21 > 1.0) {
                            var21 = 1.0;
                        }

                        var21 /= 6.0;
                    }

                    var17 += 0.5;
                    var21 = var21 * (double)par6 / 16.0;
                    ++var13;

                    for(int var23 = 0; var23 < par6; ++var23) {
                        double var24 = 0.0;
                        double var26 = var14[var23];
                        double var28 = this.field_4802[var12] / 512.0;
                        double var30 = this.field_4803[var12] / 512.0;
                        double var32 = (this.field_4801[var12] / 10.0 + 1.0) / 2.0;
                        if (var32 < 0.0) {
                            var24 = var28;
                        } else if (var32 > 1.0) {
                            var24 = var30;
                        } else {
                            var24 = var28 + (var30 - var28) * var32;
                        }

                        var24 -= var26;
                        if (var23 > par6 - 4) {
                            double var34 = (double)((float)(var23 - (par6 - 4)) / 3.0F);
                            var24 = var24 * (1.0 - var34) + -10.0 * var34;
                        }

                        if ((double)var23 < var19) {
                            double var34 = (var19 - (double)var23) / 4.0;
                            if (var34 < 0.0) {
                                var34 = 0.0;
                            }

                            if (var34 > 1.0) {
                                var34 = 1.0;
                            }

                            var24 = var24 * (1.0 - var34) + -10.0 * var34;
                        }

                        par1ArrayOfDouble[var12] = var24;
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
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(par1IChunkProvider, this.world, this.random, par2, par3, false));
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        this.fortressFeature.method_48(this.world, this.random, par2, par3);
        boolean doGen = TerrainGen.populate(
                par1IChunkProvider,
                this.world,
                this.random,
                par2,
                par3,
                false,
                net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA
        );

        for(int var6 = 0; doGen && var6 < 8; ++var6) {
            int var7 = var4 + this.random.nextInt(16) + 8;
            int var8 = this.random.nextInt(120) + 4;
            int var9 = var5 + this.random.nextInt(16) + 8;
            new NetherSpringFeature(Block.field_336.id).method_4028(this.world, this.random, var7, var8, var9);
        }

        int var12 = this.random.nextInt(this.random.nextInt(10) + 1) + 1;
        doGen = TerrainGen.populate(
                par1IChunkProvider, this.world, this.random, par2, par3, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.FIRE
        );

        for(int var7 = 0; doGen && var7 < var12; ++var7) {
            int var8 = var4 + this.random.nextInt(16) + 8;
            int var9 = this.random.nextInt(120) + 4;
            int var10 = var5 + this.random.nextInt(16) + 8;
            new NetherFireFeature().method_4028(this.world, this.random, var8, var9, var10);
        }

        var12 = this.random.nextInt(this.random.nextInt(10) + 1);
        doGen = TerrainGen.populate(
                par1IChunkProvider, this.world, this.random, par2, par3, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.GLOWSTONE
        );

        for(int var15 = 0; doGen && var15 < var12; ++var15) {
            int var8 = var4 + this.random.nextInt(16) + 8;
            int var9 = this.random.nextInt(120) + 4;
            int var10 = var5 + this.random.nextInt(16) + 8;
            new GlowstoneFeature().method_4028(this.world, this.random, var8, var9, var10);
        }

        for(int var16 = 0; doGen && var16 < 10; ++var16) {
            int var8 = var4 + this.random.nextInt(16) + 8;
            int var9 = this.random.nextInt(128);
            int var10 = var5 + this.random.nextInt(16) + 8;
            new GlowstoneClusterFeature().method_4028(this.world, this.random, var8, var9, var10);
        }

        MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(this.world, this.random, var4, var5));
        doGen = TerrainGen.decorate(this.world, this.random, var4, var5, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM);
        if (doGen && this.random.nextInt(1) == 0) {
            int var17 = var4 + this.random.nextInt(16) + 8;
            int var8 = this.random.nextInt(128);
            int var9 = var5 + this.random.nextInt(16) + 8;
            new FlowerPatchFeature(Block.BROWN_MUSHROOM.id).method_4028(this.world, this.random, var17, var8, var9);
        }

        if (doGen && this.random.nextInt(1) == 0) {
            int var18 = var4 + this.random.nextInt(16) + 8;
            int var8 = this.random.nextInt(128);
            int var9 = var5 + this.random.nextInt(16) + 8;
            new FlowerPatchFeature(Block.RED_MUSHROOM.id).method_4028(this.world, this.random, var18, var8, var9);
        }

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(this.world, this.random, var4, var5));
        MinecraftForge.EVENT_BUS
                .post(new net.minecraftforge.event.terraingen.PopulateChunkEvent.Post(par1IChunkProvider, this.world, this.random, par2, par3, false));
        SandBlock.field_318 = false;
    }
}
