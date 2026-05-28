package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "breakBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$beginLeavesDecay$check(int left, int right, Operation<Boolean> original,
                                                 @Share(namespace = "fabricated-forge", value = "block") LocalRef<Block> blockRef) {
        Block block = Block.blocksList[left];
        blockRef.set(block);
        return block != null;
    }

    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockMetadata(III)I"))
    private int forge$beginLeavesDecay$metadata(World instance, int par2, int par3, int i) {
        return 0;
    }

    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockMetadata(IIII)Z"))
    private boolean forge$beginLeavesDecay(World instance, int i, int j, int k, int l,
                                           @Share(namespace = "fabricated-forge", value = "block") LocalRef<Block> blockRef) {
        ((BlockExtension) blockRef.get()).beginLeavesDecay(instance, i, j, k);
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
