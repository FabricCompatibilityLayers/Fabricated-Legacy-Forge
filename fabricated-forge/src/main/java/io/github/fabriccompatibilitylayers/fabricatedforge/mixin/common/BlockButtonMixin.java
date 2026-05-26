package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockButton;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockButton.class)
public abstract class BlockButtonMixin extends Block implements BlockExtension {
    @Shadow protected abstract boolean redundantCanPlaceBlockAt(World par1World, int par2, int par3, int par4);

    public BlockButtonMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(par5);
        return (dir == NORTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) ||
                (dir == SOUTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
                (dir == WEST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) ||
                (dir == EAST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return (((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST)) ||
                (((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) ||
                (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
                (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateBlockMetadata(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8) {
        int var9 = par1World.getBlockMetadata(par2, par3, par4);
        int var10 = var9 & 8;
        var9 &= 7;

        ForgeDirection dir = ForgeDirection.getOrientation(par5);

        if (dir == NORTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            var9 = 4;
        }
        else if (dir == SOUTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            var9 = 3;
        }
        else if (dir == WEST && ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            var9 = 2;
        }
        else if (dir == EAST && ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            var9 = 1;
        } else {
            var9 = this.getOrientation(par1World, par2, par3, par4);
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, var9 + var10);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private int getOrientation(World par1World, int par2, int par3, int par4)
    {
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST)) return 1;
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) return 2;
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) return 3;
        if (((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) return 4;
        return 1;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (this.redundantCanPlaceBlockAt(par1World, par2, par3, par4)) {
            int var6 = par1World.getBlockMetadata(par2, par3, par4) & 7;
            boolean var7 = false;
            if (!((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST) && var6 == 1)
            {
                var7 = true;
            }

            if (!((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST) && var6 == 2)
            {
                var7 = true;
            }

            if (!((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) && var6 == 3)
            {
                var7 = true;
            }

            if (!((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH) && var6 == 4)
            {
                var7 = true;
            }

            if (var7) {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
        }
    }
}
