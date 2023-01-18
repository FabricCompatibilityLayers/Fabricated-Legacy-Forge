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
package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.NetherChunkGenerator;
import net.minecraft.world.chunk.SurfaceChunkGenerator;

import java.util.Random;

public class ModLoaderWorldGenerator implements IWorldGenerator {
    private BaseModProxy mod;

    public ModLoaderWorldGenerator(BaseModProxy mod) {
        this.mod = mod;
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, ChunkProvider chunkGenerator, ChunkProvider chunkProvider) {
        if (chunkGenerator instanceof SurfaceChunkGenerator) {
            this.mod.generateSurface(world, random, chunkX << 4, chunkZ << 4);
        } else if (chunkGenerator instanceof NetherChunkGenerator) {
            this.mod.generateNether(world, random, chunkX << 4, chunkZ << 4);
        }
    }
}
