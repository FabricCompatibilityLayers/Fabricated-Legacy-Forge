package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;

@Mixin(NetherWartBlock.class)
public class NetherWartBlockMixin extends FlowerBlock {
    protected NetherWartBlockMixin(int i, int j, Material material) {
        super(i, j, material);
    }

    /**
     * @author forge
     * @reason add hooks
     */
    @Overwrite
    public boolean canStayPlaced(World par1World, int par2, int par3, int par4) {
        Block block = Block.BLOCKS[par1World.getBlock(par2, par3 - 1, par4)];
        return block != null && block.canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_410(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.method_410(par1World, par2, par3, par4, par5, par6, par7);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList();
        int count = 1;
        if (metadata >= 3) {
            count = 2 + world.random.nextInt(3) + (fortune > 0 ? world.random.nextInt(fortune + 1) : 0);
        }

        for(int i = 0; i < count; ++i) {
            ret.add(new ItemStack(Item.NETHER_WART));
        }

        return ret;
    }
}
