package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLever;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockLever.class)
public abstract class BlockLeverMixin extends Block implements BlockExtension {
    public BlockLeverMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(par5);
        return (dir == DOWN  && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 + 1, par4, DOWN )) ||
                (dir == UP    && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 - 1, par4, UP   )) ||
                (dir == NORTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) ||
                (dir == SOUTH && ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
                (dir == WEST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST )) ||
                (dir == EAST  && ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST ));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return ((WorldExtension) par1World).isBlockSolidOnSide(par2 - 1, par3, par4, EAST ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2 + 1, par3, par4, WEST ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4 + 1, NORTH) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 - 1, par4, UP   ) ||
                ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 + 1, par4, DOWN );
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideDown$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, DOWN);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;doesBlockHaveSolidTopSurface(III)Z"))
    private boolean forge$isBlockSolidOnSideUp$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, UP);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideNorth$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, NORTH);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideSouth$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, SOUTH);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideWest$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, WEST);
    }

    @Redirect(method = "updateBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 4))
    private boolean forge$isBlockSolidOnSideEast$updateBlockMetadata(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, EAST);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 0))
    private boolean forge$isBlockSolidOnSideEast$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, EAST);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 1))
    private boolean forge$isBlockSolidOnSideWest$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, WEST);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 2))
    private boolean forge$isBlockSolidOnSideSouth$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, SOUTH);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 3))
    private boolean forge$isBlockSolidOnSideNorth$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, NORTH);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;doesBlockHaveSolidTopSurface(III)Z"))
    private boolean forge$isBlockSolidOnSideUp$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, UP);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z", ordinal = 4)))
    private boolean forge$isBlockSolidOnSideDown$onNeighborBlockChange(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, DOWN);
    }

}
