package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public interface IBlockWithEntity extends IBlock {

    BlockEntity createNewTileEntity(World world, int metadata);
}
