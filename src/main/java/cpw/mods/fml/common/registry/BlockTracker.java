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
package cpw.mods.fml.common.registry;

import net.minecraft.block.Block;

import java.util.BitSet;

class BlockTracker {
    private static final BlockTracker INSTANCE = new BlockTracker();
    private BitSet allocatedBlocks = new BitSet(4096);

    private BlockTracker() {
        this.allocatedBlocks.set(0, 4096);

        for(int i = 0; i < Block.BLOCKS.length; ++i) {
            if (Block.BLOCKS[i] != null) {
                this.allocatedBlocks.clear(i);
            }
        }

    }

    public static int nextBlockId() {
        return instance().getNextBlockId();
    }

    private int getNextBlockId() {
        int idx = this.allocatedBlocks.nextSetBit(0);
        this.allocatedBlocks.clear(idx);
        return idx;
    }

    private static BlockTracker instance() {
        return INSTANCE;
    }

    public static void reserveBlockId(int id) {
        instance().doReserveId(id);
    }

    private void doReserveId(int id) {
        this.allocatedBlocks.clear(id);
    }
}
