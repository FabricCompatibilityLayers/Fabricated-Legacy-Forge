/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemBlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBlock.class)
public abstract class ItemBlockMixin extends Item implements ItemBlockExtension {
    @Shadow private int blockID;

    protected ItemBlockMixin(int par1) {
        super(par1);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void forge$setDefaultTexture(int par1, CallbackInfo ci) {
        this.setIsDefaultTexture(((BlockAccessor) Block.blocksList[par1 + 256]).isDefaultTexture());
    }

    @Definition(id = "deadBush", field = "Lnet/minecraft/src/Block;deadBush:Lnet/minecraft/src/BlockDeadBush;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockDeadBush;blockID:I")
    @Definition(id = "var11", local = @Local(ordinal = 4, type = int.class))
    @Expression("var11 != deadBush.blockID")
    @WrapOperation(method = {"onItemUse", "canPlaceItemBlockOnSide"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isBlockReplaceable(int var11, int right, Operation<Boolean> original,
                                             @Local(argsOnly = true) World par3World,
                                             @Local(ordinal = 0, argsOnly = true) int par4,
                                             @Local(ordinal = 1, argsOnly = true) int par5,
                                             @Local(ordinal = 2, argsOnly = true) int par6) {
        return original.call(var11, right) && (Block.blocksList[var11] == null || !((BlockExtension) Block.blocksList[var11]).isBlockReplaceable(par3World, par4, par5, par6));
    }

    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockAndMetadataWithNotify(IIIII)Z"))
    private boolean forge$placeBlockAt(World par3World, int par4, int par5, int par6, int blockID, int i,
                                       @Local(argsOnly = true) ItemStack par1ItemStack,
                                       @Local(argsOnly = true) EntityPlayer par2EntityPlayer,
                                       @Local(ordinal = 3, argsOnly = true) int par7,
                                       @Local(ordinal = 0, argsOnly = true) float par8,
                                       @Local(ordinal = 1, argsOnly = true) float par9,
                                       @Local(ordinal = 2, argsOnly = true) float par10) {
        return placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
    }

    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 1))
    private int forge$cancelIf(World instance, int par2, int par3, int i) {
        return -2;
    }

    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, this.getMetadata(stack.getItemDamage())))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == this.blockID)
        {
            Block.blocksList[this.blockID].updateBlockMetadata(world, x, y, z, side, hitX, hitY, hitZ);
            Block.blocksList[this.blockID].onBlockPlacedBy(world, x, y, z, player);
        }

        return true;
    }
}
