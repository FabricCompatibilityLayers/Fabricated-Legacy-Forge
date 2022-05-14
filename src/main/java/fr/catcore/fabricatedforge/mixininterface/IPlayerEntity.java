package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IPlayerEntity {
    public float getCurrentPlayerStrVsBlock(Block par1Block, int meta);

    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z);
}
