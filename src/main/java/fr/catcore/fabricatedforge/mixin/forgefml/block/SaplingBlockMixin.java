package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.FlowerBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin extends FlowerBlock {
    @Shadow public abstract boolean method_383(World world, int i, int j, int k, int l);

    protected SaplingBlockMixin(int i, int j, Material material) {
        super(i, j, material);
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void method_382(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (TerrainGen.saplingGrowTree(par1World, par5Random, par2, par3, par4)) {
            int var6 = par1World.getBlockData(par2, par3, par4) & 3;
            Object var7 = null;
            int var8 = 0;
            int var9 = 0;
            boolean var10 = false;
            if (var6 == 1) {
                var7 = new SpruceTreeFeature(true);
            } else if (var6 == 2) {
                var7 = new BirchTreeFeature(true);
            } else if (var6 == 3) {
                for(var8 = 0; var8 >= -1; --var8) {
                    for(var9 = 0; var9 >= -1; --var9) {
                        if (this.method_383(par1World, par2 + var8, par3, par4 + var9, 3)
                                && this.method_383(par1World, par2 + var8 + 1, par3, par4 + var9, 3)
                                && this.method_383(par1World, par2 + var8, par3, par4 + var9 + 1, 3)
                                && this.method_383(par1World, par2 + var8 + 1, par3, par4 + var9 + 1, 3)) {
                            var7 = new AbstractGiantTreeFeature(true, 10 + par5Random.nextInt(20), 3, 3);
                            var10 = true;
                            break;
                        }
                    }

                    if (var7 != null) {
                        break;
                    }
                }

                if (var7 == null) {
                    var9 = 0;
                    var8 = 0;
                    var7 = new JungleTreeFeature(true, 4 + par5Random.nextInt(7), 3, 3, false);
                }
            } else {
                var7 = new JungleTreeFeature(true);
                if (par5Random.nextInt(10) == 0) {
                    var7 = new BigTreeFeature(true);
                }
            }

            if (var10) {
                par1World.method_3652(par2 + var8, par3, par4 + var9, 0);
                par1World.method_3652(par2 + var8 + 1, par3, par4 + var9, 0);
                par1World.method_3652(par2 + var8, par3, par4 + var9 + 1, 0);
                par1World.method_3652(par2 + var8 + 1, par3, par4 + var9 + 1, 0);
            } else {
                par1World.method_3652(par2, par3, par4, 0);
            }

            if (!((Feature)var7).method_4028(par1World, par5Random, par2 + var8, par3, par4 + var9)) {
                if (var10) {
                    par1World.method_3673(par2 + var8, par3, par4 + var9, this.id, var6);
                    par1World.method_3673(par2 + var8 + 1, par3, par4 + var9, this.id, var6);
                    par1World.method_3673(par2 + var8, par3, par4 + var9 + 1, this.id, var6);
                    par1World.method_3673(par2 + var8 + 1, par3, par4 + var9 + 1, this.id, var6);
                } else {
                    par1World.method_3673(par2, par3, par4, this.id, var6);
                }
            }
        }
    }
}
