package cpw.mods.fml.common;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;

import java.util.Random;

public interface IWorldGenerator {
    void generate(Random random, int i, int j, World arg, ChunkProvider arg2, ChunkProvider arg3);
}
