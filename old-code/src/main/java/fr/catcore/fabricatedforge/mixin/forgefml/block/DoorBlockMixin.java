package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends Block {

    @Shadow public abstract int method_304(BlockView worldView, int i, int j, int k);

    public DoorBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean onActivated(World par1World, int par2, int par3, int par4, PlayerEntity par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (this.material == Material.IRON) {
            return false;
        } else {
            int var10 = this.method_304(par1World, par2, par3, par4);
            int var11 = var10 & 7;
            var11 ^= 4;
            if ((var10 & 8) == 0) {
                par1World.method_3672(par2, par3, par4, var11);
                par1World.onRenderRegionUpdate(par2, par3, par4, par2, par3, par4);
            } else {
                par1World.method_3672(par2, par3 - 1, par4, var11);
                par1World.onRenderRegionUpdate(par2, par3 - 1, par4, par2, par3, par4);
            }

            par1World.dispatchEvent(par5EntityPlayer, 1003, par2, par3, par4, 0);
            return true;
        }
    }
}
