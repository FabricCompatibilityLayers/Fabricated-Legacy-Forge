package fr.catcore.fabricatedforge.mixin.forgefml.block;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MushroomPlantBlock.class)
public class MushroomPlantBlockMixin extends FlowerBlock {
    protected MushroomPlantBlockMixin(int i, int j, Material material) {
        super(i, j, material);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean canStayPlaced(World par1World, int par2, int par3, int par4) {
        if (par3 >= 0 && par3 < 256) {
            int var5 = par1World.getBlock(par2, par3 - 1, par4);
            Block soil = Block.BLOCKS[var5];
            return (var5 == Block.MYCELIUM.id || par1World.method_3718(par2, par3, par4) < 13)
                    && soil != null
                    && soil.canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this);
        } else {
            return false;
        }
    }
}
