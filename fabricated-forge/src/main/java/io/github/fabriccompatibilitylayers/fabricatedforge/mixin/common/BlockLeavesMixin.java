package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(BlockLeaves.class)
public abstract class BlockLeavesMixin extends BlockLeavesBase implements BlockExtension, IShearable {
    protected BlockLeavesMixin(int par1, int par2, Material par3Material, boolean par4) {
        super(par1, par2, par3Material, par4);
    }

    @Redirect(method = "breakBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/src/BlockLeaves;blockID:I"))
    private int forge$beginLeavesDecay$check(BlockLeaves instance, @Local(ordinal = 9) int var12) {
        return (Block.blocksList[var12] != null)
                ? var12 : -2;
    }

    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockMetadata(IIII)Z"))
    private boolean forge$beginLeavesDecay(World instance, int i, int j, int k, int l, @Local(ordinal = 9) int var12) {
        ((BlockExtension) Block.blocksList[var12]).beginLeavesDecay(instance, i, j, k);
        return true;
    }

    @Redirect(method = "updateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;blockID:I"))
    private int forge$canSustainLeaves(Block instance,
                                       @Local(argsOnly = true) World par1World,
                                       @Local(argsOnly = true, ordinal = 0) int par2,
                                       @Local(argsOnly = true, ordinal = 1) int par3,
                                       @Local(argsOnly = true, ordinal = 2) int par4,
                                       @Local(ordinal = 7) int var12,
                                       @Local(ordinal = 8) int var13,
                                       @Local(ordinal = 9) int var14,
                                       @Local(ordinal = 10) int var15,
                                       @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        Block block = Block.blocksList[var15];
        blockRef.set(block);
        return (block != null && ((BlockExtension) block).canSustainLeaves(par1World, par2 + var12, par3 + var13, par4 + var14))
                ? var15 : -2;
    }

    @Redirect(method = "updateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/src/BlockLeaves;blockID:I"))
    private int forge$isLeaves(BlockLeaves instance,
                               @Local(argsOnly = true) World par1World,
                               @Local(argsOnly = true, ordinal = 0) int par2,
                               @Local(argsOnly = true, ordinal = 1) int par3,
                               @Local(argsOnly = true, ordinal = 2) int par4,
                               @Local(ordinal = 7) int var12,
                               @Local(ordinal = 8) int var13,
                               @Local(ordinal = 9) int var14,
                               @Local(ordinal = 10) int var15,
                               @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        Block block = blockRef.get();
        return (block != null && ((BlockExtension) block).isLeaves(par1World, par2 + var12, par3 + var13, par4 + var14))
                ? var15 : -2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    @Override
    public boolean isShearable(ItemStack item, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
        return ret;
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z)
    {
        world.setBlockMetadata(x, y, z, world.getBlockMetadata(x, y, z) | 8);
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z)
    {
        return true;
    }
}
