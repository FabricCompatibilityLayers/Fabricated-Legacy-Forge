package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.CocoaBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Random;

@Mixin(CocoaBlock.class)
public abstract class CocoaBlockMixin extends FacingBlock {
    @Shadow
    public static int method_292(int i) {
        return 0;
    }

    protected CocoaBlockMixin(int i, int j, Material material) {
        super(i, j, material);
    }

    /**
     * @author forge
     * @reason report call
     */
    @Overwrite
    public void method_410(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.method_410(par1World, par2, par3, par4, par5, par6, 0);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> dropped = super.getBlockDropped(world, x, y, z, metadata, fortune);
        int var8 = method_292(metadata);
        byte var9 = 1;
        if (var8 >= 2) {
            var9 = 3;
        }

        for(int var10 = 0; var10 < var9; ++var10) {
            dropped.add(new ItemStack(Item.DYES, 1, 3));
        }

        return dropped;
    }

    @Override
    public int method_398(int par1, Random par2Random, int par3) {
        return 0;
    }
}
