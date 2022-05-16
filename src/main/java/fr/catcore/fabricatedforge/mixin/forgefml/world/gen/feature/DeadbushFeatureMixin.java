package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

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
    public boolean method_4028(World world, Random random, int i, int j, int k) {
        int var11;
        for(boolean var6 = false; ((var11 = world.getBlock(i, j, k)) == 0 || var11 == Block.LEAVES.id) && j > 0; --j) {
        }

        for(int var7 = 0; var7 < 4; ++var7) {
            int var8 = i + random.nextInt(8) - random.nextInt(8);
            int var9 = j + random.nextInt(4) - random.nextInt(4);
            int var10 = k + random.nextInt(8) - random.nextInt(8);
            if (world.isAir(var8, var9, var10) && Block.BLOCKS[this.field_4881].method_450(world, var8, var9, var10)) {
                world.method_3652(var8, var9, var10, this.field_4881);
            }
        }

        return true;
    }
}
