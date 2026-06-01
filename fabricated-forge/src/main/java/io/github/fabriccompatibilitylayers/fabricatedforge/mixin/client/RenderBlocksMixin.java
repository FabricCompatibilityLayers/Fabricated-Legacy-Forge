/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockFireExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(RenderBlocks.class)
public class RenderBlocksMixin {

    @Shadow public IBlockAccess blockAccess;
    @Shadow private int overrideBlockTexture;

    // Pattern N (@Redirect INVOKESTATIC): replaces BlockBed.getDirection(metadata) with
    // getBedDirection(IBlockAccess, x, y, z) — Forge API delegates direction logic to the block.
    // Logic delta: metadata is still fetched by vanilla bytecode but our handler ignores it;
    // net result is the same as removing the getBlockMetadata/getDirection pair.
    @Redirect(
        method = "renderBlockBed",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;getDirection(I)I") // TODO: verify — bytecode may emit BlockBed instead of BlockDirectional
    )
    private int forge$getBedDirection(int metadata,
                                      @Local(argsOnly = true) Block par1Block,
                                      @Local(argsOnly = true, ordinal = 0) int par2,
                                      @Local(argsOnly = true, ordinal = 1) int par3,
                                      @Local(argsOnly = true, ordinal = 2) int par4) {
        return ((BlockExtension) par1Block).getBedDirection(this.blockAccess, par2, par3, par4);
    }

    // Pattern N (@Redirect INVOKESTATIC): replaces BlockBed.isBlockHeadOfBed(metadata) with
    // isBedFoot(IBlockAccess, x, y, z) — Forge API delegates head/foot logic to the block.
    // Logic delta: metadata fetch still happens in bytecode but result is unused by us.
    @Redirect(
        method = "renderBlockBed",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;isBlockHeadOfBed(I)Z")
    )
    private boolean forge$isBedFoot(int metadata,
                                    @Local(argsOnly = true) Block par1Block,
                                    @Local(argsOnly = true, ordinal = 0) int par2,
                                    @Local(argsOnly = true, ordinal = 1) int par3,
                                    @Local(argsOnly = true, ordinal = 2) int par4) {
        return ((BlockExtension) par1Block).isBedFoot(this.blockAccess, par2, par3, par4);
    }

    // Pattern O (@ModifyExpressionValue INVOKE ordinal=0): intercepts the face-0 getBlockTexture
    // return value and replaces it with the override texture when active.
    @ModifyExpressionValue(
        method = "renderBlockBed",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getBlockTexture(Lnet/minecraft/src/IBlockAccess;IIII)I", ordinal = 0)
    )
    private int forge$overrideBedTextureBottom(int original) {
        return this.overrideBlockTexture >= 0 ? this.overrideBlockTexture : original;
    }

    // Pattern O (@ModifyExpressionValue INVOKE ordinal=1): intercepts the face-1 getBlockTexture
    // return value and replaces it with the override texture when active.
    @ModifyExpressionValue(
        method = "renderBlockBed",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getBlockTexture(Lnet/minecraft/src/IBlockAccess;IIII)I", ordinal = 1)
    )
    private int forge$overrideBedTextureTop(int original) {
        return this.overrideBlockTexture >= 0 ? this.overrideBlockTexture : original;
    }

    // Pattern N (@Redirect INVOKE ordinal=0): redirects canBlockCatchFire(below) to the 5-arg
    // Forge extension with ForgeDirection.UP — checking whether the block below can ignite upward.
    @Redirect(
        method = "renderBlockFire",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 0)
    )
    private boolean forge$canCatchFireBelow(BlockFire fire, IBlockAccess world, int x, int y, int z) {
        return ((BlockFireExtension) fire).canBlockCatchFire(world, x, y, z, ForgeDirection.UP);
    }

    // Pattern N (@Redirect INVOKE ordinal=1): west neighbour (par2-1) checks EAST face.
    @Redirect(
        method = "renderBlockFire",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 1)
    )
    private boolean forge$canCatchFireEast(BlockFire fire, IBlockAccess world, int x, int y, int z) {
        return ((BlockFireExtension) fire).canBlockCatchFire(world, x, y, z, ForgeDirection.EAST);
    }

    // Pattern N (@Redirect INVOKE ordinal=2): east neighbour (par2+1) checks WEST face.
    @Redirect(
        method = "renderBlockFire",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 2)
    )
    private boolean forge$canCatchFireWest(BlockFire fire, IBlockAccess world, int x, int y, int z) {
        return ((BlockFireExtension) fire).canBlockCatchFire(world, x, y, z, ForgeDirection.WEST);
    }

    // Pattern N (@Redirect INVOKE ordinal=3): north neighbour (par4-1) checks SOUTH face.
    @Redirect(
        method = "renderBlockFire",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 3)
    )
    private boolean forge$canCatchFireSouth(BlockFire fire, IBlockAccess world, int x, int y, int z) {
        return ((BlockFireExtension) fire).canBlockCatchFire(world, x, y, z, ForgeDirection.SOUTH);
    }

    // Pattern N (@Redirect INVOKE ordinal=4): south neighbour (par4+1) checks NORTH face.
    @Redirect(
        method = "renderBlockFire",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 4)
    )
    private boolean forge$canCatchFireNorth(BlockFire fire, IBlockAccess world, int x, int y, int z) {
        return ((BlockFireExtension) fire).canBlockCatchFire(world, x, y, z, ForgeDirection.NORTH);
    }

    // Pattern N (@Redirect INVOKE ordinal=5): above neighbour (par3+1) checks DOWN face.
    @Redirect(
        method = "renderBlockFire",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 5)
    )
    private boolean forge$canCatchFireAbove(BlockFire fire, IBlockAccess world, int x, int y, int z) {
        return ((BlockFireExtension) fire).canBlockCatchFire(world, x, y, z, ForgeDirection.DOWN);
    }

    @Definition(id = "fancyGrass", field = "Lnet/minecraft/src/RenderBlocks;fancyGrass:Z")
    @Expression("fancyGrass")
    @WrapOperation(method = {"renderStandardBlockWithAmbientOcclusion", "renderStandardBlockWithColorMultiplier"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$defaultTexture(Operation<Boolean> original) {
        return ((TessellatorAccessor) Tessellator.instance).isDefaultTexture() && original.call();
    }
}