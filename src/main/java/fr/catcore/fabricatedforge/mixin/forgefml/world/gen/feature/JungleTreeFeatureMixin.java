package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.Axis;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.JungleTreeFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(JungleTreeFeature.class)
public abstract class JungleTreeFeatureMixin extends Feature {

    @Shadow @Final private int field_4900;

    @Shadow @Final private int field_4903;

    @Shadow @Final private int field_4902;

    @Shadow @Final private boolean field_4901;

    @Shadow protected abstract void method_4033(World world, int i, int j, int k, int l);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        int var6 = par2Random.nextInt(3) + this.field_4900;
        boolean var7 = true;
        if (par4 >= 1 && par4 + var6 + 1 <= 256) {
            int var8;
            byte var9;
            int var11;
            int var12;
            for(var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
                var9 = 1;
                if (var8 == par4) {
                    var9 = 0;
                }

                if (var8 >= par4 + 1 + var6 - 2) {
                    var9 = 2;
                }

                for(int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
                    for(var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 256) {
                            var12 = par1World.getBlock(var10, var8, var11);
                            Block block = Block.BLOCKS[var12];
                            if (var12 != 0 && !((IBlock)block).isLeaves(par1World, var10, var8, var11) && var12 != Block.GRASS_BLOCK.id && var12 != Block.DIRT.id && !((IBlock)block).isWood(par1World, var10, var8, var11)) {
                                var7 = false;
                            }
                        } else {
                            var7 = false;
                        }
                    }
                }
            }

            if (!var7) {
                return false;
            } else {
                var8 = par1World.getBlock(par3, par4 - 1, par5);
                if ((var8 == Block.GRASS_BLOCK.id || var8 == Block.DIRT.id) && par4 < 256 - var6 - 1) {
                    this.method_4026(par1World, par3, par4 - 1, par5, Block.DIRT.id);
                    var9 = 3;
                    byte var18 = 0;

                    int var14;
                    int var15;
                    int var13;
                    for(var11 = par4 - var9 + var6; var11 <= par4 + var6; ++var11) {
                        var12 = var11 - (par4 + var6);
                        var13 = var18 + 1 - var12 / 2;

                        for(var14 = par3 - var13; var14 <= par3 + var13; ++var14) {
                            var15 = var14 - par3;

                            for(int var16 = par5 - var13; var16 <= par5 + var13; ++var16) {
                                int var17 = var16 - par5;
                                Block block = Block.BLOCKS[par1World.getBlock(var14, var11, var16)];
                                if ((Math.abs(var15) != var13 || Math.abs(var17) != var13 || par2Random.nextInt(2) != 0 && var12 != 0) && (block == null || ((IBlock)block).canBeReplacedByLeaves(par1World, var14, var11, var16))) {
                                    this.method_4027(par1World, var14, var11, var16, Block.LEAVES.id, this.field_4903);
                                }
                            }
                        }
                    }

                    Block block;
                    for(var11 = 0; var11 < var6; ++var11) {
                        var12 = par1World.getBlock(par3, par4 + var11, par5);
                        block = Block.BLOCKS[var12];
                        if (var12 == 0 || block == null || ((IBlock)block).isLeaves(par1World, par3, par4 + var11, par5)) {
                            this.method_4027(par1World, par3, par4 + var11, par5, Block.LOG.id, this.field_4902);
                            if (this.field_4901 && var11 > 0) {
                                if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 - 1, par4 + var11, par5)) {
                                    this.method_4027(par1World, par3 - 1, par4 + var11, par5, Block.VINE.id, 8);
                                }

                                if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 + 1, par4 + var11, par5)) {
                                    this.method_4027(par1World, par3 + 1, par4 + var11, par5, Block.VINE.id, 2);
                                }

                                if (par2Random.nextInt(3) > 0 && par1World.isAir(par3, par4 + var11, par5 - 1)) {
                                    this.method_4027(par1World, par3, par4 + var11, par5 - 1, Block.VINE.id, 1);
                                }

                                if (par2Random.nextInt(3) > 0 && par1World.isAir(par3, par4 + var11, par5 + 1)) {
                                    this.method_4027(par1World, par3, par4 + var11, par5 + 1, Block.VINE.id, 4);
                                }
                            }
                        }
                    }

                    if (this.field_4901) {
                        for(var11 = par4 - 3 + var6; var11 <= par4 + var6; ++var11) {
                            var12 = var11 - (par4 + var6);
                            var13 = 2 - var12 / 2;

                            for(var14 = par3 - var13; var14 <= par3 + var13; ++var14) {
                                for(var15 = par5 - var13; var15 <= par5 + var13; ++var15) {
                                    block = Block.BLOCKS[par1World.getBlock(var14, var11, var15)];
                                    if (block != null && ((IBlock)block).isLeaves(par1World, var14, var11, var15)) {
                                        if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14 - 1, var11, var15) == 0) {
                                            this.method_4033(par1World, var14 - 1, var11, var15, 8);
                                        }

                                        if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14 + 1, var11, var15) == 0) {
                                            this.method_4033(par1World, var14 + 1, var11, var15, 2);
                                        }

                                        if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14, var11, var15 - 1) == 0) {
                                            this.method_4033(par1World, var14, var11, var15 - 1, 1);
                                        }

                                        if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14, var11, var15 + 1) == 0) {
                                            this.method_4033(par1World, var14, var11, var15 + 1, 4);
                                        }
                                    }
                                }
                            }
                        }

                        if (par2Random.nextInt(5) == 0 && var6 > 5) {
                            for(var11 = 0; var11 < 2; ++var11) {
                                for(var12 = 0; var12 < 4; ++var12) {
                                    if (par2Random.nextInt(4 - var11) == 0) {
                                        var13 = par2Random.nextInt(3);
                                        this.method_4027(par1World, par3 + Axis.OFFSET_X[Axis.OPPOSITE[var12]], par4 + var6 - 5 + var11, par5 + Axis.OFFSET_Z[Axis.OPPOSITE[var12]], Block.COCOA.id, var13 << 2 | var12);
                                    }
                                }
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
