package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLadder;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockLadder.class)
public abstract class BlockLadderMixin extends Block implements BlockExtension {
    public BlockLadderMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST ) ||
                par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST ) ||
                par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) ||
                par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideNorth$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, NORTH);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideSouth$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, SOUTH);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideWest$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, WEST);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideEast$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, EAST);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideNorth$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, NORTH);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideSouth$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, SOUTH);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideWest$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, WEST);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideEast$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, EAST);
    }

    @Override
    public boolean isLadder(World world, int x, int y, int z)
    {
        return true;
    }
}
