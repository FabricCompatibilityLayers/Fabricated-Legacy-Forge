package fr.catcore.fabricatedforge.mixin.forgefml.world;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.SERVER)
@Mixin(World.class)
public abstract class Server_WorldMixin implements BlockView, IWorld {
    @Shadow @Final public Dimension dimension;

    @Override
    public Biome getBiome(int par1, int par2) {
        return this.dimension.getBiomeGenForCoords(par1, par2);
    }

    @Override
    public boolean isAir(int par1, int par2, int par3) {
        int id = this.getBlock(par1, par2, par3);
        return id == 0 || Block.BLOCKS[id] == null || ((IBlock)Block.BLOCKS[id]).isAirBlock((World)(Object) this, par1, par2, par3);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Override
    public boolean isBlockSolid(int par1, int par2, int par3) {
        Block block = Block.BLOCKS[this.getBlock(par1, par2, par3)];
        return block != null && ((IBlock)block).isBlockNormalCube((World)(Object) this, par1, par2, par3);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Override
    public boolean isTopSolid(int par1, int par2, int par3) {
        return this.isBlockSolidOnSide(par1, par2, par3, ForgeDirection.UP);
    }

    @Override
    public int getMaxBuildHeight() {
        return this.dimension.getHeight();
    }
}
