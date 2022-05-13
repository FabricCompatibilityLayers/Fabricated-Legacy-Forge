package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SnowLayerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin extends Block {
    public SnowLayerBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean method_434(World par1World, int par2, int par3, int par4) {
        int var5 = par1World.getBlock(par2, par3 - 1, par4);
        Block block = Block.BLOCKS[var5];
        return block != null && (block.isLeaves(par1World, par2, par3 - 1, par4) || block.hasTransperancy()) && par1World.getMaterial(par2, par3 - 1, par4).blocksMovement();
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    private boolean method_488(World par1World, int par2, int par3, int par4) {
        if (!this.method_434(par1World, par2, par3, par4)) {
            par1World.method_3690(par2, par3, par4, 0);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_424(World par1World, PlayerEntity par2EntityPlayer, int par3, int par4, int par5, int par6) {
        this.method_445(par1World, par3, par4, par5, par6, 0);
        par2EntityPlayer.incrementStat(Stats.BLOCK_STATS[this.id], 1);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public int getDropCount(Random par1Random) {
        return 1;
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_436(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (par1World.method_3667(LightType.BLOCK, par2, par3, par4) > 11) {
            par1World.method_3690(par2, par3, par4, 0);
        }
    }
}
