package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(BlockSnow.class)
public abstract class BlockSnowMixin extends Block implements BlockExtension {
    public BlockSnowMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @ModifyConstant(method = "canPlaceBlockAt", constant = @Constant(intValue = 0))
    private int forge$NonNullBlock(int constant,
                                   @Local(ordinal = 3) int var5,
                                   @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        Block block = Block.blocksList[var5];

        if (block != null) {
            blockRef.set(block);
            return -2;
        }

        return var5;
    }

    @Redirect(method = "canPlaceBlockAt", at = @At(value = "FIELD", target = "Lnet/minecraft/src/BlockLeaves;blockID:I"))
    private int forge$isLeaves(BlockLeaves instance,
                               @Local(argsOnly = true) World par1World,
                               @Local(argsOnly = true, ordinal = 0) int par2,
                               @Local(argsOnly = true, ordinal = 1) int par3,
                               @Local(argsOnly = true, ordinal = 2) int par4,
                               @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        Block block = blockRef.get();
        return (((BlockExtension) block).isLeaves(par1World, par2, par3 - 1, par4))
                ? block.blockID : -2;
    }

    @Redirect(method = {"canSnowStay", "updateTick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockSnow;dropBlockAsItem(Lnet/minecraft/src/World;IIIII)V"))
    private void forge$cancelDropBlockAsItem(BlockSnow instance, World world, int i, int j, int k, int l, int m) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        dropBlockAsItem(par1World, par3, par4, par5, par6, 0);
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
}
