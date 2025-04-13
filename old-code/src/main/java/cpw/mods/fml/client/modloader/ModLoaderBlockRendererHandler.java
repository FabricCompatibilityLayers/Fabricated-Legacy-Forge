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
package cpw.mods.fml.client.modloader;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.BaseMod;
import net.minecraft.block.Block;
import net.minecraft.client.BlockRenderer;
import net.minecraft.world.BlockView;

public class ModLoaderBlockRendererHandler implements ISimpleBlockRenderingHandler {
    private int renderId;
    private boolean render3dInInventory;
    private BaseMod mod;

    public ModLoaderBlockRendererHandler(int renderId, boolean render3dInInventory, BaseMod mod) {
        this.renderId = renderId;
        this.render3dInInventory = render3dInInventory;
        this.mod = mod;
    }

    public int getRenderId() {
        return this.renderId;
    }

    public boolean shouldRender3DInInventory() {
        return this.render3dInInventory;
    }

    public boolean renderWorldBlock(BlockView world, int x, int y, int z, Block block, int modelId, BlockRenderer renderer) {
        return this.mod.renderWorldBlock(renderer, world, x, y, z, block, modelId);
    }

    public void renderInventoryBlock(Block block, int metadata, int modelID, BlockRenderer renderer) {
        this.mod.renderInvBlock(renderer, block, metadata, modelID);
    }
}
