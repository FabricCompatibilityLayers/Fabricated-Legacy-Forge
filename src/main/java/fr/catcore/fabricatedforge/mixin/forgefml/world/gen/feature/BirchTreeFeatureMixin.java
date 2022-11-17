package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.BirchTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(BirchTreeFeature.class)
public abstract class BirchTreeFeatureMixin extends Feature {

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        int var6 = par2Random.nextInt(3) + 5;
        boolean var7 = true;
        if (par4 >= 1 && par4 + var6 + 1 <= 256) {
            for(int var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
                byte var9 = 1;
                if (var8 == par4) {
                    var9 = 0;
                }

                if (var8 >= par4 + 1 + var6 - 2) {
                    var9 = 2;
                }

                for(int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
                    for(int var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 256) {
                            int var12 = par1World.getBlock(var10, var8, var11);
                            Block block = Block.BLOCKS[var12];
                            if (var12 != 0 && block != null && !((IBlock)block).isLeaves(par1World, var10, var8, var11)) {
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
                int var17 = par1World.getBlock(par3, par4 - 1, par5);
                if ((var17 == Block.GRASS_BLOCK.id || var17 == Block.DIRT.id) && par4 < 256 - var6 - 1) {
                    this.method_4026(par1World, par3, par4 - 1, par5, Block.DIRT.id);

                    for(int var16 = par4 - 3 + var6; var16 <= par4 + var6; ++var16) {
                        int var10 = var16 - (par4 + var6);
                        int var11 = 1 - var10 / 2;

                        for(int var12 = par3 - var11; var12 <= par3 + var11; ++var12) {
                            int var13 = var12 - par3;

                            for(int var14 = par5 - var11; var14 <= par5 + var11; ++var14) {
                                int var15 = var14 - par5;
                                Block block = Block.BLOCKS[par1World.getBlock(var12, var16, var14)];
                                if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || par2Random.nextInt(2) != 0 && var10 != 0)
                                        && (block == null || ((IBlock)block).canBeReplacedByLeaves(par1World, var12, var16, var14))) {
                                    this.method_4027(par1World, var12, var16, var14, Block.LEAVES.id, 2);
                                }
                            }
                        }
                    }

                    for(int var23 = 0; var23 < var6; ++var23) {
                        int var10 = par1World.getBlock(par3, par4 + var23, par5);
                        Block block = Block.BLOCKS[var10];
                        if (var10 == 0 || block == null || ((IBlock)block).isLeaves(par1World, par3, par4 + var23, par5)) {
                            this.method_4027(par1World, par3, par4 + var23, par5, Block.LOG.id, 2);
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
