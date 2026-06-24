/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityDragon.class)
public abstract class EntityDragonMixin extends EntityLiving {
    public EntityDragonMixin(World par1World) {
        super(par1World);
    }

    @WrapOperation(method = "destroyBlocksInAABB", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
    private int forge$capturePos(World instance, int par2, int par3, int i, Operation<Integer> original,
                                 @Share(namespace = "forge", value = "pos")LocalRef<ChunkCoordinates> pos) {
        pos.set(new ChunkCoordinates(par2, par3, i));
        return original.call(instance, par2, par3, i);
    }

    @Expression("? != 0")
    @WrapOperation(method = "destroyBlocksInAABB", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$airCheck(int left, int right, Operation<Boolean> original) {
        return Block.blocksList[left] != null;
    }

    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Definition(id = "bedrock", field = "Lnet/minecraft/src/Block;bedrock:Lnet/minecraft/src/Block;")
    @Expression("? != bedrock.blockID")
    @WrapOperation(method = "destroyBlocksInAABB", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canDragonDestroy(int left, int right, Operation<Boolean> original,
                                           @Share(namespace = "forge", value = "pos")LocalRef<ChunkCoordinates> posRef) {
        ChunkCoordinates pos = posRef.get();
        return original.call(left, right) && ((BlockExtension) Block.blocksList[left]).canDragonDestroy(worldObj, pos.posX, pos.posY, pos.posZ);
    }
}
