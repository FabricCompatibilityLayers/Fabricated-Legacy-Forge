package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DeadbushFeature;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(DeadbushFeature.class)
public abstract class DeadbushFeatureMixin extends Feature {

    @Shadow private int field_4881;

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

        for(int var7 = 0; var7 < 4; ++var7) {
            int var8 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            int var9 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
            int var10 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            if (par1World.isAir(var8, var9, var10) && Block.BLOCKS[this.field_4881].canStayPlaced(par1World, var8, var9, var10)) {
                par1World.method_3652(var8, var9, var10, this.field_4881);
            }
        }

        return true;
    }
}
