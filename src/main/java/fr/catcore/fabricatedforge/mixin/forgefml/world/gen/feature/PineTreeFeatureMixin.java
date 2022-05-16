package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PineTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(PineTreeFeature.class)
public abstract class PineTreeFeatureMixin extends Feature {

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        int var6 = par2Random.nextInt(5) + 7;
        int var7 = var6 - par2Random.nextInt(2) - 3;
        int var8 = var6 - var7;
        int var9 = 1 + par2Random.nextInt(var8 + 1);
        boolean var10 = true;
        if (par4 >= 1 && par4 + var6 + 1 <= 128) {
            int var11;
            int var13;
            int var14;
            int var15;
            int var18;
            for(var11 = par4; var11 <= par4 + 1 + var6 && var10; ++var11) {
                boolean var12 = true;
                if (var11 - par4 < var7) {
                    var18 = 0;
                } else {
                    var18 = var9;
                }

                for(var13 = par3 - var18; var13 <= par3 + var18 && var10; ++var13) {
                    for(var14 = par5 - var18; var14 <= par5 + var18 && var10; ++var14) {
                        if (var11 >= 0 && var11 < 128) {
                            var15 = par1World.getBlock(var13, var11, var14);
                            Block block = Block.BLOCKS[var15];
                            if (var15 != 0 && (block == null || !((IBlock)block).isLeaves(par1World, var13, var11, var14))) {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    }
                }
            }

            if (!var10) {
                return false;
            } else {
                var11 = par1World.getBlock(par3, par4 - 1, par5);
                if ((var11 == Block.GRASS_BLOCK.id || var11 == Block.DIRT.id) && par4 < 128 - var6 - 1) {
                    this.method_4026(par1World, par3, par4 - 1, par5, Block.DIRT.id);
                    var18 = 0;

                    for(var13 = par4 + var6; var13 >= par4 + var7; --var13) {
                        for(var14 = par3 - var18; var14 <= par3 + var18; ++var14) {
                            var15 = var14 - par3;

                            for(int var16 = par5 - var18; var16 <= par5 + var18; ++var16) {
                                int var17 = var16 - par5;
                                Block block = Block.BLOCKS[par1World.getBlock(var14, var13, var16)];
                                if ((Math.abs(var15) != var18 || Math.abs(var17) != var18 || var18 <= 0) && (block == null || ((IBlock)block).canBeReplacedByLeaves(par1World, var14, var13, var16))) {
                                    this.method_4027(par1World, var14, var13, var16, Block.LEAVES.id, 1);
                                }
                            }
                        }

                        if (var18 >= 1 && var13 == par4 + var7 + 1) {
                            --var18;
                        } else if (var18 < var9) {
                            ++var18;
                        }
                    }

                    for(var13 = 0; var13 < var6 - 1; ++var13) {
                        var14 = par1World.getBlock(par3, par4 + var13, par5);
                        Block block = Block.BLOCKS[var14];
                        if (var14 == 0 || block == null || ((IBlock)block).isLeaves(par1World, par3, par4 + var13, par5)) {
                            this.method_4027(par1World, par3, par4 + var13, par5, Block.LOG.id, 1);
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
