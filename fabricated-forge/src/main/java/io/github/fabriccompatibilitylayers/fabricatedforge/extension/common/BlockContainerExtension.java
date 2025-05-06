package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public interface BlockContainerExtension extends BlockExtension {
    TileEntity createNewTileEntity(World world, int metadata);
}
