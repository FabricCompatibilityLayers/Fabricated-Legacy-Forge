package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockTorch.class)
public abstract class BlockTorchMixin extends Block implements BlockExtension {
    @Shadow protected abstract boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4);

    public BlockTorchMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @ModifyReturnValue(method = "canPlaceTorchOn", at = @At(value = "RETURN", ordinal = 1))
    private boolean forge$canPlaceTorchOnTop(boolean original,
                                             @Local(argsOnly = true) World par1World,
                                             @Local(argsOnly = true, ordinal = 0) int par2,
                                             @Local(argsOnly = true, ordinal = 1) int par3,
                                             @Local(argsOnly = true, ordinal = 2) int par4,
                                             @Local(ordinal = 3) int var5) {
        return (Block.blocksList[var5] != null && ((BlockExtension) Block.blocksList[var5]).canPlaceTorchOnTop(par1World, par2, par3, par4)) || original;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST,  true) ||
                par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST,  true) ||
                par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) ||
                par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) ||
                canPlaceTorchOn(par1World, par2, par3 - 1, par4);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideNorth$updateBlockMetadata(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, NORTH, b);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideSouth$updateBlockMetadata(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, SOUTH, b);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideWest$updateBlockMetadata(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, WEST, b);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideEast$updateBlockMetadata(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, EAST, b);
    }

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideEast$onBlockAdded(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, EAST, b);
    }

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideWest$onBlockAdded(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, WEST, b);
    }

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideSouth$onBlockAdded(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, SOUTH, b);
    }

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideNorth$onBlockAdded(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, NORTH, b);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideEast$onNeighborBlockChange(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, EAST, b);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideWest$onNeighborBlockChange(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, WEST, b);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideSouth$onNeighborBlockChange(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, SOUTH, b);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCubeDefault(IIIZ)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideNorth$onNeighborBlockChange(World instance, int par2, int par3, int par4, boolean b) {
        return instance.isBlockSolidOnSide(par2, par3, par4, NORTH, b);
    }
}
