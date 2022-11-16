package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(OakTreeFeature.class)
public abstract class OakTreeFeatureMixin extends Feature {

    @Shadow protected abstract void method_4032(World world, int i, int j, int k, int l);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        int var6 = par2Random.nextInt(4) + 5;

        while(par1World.getMaterial(par3, par4 - 1, par5) == Material.WATER) {
            --par4;
        }

        boolean var7 = true;
        if (par4 >= 1 && par4 + var6 + 1 <= 128) {
            for(int var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
                byte var9 = 1;
                if (var8 == par4) {
                    var9 = 0;
                }

                if (var8 >= par4 + 1 + var6 - 2) {
                    var9 = 3;
                }

                for(int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
                    for(int var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 128) {
                            int var12 = par1World.getBlock(var10, var8, var11);
                            if (var12 != 0 && Block.BLOCKS[var12] != null && !Block.BLOCKS[var12].isLeaves(par1World, var10, var8, var11)) {
                                if (var12 != Block.WATER.id && var12 != Block.field_334.id) {
                                    var7 = false;
                                } else if (var8 > par4) {
                                    var7 = false;
                                }
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
                int var17 = par1World.getBlock(par3, par4 - 1, par5);
                if ((var17 == Block.GRASS_BLOCK.id || var17 == Block.DIRT.id) && par4 < 128 - var6 - 1) {
                    this.method_4026(par1World, par3, par4 - 1, par5, Block.DIRT.id);

                    for(int var16 = par4 - 3 + var6; var16 <= par4 + var6; ++var16) {
                        int var10 = var16 - (par4 + var6);
                        int var11 = 2 - var10 / 2;

                        for(int var12 = par3 - var11; var12 <= par3 + var11; ++var12) {
                            int var13 = var12 - par3;

                            for(int var14 = par5 - var11; var14 <= par5 + var11; ++var14) {
                                int var15 = var14 - par5;
                                Block block = Block.BLOCKS[par1World.getBlock(var12, var16, var14)];
                                if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || par2Random.nextInt(2) != 0 && var10 != 0)
                                        && (block == null || block.canBeReplacedByLeaves(par1World, var12, var16, var14))) {
                                    this.method_4026(par1World, var12, var16, var14, Block.LEAVES.id);
                                }
                            }
                        }
                    }

                    for(int var27 = 0; var27 < var6; ++var27) {
                        int var10 = par1World.getBlock(par3, par4 + var27, par5);
                        Block block = Block.BLOCKS[var10];
                        if (var10 == 0
                                || block != null && block.isLeaves(par1World, par3, par4 + var27, par5)
                                || var10 == Block.field_334.id
                                || var10 == Block.WATER.id) {
                            this.method_4026(par1World, par3, par4 + var27, par5, Block.LOG.id);
                        }
                    }

                    for(int var28 = par4 - 3 + var6; var28 <= par4 + var6; ++var28) {
                        int var10 = var28 - (par4 + var6);
                        int var11 = 2 - var10 / 2;

                        for(int var12 = par3 - var11; var12 <= par3 + var11; ++var12) {
                            for(int var13 = par5 - var11; var13 <= par5 + var11; ++var13) {
                                Block block = Block.BLOCKS[par1World.getBlock(var12, var28, var13)];
                                if (block != null && block.isLeaves(par1World, var12, var28, var13)) {
                                    if (par2Random.nextInt(4) == 0 && par1World.getBlock(var12 - 1, var28, var13) == 0) {
                                        this.method_4032(par1World, var12 - 1, var28, var13, 8);
                                    }

                                    if (par2Random.nextInt(4) == 0 && par1World.getBlock(var12 + 1, var28, var13) == 0) {
                                        this.method_4032(par1World, var12 + 1, var28, var13, 2);
                                    }

                                    if (par2Random.nextInt(4) == 0 && par1World.getBlock(var12, var28, var13 - 1) == 0) {
                                        this.method_4032(par1World, var12, var28, var13 - 1, 1);
                                    }

                                    if (par2Random.nextInt(4) == 0 && par1World.getBlock(var12, var28, var13 + 1) == 0) {
                                        this.method_4032(par1World, var12, var28, var13 + 1, 4);
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
