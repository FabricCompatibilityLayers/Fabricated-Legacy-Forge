/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.client.registry;

import net.minecraft.block.Block;
import net.minecraft.client.BlockRenderer;
import net.minecraft.world.BlockView;

public interface ISimpleBlockRenderingHandler {
    void renderInventoryBlock(Block arg, int i, int j, BlockRenderer arg2);

    boolean renderWorldBlock(BlockView arg, int i, int j, int k, Block arg2, int l, BlockRenderer arg3);

    boolean shouldRender3DInInventory();

    int getRenderId();
}
