package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.EndChunkGenerator;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndChunkGenerator.class)
public abstract class EndChunkGeneratorMixin implements ChunkProvider {
    @Shadow private NoiseGenerator field_4856;

    @Shadow private NoiseGenerator field_4857;

    @Shadow private NoiseGenerator field_4858;

    @Shadow public NoiseGenerator field_4847;

    @Shadow public NoiseGenerator field_4848;

    @Shadow private Random random;

    @Shadow
    double[] field_4852;

    @Shadow
    double[] field_4853;

    @Shadow
    double[] field_4849;

    @Shadow
    double[] field_4850;

    @Shadow
    double[] field_4851;

    @Shadow private World world;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addModdedCtr(World par1World, long par2, CallbackInfo ci) {
        NoiseGenerator[] noiseGens = new NoiseGenerator[]{this.field_4856, this.field_4857, this.field_4858, this.field_4847, this.field_4848};
        noiseGens = TerrainGen.getModdedNoiseGenerators(par1World, this.random, noiseGens);
        this.field_4856 = noiseGens[0];
        this.field_4857 = noiseGens[1];
        this.field_4858 = noiseGens[2];
        this.field_4847 = noiseGens[3];
        this.field_4848 = noiseGens[4];
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void method_4012(int par1, int par2, byte[] par3ArrayOfByte, Biome[] par4ArrayOfBiomeGenBase) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, par1, par2, par3ArrayOfByte, par4ArrayOfBiomeGenBase);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() != Event.Result.DENY) {
            for(int var5 = 0; var5 < 16; ++var5) {
                for(int var6 = 0; var6 < 16; ++var6) {
                    byte var7 = 1;
                    int var8 = -1;
                    byte var9 = (byte) Block.END_STONE.id;
                    byte var10 = (byte)Block.END_STONE.id;

                    for(int var11 = 127; var11 >= 0; --var11) {
                        int var12 = (var6 * 16 + var5) * 128 + var11;
                        byte var13 = par3ArrayOfByte[var12];
                        if (var13 == 0) {
                            var8 = -1;
                        } else if (var13 == Block.STONE_BLOCK.id) {
                            if (var8 == -1) {
                                if (var7 <= 0) {
                                    var9 = 0;
                                    var10 = (byte)Block.END_STONE.id;
                                }

                                var8 = var7;
                                if (var11 >= 0) {
                                    par3ArrayOfByte[var12] = var9;
                                } else {
                                    par3ArrayOfByte[var12] = var10;
                                }
                            } else if (var8 > 0) {
                                --var8;
                                par3ArrayOfByte[var12] = var10;
                            }
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
    private double[] method_4011(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7) {
        ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(this, par1ArrayOfDouble, par2, par3, par4, par5, par6, par7);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) {
            return event.noisefield;
        } else {
            if (par1ArrayOfDouble == null) {
                par1ArrayOfDouble = new double[par5 * par6 * par7];
            }

            double var8 = 684.412;
            double var10 = 684.412;
            this.field_4852 = this.field_4847.method_121(this.field_4852, par2, par4, par5, par7, 1.121, 1.121, 0.5);
            this.field_4853 = this.field_4848.method_121(this.field_4853, par2, par4, par5, par7, 200.0, 200.0, 0.5);
            var8 *= 2.0;
            this.field_4849 = this.field_4858.method_122(this.field_4849, par2, par3, par4, par5, par6, par7, var8 / 80.0, var10 / 160.0, var8 / 80.0);
            this.field_4850 = this.field_4856.method_122(this.field_4850, par2, par3, par4, par5, par6, par7, var8, var10, var8);
            this.field_4851 = this.field_4857.method_122(this.field_4851, par2, par3, par4, par5, par6, par7, var8, var10, var8);
            int var12 = 0;
            int var13 = 0;

            for(int var14 = 0; var14 < par5; ++var14) {
                for(int var15 = 0; var15 < par7; ++var15) {
                    double var16 = (this.field_4852[var13] + 256.0) / 512.0;
                    if (var16 > 1.0) {
                        var16 = 1.0;
                    }

                    double var18 = this.field_4853[var13] / 8000.0;
                    if (var18 < 0.0) {
                        var18 = -var18 * 0.3;
                    }

                    var18 = var18 * 3.0 - 2.0;
                    float var20 = (float)(var14 + par2 - 0) / 1.0F;
                    float var21 = (float)(var15 + par4 - 0) / 1.0F;
                    float var22 = 100.0F - MathHelper.sqrt(var20 * var20 + var21 * var21) * 8.0F;
                    if (var22 > 80.0F) {
                        var22 = 80.0F;
                    }

                    if (var22 < -100.0F) {
                        var22 = -100.0F;
                    }

                    if (var18 > 1.0) {
                        var18 = 1.0;
                    }

                    var18 /= 8.0;
                    var18 = 0.0;
                    if (var16 < 0.0) {
                        var16 = 0.0;
                    }

                    var16 += 0.5;
                    var18 = var18 * (double)par6 / 16.0;
                    ++var13;
                    double var23 = (double)par6 / 2.0;

                    for(int var25 = 0; var25 < par6; ++var25) {
                        double var26 = 0.0;
                        double var28 = ((double)var25 - var23) * 8.0 / var16;
                        if (var28 < 0.0) {
                            var28 *= -1.0;
                        }

                        double var30 = this.field_4850[var12] / 512.0;
                        double var32 = this.field_4851[var12] / 512.0;
                        double var34 = (this.field_4849[var12] / 10.0 + 1.0) / 2.0;
                        if (var34 < 0.0) {
                            var26 = var30;
                        } else if (var34 > 1.0) {
                            var26 = var32;
                        } else {
                            var26 = var30 + (var32 - var30) * var34;
                        }

                        var26 -= 8.0;
                        var26 += (double)var22;
                        byte var36 = 2;
                        if (var25 > par6 / 2 - var36) {
                            double var37 = (double)((float)(var25 - (par6 / 2 - var36)) / 64.0F);
                            if (var37 < 0.0) {
                                var37 = 0.0;
                            }

                            if (var37 > 1.0) {
                                var37 = 1.0;
                            }

                            var26 = var26 * (1.0 - var37) + -3000.0 * var37;
                        }

                        var36 = 8;
                        if (var25 < var36) {
                            double var37 = (double)((float)(var36 - var25) / ((float)var36 - 1.0F));
                            var26 = var26 * (1.0 - var37) + -30.0 * var37;
                        }

                        par1ArrayOfDouble[var12] = var26;
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
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(par1IChunkProvider, this.world, this.world.random, par2, par3, false));
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        Biome var6 = this.world.getBiome(var4 + 16, var5 + 16);
        var6.decorate(this.world, this.world.random, var4, var5);
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(par1IChunkProvider, this.world, this.world.random, par2, par3, false));
        SandBlock.field_318 = false;
    }
}
