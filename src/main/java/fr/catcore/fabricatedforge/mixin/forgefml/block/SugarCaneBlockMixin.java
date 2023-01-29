package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends Block implements IPlantable {
    public SugarCaneBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        Block block = Block.BLOCKS[par1World.getBlock(par2, par3 - 1, par4)];
        return block != null && block.canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return EnumPlantType.Beach;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return this.id;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return world.getBlockData(x, y, z);
    }
}
