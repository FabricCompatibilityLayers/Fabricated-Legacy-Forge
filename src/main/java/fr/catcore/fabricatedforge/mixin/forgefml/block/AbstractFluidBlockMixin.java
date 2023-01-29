package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractFluidBlock.class)
public abstract class AbstractFluidBlockMixin extends Block {
    public AbstractFluidBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author forge
     * @reason waterColor
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public int getTint(BlockView par1IBlockAccess, int par2, int par3, int par4) {
        if (this.material != Material.WATER) {
            return 16777215;
        } else {
            int var5 = 0;
            int var6 = 0;
            int var7 = 0;

            for(int var8 = -1; var8 <= 1; ++var8) {
                for(int var9 = -1; var9 <= 1; ++var9) {
                    int var10 = par1IBlockAccess.getBiome(par2 + var9, par4 + var8).getWaterColorMultiplier();
                    var5 += (var10 & 0xFF0000) >> 16;
                    var6 += (var10 & 0xFF00) >> 8;
                    var7 += var10 & 0xFF;
                }
            }

            return (var5 / 9 & 0xFF) << 16 | (var6 / 9 & 0xFF) << 8 | var7 / 9 & 0xFF;
        }
    }
}
