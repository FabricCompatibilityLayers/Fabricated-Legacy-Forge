package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockLog.class)
public abstract class BlockLogMixin extends Block implements BlockExtension {
    public BlockLogMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Redirect(method = "breakBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/src/BlockLeaves;blockID:I"))
    private int forge$beginLeavesDecay$check(BlockLeaves instance, @Local(ordinal = 9) int var12) {
        return (Block.blocksList[var12] != null)
                ? var12 : -2;
    }

    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockMetadata(III)I"))
    private int forge$beginLeavesDecay$metadata(World instance, int par2, int par3, int i) {
        return 0;
    }

    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockMetadata(IIII)Z"))
    private boolean forge$beginLeavesDecay(World instance, int i, int j, int k, int l, @Local(ordinal = 9) int var12) {
        ((BlockExtension) Block.blocksList[var12]).beginLeavesDecay(instance, i, j, k);
        return true;
    }

    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean isWood(World world, int x, int y, int z)
    {
        return true;
    }
}
