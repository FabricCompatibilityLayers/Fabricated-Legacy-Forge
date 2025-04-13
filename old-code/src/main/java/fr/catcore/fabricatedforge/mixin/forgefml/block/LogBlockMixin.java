package fr.catcore.fabricatedforge.mixin.forgefml.block;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LogBlock.class)
public class LogBlockMixin extends Block {
    public LogBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_411(World par1World, int par2, int par3, int par4, int par5, int par6) {
        byte var7 = 4;
        int var8 = var7 + 1;
        if (par1World.isRegionLoaded(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
            for(int var9 = -var7; var9 <= var7; ++var9) {
                for(int var10 = -var7; var10 <= var7; ++var10) {
                    for(int var11 = -var7; var11 <= var7; ++var11) {
                        int var12 = par1World.getBlock(par2 + var9, par3 + var10, par4 + var11);
                        if (Block.BLOCKS[var12] != null) {
                            ((IBlock)Block.BLOCKS[var12]).beginLeavesDecay(par1World, par2 + var9, par3 + var10, par4 + var11);
                        }
                    }
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int method_396(int par1, int par2) {
        int var3 = par2 & 12;
        int var4 = par2 & 3;
        return var3 != 0 || par1 != 1 && par1 != 0 ? (var3 == 4 && (par1 == 5 || par1 == 4) ? 21 : (var3 == 8 && (par1 == 2 || par1 == 3) ? 21 : (var4 == 1 ? 116 : (var4 == 2 ? 117 : (var4 == 3 ? 153 : 20))))) : 21;
    }

    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isWood(World world, int x, int y, int z) {
        return true;
    }
}
