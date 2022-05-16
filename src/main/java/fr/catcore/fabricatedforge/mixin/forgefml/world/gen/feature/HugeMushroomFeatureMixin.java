package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(HugeMushroomFeature.class)
public abstract class HugeMushroomFeatureMixin extends Feature {

    @Shadow private int field_4887;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        int var6 = par2Random.nextInt(2);
        if (this.field_4887 >= 0) {
            var6 = this.field_4887;
        }

        int var7 = par2Random.nextInt(3) + 4;
        boolean var8 = true;
        if (par4 >= 1 && par4 + var7 + 1 < 256) {
            int var9;
            int var11;
            int var12;
            int var13;
            int var16;
            Block block;
            for(var9 = par4; var9 <= par4 + 1 + var7; ++var9) {
                var16 = 3;
                if (var9 <= par4 + 3) {
                    var16 = 0;
                }

                for(var11 = par3 - var16; var11 <= par3 + var16 && var8; ++var11) {
                    for(var12 = par5 - var16; var12 <= par5 + var16 && var8; ++var12) {
                        if (var9 >= 0 && var9 < 256) {
                            var13 = par1World.getBlock(var11, var9, var12);
                            block = Block.BLOCKS[var13];
                            if (var13 != 0 && block != null && !block.isLeaves(par1World, var11, var9, var12)) {
                                var8 = false;
                            }
                        } else {
                            var8 = false;
                        }
                    }
                }
            }

            if (!var8) {
                return false;
            } else {
                var9 = par1World.getBlock(par3, par4 - 1, par5);
                if (var9 != Block.DIRT.id && var9 != Block.GRASS_BLOCK.id && var9 != Block.MYCELIUM.id) {
                    return false;
                } else {
                    var16 = par4 + var7;
                    if (var6 == 1) {
                        var16 = par4 + var7 - 3;
                    }

                    for(var11 = var16; var11 <= par4 + var7; ++var11) {
                        var12 = 1;
                        if (var11 < par4 + var7) {
                            ++var12;
                        }

                        if (var6 == 0) {
                            var12 = 3;
                        }

                        for(var13 = par3 - var12; var13 <= par3 + var12; ++var13) {
                            for(int var14 = par5 - var12; var14 <= par5 + var12; ++var14) {
                                int var15 = 5;
                                if (var13 == par3 - var12) {
                                    --var15;
                                }

                                if (var13 == par3 + var12) {
                                    ++var15;
                                }

                                if (var14 == par5 - var12) {
                                    var15 -= 3;
                                }

                                if (var14 == par5 + var12) {
                                    var15 += 3;
                                }

                                if (var6 == 0 || var11 < par4 + var7) {
                                    if ((var13 == par3 - var12 || var13 == par3 + var12) && (var14 == par5 - var12 || var14 == par5 + var12)) {
                                        continue;
                                    }

                                    if (var13 == par3 - (var12 - 1) && var14 == par5 - var12) {
                                        var15 = 1;
                                    }

                                    if (var13 == par3 - var12 && var14 == par5 - (var12 - 1)) {
                                        var15 = 1;
                                    }

                                    if (var13 == par3 + (var12 - 1) && var14 == par5 - var12) {
                                        var15 = 3;
                                    }

                                    if (var13 == par3 + var12 && var14 == par5 - (var12 - 1)) {
                                        var15 = 3;
                                    }

                                    if (var13 == par3 - (var12 - 1) && var14 == par5 + var12) {
                                        var15 = 7;
                                    }

                                    if (var13 == par3 - var12 && var14 == par5 + (var12 - 1)) {
                                        var15 = 7;
                                    }

                                    if (var13 == par3 + (var12 - 1) && var14 == par5 + var12) {
                                        var15 = 9;
                                    }

                                    if (var13 == par3 + var12 && var14 == par5 + (var12 - 1)) {
                                        var15 = 9;
                                    }
                                }

                                if (var15 == 5 && var11 < par4 + var7) {
                                    var15 = 0;
                                }

                                block = Block.BLOCKS[par1World.getBlock(var13, var11, var14)];
                                if ((var15 != 0 || par4 >= par4 + var7 - 1) && (block == null || block.canBeReplacedByLeaves(par1World, var13, var11, var14))) {
                                    this.method_4027(par1World, var13, var11, var14, Block.BROWN_MUSHROOM_BLOCK.id + var6, var15);
                                }
                            }
                        }
                    }

                    for(var11 = 0; var11 < var7; ++var11) {
                        var12 = par1World.getBlock(par3, par4 + var11, par5);
                        block = Block.BLOCKS[var12];
                        if (block == null || block.canBeReplacedByLeaves(par1World, par3, par4 + var11, par5)) {
                            this.method_4027(par1World, par3, par4 + var11, par5, Block.BROWN_MUSHROOM_BLOCK.id + var6, 10);
                        }
                    }

                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
