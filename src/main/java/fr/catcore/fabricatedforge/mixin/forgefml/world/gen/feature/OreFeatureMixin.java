package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IOreFeature;
import fr.catcore.modremapperapi.api.mixin.NewConstructor;
import fr.catcore.modremapperapi.api.mixin.ShadowConstructor;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin extends Feature implements IOreFeature {

    @Shadow private int amount;
    @Shadow private int field_4892;

    private int minableBlockMeta = 0;

    @Override
    public void setMinableBlockMeta(int minableBlockMeta) {
        this.minableBlockMeta = minableBlockMeta;
    }

    @ShadowConstructor
    public abstract void vanilla$ctr(int id, int number);

    @NewConstructor
    public void forge$ctr(int id, int meta, int number) {
        vanilla$ctr(id, number);
        this.minableBlockMeta = meta;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        float var6 = par2Random.nextFloat() * (float) Math.PI;
        double var7 = (double)((float)(par3 + 8) + MathHelper.sin(var6) * (float)this.amount / 8.0F);
        double var9 = (double)((float)(par3 + 8) - MathHelper.sin(var6) * (float)this.amount / 8.0F);
        double var11 = (double)((float)(par5 + 8) + MathHelper.cos(var6) * (float)this.amount / 8.0F);
        double var13 = (double)((float)(par5 + 8) - MathHelper.cos(var6) * (float)this.amount / 8.0F);
        double var15 = (double)(par4 + par2Random.nextInt(3) - 2);
        double var17 = (double)(par4 + par2Random.nextInt(3) - 2);

        for(int var19 = 0; var19 <= this.amount; ++var19) {
            double var20 = var7 + (var9 - var7) * (double)var19 / (double)this.amount;
            double var22 = var15 + (var17 - var15) * (double)var19 / (double)this.amount;
            double var24 = var11 + (var13 - var11) * (double)var19 / (double)this.amount;
            double var26 = par2Random.nextDouble() * (double)this.amount / 16.0;
            double var28 = (double)(MathHelper.sin((float)var19 * (float) Math.PI / (float)this.amount) + 1.0F) * var26 + 1.0;
            double var30 = (double)(MathHelper.sin((float)var19 * (float) Math.PI / (float)this.amount) + 1.0F) * var26 + 1.0;
            int var32 = MathHelper.floor(var20 - var28 / 2.0);
            int var33 = MathHelper.floor(var22 - var30 / 2.0);
            int var34 = MathHelper.floor(var24 - var28 / 2.0);
            int var35 = MathHelper.floor(var20 + var28 / 2.0);
            int var36 = MathHelper.floor(var22 + var30 / 2.0);
            int var37 = MathHelper.floor(var24 + var28 / 2.0);

            for(int var38 = var32; var38 <= var35; ++var38) {
                double var39 = ((double)var38 + 0.5 - var20) / (var28 / 2.0);
                if (var39 * var39 < 1.0) {
                    for(int var41 = var33; var41 <= var36; ++var41) {
                        double var42 = ((double)var41 + 0.5 - var22) / (var30 / 2.0);
                        if (var39 * var39 + var42 * var42 < 1.0) {
                            for(int var44 = var34; var44 <= var37; ++var44) {
                                double var45 = ((double)var44 + 0.5 - var24) / (var28 / 2.0);
                                Block block = Block.BLOCKS[par1World.getBlock(var38, var41, var44)];
                                if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0
                                        && block != null
                                        && ((IBlock)block).isGenMineableReplaceable(par1World, var38, var41, var44)) {
                                    par1World.method_3673(var38, var41, var44, this.field_4892, this.minableBlockMeta);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
