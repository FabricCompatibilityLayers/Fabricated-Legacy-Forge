package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public interface IBlockWithEntity {

    BlockEntity createNewTileEntity(World world, int metadata);
}
