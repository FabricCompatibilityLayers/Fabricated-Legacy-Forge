package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractGiantTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(AbstractGiantTreeFeature.class)
public abstract class AbstractGiantTreeFeatureMixin extends Feature {

    @Shadow @Final private int baseHeight;

    @Shadow @Final private int field_4890;

    @Shadow @Final private int field_4891;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        int var6 = par2Random.nextInt(3) + this.baseHeight;
        boolean var7 = true;
        if (par4 >= 1 && par4 + var6 + 1 <= 256) {
            for(int var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
                byte var9 = 2;
                if (var8 == par4) {
                    var9 = 1;
                }

                if (var8 >= par4 + 1 + var6 - 2) {
                    var9 = 2;
                }

                for(int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
                    for(int var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 256) {
                            int var12 = par1World.getBlock(var10, var8, var11);
                            if (var12 != 0
                                    && Block.BLOCKS[var12] != null
                                    && !((IBlock)Block.BLOCKS[var12]).isLeaves(par1World, var10, var8, var11)
                                    && var12 != Block.GRASS_BLOCK.id
                                    && var12 != Block.DIRT.id
                                    && Block.BLOCKS[var12] != null
                                    && !((IBlock)Block.BLOCKS[var12]).isWood(par1World, var10, var8, var11)
                                    && var12 != Block.SAPLING.id) {
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
                int var151 = par1World.getBlock(par3, par4 - 1, par5);
                if ((var151 == Block.GRASS_BLOCK.id || var151 == Block.DIRT.id) && par4 < 256 - var6 - 1) {
                    par1World.method_3652(par3, par4 - 1, par5, Block.DIRT.id);
                    par1World.method_3652(par3 + 1, par4 - 1, par5, Block.DIRT.id);
                    par1World.method_3652(par3, par4 - 1, par5 + 1, Block.DIRT.id);
                    par1World.method_3652(par3 + 1, par4 - 1, par5 + 1, Block.DIRT.id);
                    this.method_4029(par1World, par3, par5, par4 + var6, 2, par2Random);

                    for(int var14 = par4 + var6 - 2 - par2Random.nextInt(4); var14 > par4 + var6 / 2; var14 -= 2 + par2Random.nextInt(4)) {
                        float var15x = par2Random.nextFloat() * (float) Math.PI * 2.0F;
                        int var11 = par3 + (int)(0.5F + MathHelper.cos(var15x) * 4.0F);
                        int var12 = par5 + (int)(0.5F + MathHelper.sin(var15x) * 4.0F);
                        this.method_4029(par1World, var11, var12, var14, 0, par2Random);

                        for(int var13 = 0; var13 < 5; ++var13) {
                            var11 = par3 + (int)(1.5F + MathHelper.cos(var15x) * (float)var13);
                            var12 = par5 + (int)(1.5F + MathHelper.sin(var15x) * (float)var13);
                            this.method_4027(par1World, var11, var14 - 3 + var13 / 2, var12, Block.LOG.id, this.field_4890);
                        }
                    }

                    for(int var10 = 0; var10 < var6; ++var10) {
                        int var11 = par1World.getBlock(par3, par4 + var10, par5);
                        if (var11 == 0 || Block.BLOCKS[var11] == null || ((IBlock)Block.BLOCKS[var11]).isLeaves(par1World, par3, par4 + var10, par5)) {
                            this.method_4027(par1World, par3, par4 + var10, par5, Block.LOG.id, this.field_4890);
                            if (var10 > 0) {
                                if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 - 1, par4 + var10, par5)) {
                                    this.method_4027(par1World, par3 - 1, par4 + var10, par5, Block.VINE.id, 8);
                                }

                                if (par2Random.nextInt(3) > 0 && par1World.isAir(par3, par4 + var10, par5 - 1)) {
                                    this.method_4027(par1World, par3, par4 + var10, par5 - 1, Block.VINE.id, 1);
                                }
                            }
                        }

                        if (var10 < var6 - 1) {
                            var11 = par1World.getBlock(par3 + 1, par4 + var10, par5);
                            if (var11 == 0 || Block.BLOCKS[var11] == null || ((IBlock)Block.BLOCKS[var11]).isLeaves(par1World, par3 + 1, par4 + var10, par5)) {
                                this.method_4027(par1World, par3 + 1, par4 + var10, par5, Block.LOG.id, this.field_4890);
                                if (var10 > 0) {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 + 2, par4 + var10, par5)) {
                                        this.method_4027(par1World, par3 + 2, par4 + var10, par5, Block.VINE.id, 2);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 + 1, par4 + var10, par5 - 1)) {
                                        this.method_4027(par1World, par3 + 1, par4 + var10, par5 - 1, Block.VINE.id, 1);
                                    }
                                }
                            }

                            var11 = par1World.getBlock(par3 + 1, par4 + var10, par5 + 1);
                            if (var11 == 0 || Block.BLOCKS[var11] == null || ((IBlock)Block.BLOCKS[var11]).isLeaves(par1World, par3 + 1, par4 + var10, par5 + 1)) {
                                this.method_4027(par1World, par3 + 1, par4 + var10, par5 + 1, Block.LOG.id, this.field_4890);
                                if (var10 > 0) {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 + 2, par4 + var10, par5 + 1)) {
                                        this.method_4027(par1World, par3 + 2, par4 + var10, par5 + 1, Block.VINE.id, 2);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 + 1, par4 + var10, par5 + 2)) {
                                        this.method_4027(par1World, par3 + 1, par4 + var10, par5 + 2, Block.VINE.id, 4);
                                    }
                                }
                            }

                            var11 = par1World.getBlock(par3, par4 + var10, par5 + 1);
                            if (var11 == 0 || Block.BLOCKS[var11] == null || ((IBlock)Block.BLOCKS[var11]).isLeaves(par1World, par3, par4 + var10, par5 + 1)) {
                                this.method_4027(par1World, par3, par4 + var10, par5 + 1, Block.LOG.id, this.field_4890);
                                if (var10 > 0) {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAir(par3 - 1, par4 + var10, par5 + 1)) {
                                        this.method_4027(par1World, par3 - 1, par4 + var10, par5 + 1, Block.VINE.id, 8);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAir(par3, par4 + var10, par5 + 2)) {
                                        this.method_4027(par1World, par3, par4 + var10, par5 + 2, Block.VINE.id, 4);
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

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private void method_4029(World par1World, int par2, int par3, int par4, int par5, Random par6Random) {
        byte var7 = 2;

        for(int var8 = par4 - var7; var8 <= par4; ++var8) {
            int var9 = var8 - par4;
            int var10 = par5 + 1 - var9;

            for(int var11 = par2 - var10; var11 <= par2 + var10 + 1; ++var11) {
                int var12 = var11 - par2;

                for(int var13 = par3 - var10; var13 <= par3 + var10 + 1; ++var13) {
                    int var14 = var13 - par3;
                    Block block = Block.BLOCKS[par1World.getBlock(var11, var8, var13)];
                    if ((var12 >= 0 || var14 >= 0 || var12 * var12 + var14 * var14 <= var10 * var10)
                            && (var12 <= 0 && var14 <= 0 || var12 * var12 + var14 * var14 <= (var10 + 1) * (var10 + 1))
                            && (par6Random.nextInt(4) != 0 || var12 * var12 + var14 * var14 <= (var10 - 1) * (var10 - 1))
                            && (block == null || ((IBlock)block).canBeReplacedByLeaves(par1World, var11, var8, var13))) {
                        this.method_4027(par1World, var11, var8, var13, Block.LEAVES.id, this.field_4891);
                    }
                }
            }
        }
    }
}
