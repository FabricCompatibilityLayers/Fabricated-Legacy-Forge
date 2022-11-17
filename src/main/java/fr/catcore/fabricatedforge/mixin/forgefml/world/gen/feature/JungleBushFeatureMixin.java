package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.JungleBushFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(JungleBushFeature.class)
public abstract class JungleBushFeatureMixin extends Feature {

    @Shadow private int field_4885;

    @Shadow private int field_4884;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        Block block = null;

        do {
            block = Block.BLOCKS[par1World.getBlock(par3, par4, par5)];
        } while((block == null || ((IBlock)block).isLeaves(par1World, par3, par4, par5)) && --par4 > 0);

        int var7 = par1World.getBlock(par3, par4, par5);
        if (var7 == Block.DIRT.id || var7 == Block.GRASS_BLOCK.id) {
            this.method_4027(par1World, par3, ++par4, par5, Block.LOG.id, this.field_4885);

            for(int var8 = par4; var8 <= par4 + 2; ++var8) {
                int var9 = var8 - par4;
                int var10 = 2 - var9;

                for(int var11 = par3 - var10; var11 <= par3 + var10; ++var11) {
                    int var12 = var11 - par3;

                    for(int var13 = par5 - var10; var13 <= par5 + var10; ++var13) {
                        int var14 = var13 - par5;
                        block = Block.BLOCKS[par1World.getBlock(var11, var8, var13)];
                        if ((Math.abs(var12) != var10 || Math.abs(var14) != var10 || par2Random.nextInt(2) != 0)
                                && (block == null || ((IBlock)block).canBeReplacedByLeaves(par1World, var11, var8, var13))) {
                            this.method_4027(par1World, var11, var8, var13, Block.LEAVES.id, this.field_4884);
                        }
                    }
                }
            }
        }

        return true;
    }
}
