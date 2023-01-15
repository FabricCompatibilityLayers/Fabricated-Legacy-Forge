package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LeverBlock.class)
public abstract class LeverBlockMixin extends Block {
    @Shadow protected abstract boolean method_328(World world, int i, int j, int k);

    public LeverBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_428(World par1World, int par2, int par3, int par4, int par5) {
        ForgeDirection dir = ForgeDirection.getOrientation(par5);
        return dir == ForgeDirection.DOWN && par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN)
                || dir == ForgeDirection.UP && par1World.isBlockSolidOnSide(par2, par3 - 1, par4, ForgeDirection.UP)
                || dir == ForgeDirection.NORTH && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH)
                || dir == ForgeDirection.SOUTH && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH)
                || dir == ForgeDirection.WEST && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST)
                || dir == ForgeDirection.EAST && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST)
                || par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST)
                || par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH)
                || par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH)
                || par1World.isBlockSolidOnSide(par2, par3 - 1, par4, ForgeDirection.UP)
                || par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int method_4185(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
        int var11 = par9 & 8;
        int var10 = par9 & 7;
        var10 = -1;
        if (par5 == 0 && par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN)) {
            var10 = par1World.random.nextBoolean() ? 0 : 7;
        }

        if (par5 == 1 && par1World.isBlockSolidOnSide(par2, par3 - 1, par4, ForgeDirection.UP)) {
            var10 = 5 + par1World.random.nextInt(2);
        }

        if (par5 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH)) {
            var10 = 4;
        }

        if (par5 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH)) {
            var10 = 3;
        }

        if (par5 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST)) {
            var10 = 2;
        }

        if (par5 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST)) {
            var10 = 1;
        }

        return var10 + var11;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void onNeighborUpdate(World par1World, int par2, int par3, int par4, int par5) {
        if (this.method_328(par1World, par2, par3, par4)) {
            int var6 = par1World.getBlockData(par2, par3, par4) & 7;
            boolean var7 = false;
            if (!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST) && var6 == 1) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST) && var6 == 2) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH) && var6 == 3) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH) && var6 == 4) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3 - 1, par4, ForgeDirection.UP) && var6 == 5) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3 - 1, par4, ForgeDirection.UP) && var6 == 6) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN) && var6 == 0) {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN) && var6 == 7) {
                var7 = true;
            }

            if (var7) {
                this.canStayPlaced(par1World, par2, par3, par4, par1World.getBlockData(par2, par3, par4), 0);
                par1World.method_3690(par2, par3, par4, 0);
            }
        }
    }
}
