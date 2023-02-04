package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.BigTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(BigTreeFeature.class)
public abstract class BigTreeFeatureMixin extends Feature {
    @Shadow
    int maxHeight;

    @Shadow abstract int method_4017(int[] is, int[] js);

    @Shadow
    World world;

    @Shadow
    int[] field_4865;

    @Shadow
    int[][] field_4876;

    @Shadow
    double branchAngle;

    @Shadow
    int leafRadius;

    @Shadow
    Random field_4863;

    @Shadow
    double leafSizeModifer;

    @Shadow abstract float getLeafSizeAtHeight(int height);

    @Shadow
    int height;

    @Shadow
    double leafHeightModifier;

    @Shadow
    double heightModifier;

    /**
     * @author forge
     * @reason PI?
     */
    @Overwrite
    void addBlockCoords() {
        this.height = (int)((double)this.maxHeight * this.heightModifier);
        if (this.height >= this.maxHeight) {
            this.height = this.maxHeight - 1;
        }

        int var1 = (int)(1.382 + Math.pow(this.leafHeightModifier * (double)this.maxHeight / 13.0, 2.0));
        if (var1 < 1) {
            var1 = 1;
        }

        int[][] var2 = new int[var1 * this.maxHeight][4];
        int var3 = this.field_4865[1] + this.maxHeight - this.leafRadius;
        int var4 = 1;
        int var5 = this.field_4865[1] + this.height;
        int var6 = var3 - this.field_4865[1];
        var2[0][0] = this.field_4865[0];
        var2[0][1] = var3;
        var2[0][2] = this.field_4865[2];
        var2[0][3] = var5;
        --var3;

        while(var6 >= 0) {
            int var7 = 0;
            float var8 = this.getLeafSizeAtHeight(var6);
            if (var8 < 0.0F) {
                --var3;
                --var6;
            } else {
                for(double var9 = 0.5; var7 < var1; ++var7) {
                    double var11 = this.leafSizeModifer * (double)var8 * ((double)this.field_4863.nextFloat() + 0.328);
                    double var13 = (double)this.field_4863.nextFloat() * 2.0 * Math.PI;
                    int var15 = MathHelper.floor(var11 * Math.sin(var13) + (double)this.field_4865[0] + var9);
                    int var16 = MathHelper.floor(var11 * Math.cos(var13) + (double)this.field_4865[2] + var9);
                    int[] var17 = new int[]{var15, var3, var16};
                    int[] var18 = new int[]{var15, var3 + this.leafRadius, var16};
                    if (this.method_4017(var17, var18) == -1) {
                        int[] var19 = new int[]{this.field_4865[0], this.field_4865[1], this.field_4865[2]};
                        double var20 = Math.sqrt(
                                Math.pow((double)Math.abs(this.field_4865[0] - var17[0]), 2.0) + Math.pow((double)Math.abs(this.field_4865[2] - var17[2]), 2.0)
                        );
                        double var22 = var20 * this.branchAngle;
                        if ((double)var17[1] - var22 > (double)var5) {
                            var19[1] = var5;
                        } else {
                            var19[1] = (int)((double)var17[1] - var22);
                        }

                        if (this.method_4017(var19, var17) == -1) {
                            var2[var4][0] = var15;
                            var2[var4][1] = var3;
                            var2[var4][2] = var16;
                            var2[var4][3] = var19[1];
                            ++var4;
                        }
                    }
                }

                --var3;
                --var6;
            }
        }

        this.field_4876 = new int[var4][4];
        System.arraycopy(var2, 0, this.field_4876, 0, var4);
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    boolean canGenerate() {
        int[] var1 = new int[]{this.field_4865[0], this.field_4865[1], this.field_4865[2]};
        int[] var2 = new int[]{this.field_4865[0], this.field_4865[1] + this.maxHeight - 1, this.field_4865[2]};
        int var3 = this.world.getBlock(this.field_4865[0], this.field_4865[1] - 1, this.field_4865[2]);
        Block soil = Block.BLOCKS[var3];
        boolean isValidSoil = soil != null
                && soil.canSustainPlant(this.world, this.field_4865[0], this.field_4865[1] - 1, this.field_4865[2], ForgeDirection.UP, (SaplingBlock)Block.SAPLING);
        if (!isValidSoil) {
            return false;
        } else {
            int var4 = this.method_4017(var1, var2);
            if (var4 == -1) {
                return true;
            } else if (var4 < 6) {
                return false;
            } else {
                this.maxHeight = var4;
                return true;
            }
        }
    }
}
