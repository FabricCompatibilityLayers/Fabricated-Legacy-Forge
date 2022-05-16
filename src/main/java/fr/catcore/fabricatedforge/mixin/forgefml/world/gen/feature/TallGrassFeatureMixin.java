package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TallGrassFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(TallGrassFeature.class)
public abstract class TallGrassFeatureMixin extends Feature {

    @Shadow private int field_4898;

    @Shadow private int field_4899;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        Block block = null;

        do {
            block = Block.BLOCKS[par1World.getBlock(par3, par4, par5)];
            if (block != null && !block.isLeaves(par1World, par3, par4, par5)) {
                break;
            }

            --par4;
        } while(par4 > 0);

        for(int var7 = 0; var7 < 128; ++var7) {
            int var8 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            int var9 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
            int var10 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            if (par1World.isAir(var8, var9, var10) && Block.BLOCKS[this.field_4898].method_450(par1World, var8, var9, var10)) {
                par1World.method_3673(var8, var9, var10, this.field_4898, this.field_4899);
            }
        }

        return true;
    }
}
