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
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCache;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkCache.class)
public abstract class ChunkCacheMixin implements IBlockAccess {
    @Definition(id = "chunkArray", field = "Lnet/minecraft/src/ChunkCache;chunkArray:[[Lnet/minecraft/src/Chunk;")
    @Expression("this.chunkArray[?]")
    @WrapOperation(method = {"getBlockTileEntity", "getBlockMetadata"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private Chunk[] forge$safeAccess1(Chunk[][] array, int index, Operation<Chunk[]> original) {
        if (index >= 0 && index < array.length) {
            return original.call(array, index);
        }

        return null;
    }

    @Definition(id = "chunkArray", field = "Lnet/minecraft/src/ChunkCache;chunkArray:[[Lnet/minecraft/src/Chunk;")
    @Expression("this.chunkArray[?][?]")
    @WrapOperation(method = {"getBlockTileEntity", "getBlockMetadata"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private Chunk forge$safeAccess2(Chunk[] array, int index, Operation<Chunk> original) {
        if (array != null && index >= 0 && index < array.length) {
            return original.call(array, index);
        }

        return null;
    }

    @WrapOperation(method = "getBlockTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Chunk;getChunkBlockTileEntity(III)Lnet/minecraft/src/TileEntity;"))
    private TileEntity forge$safe$getChunkBlockTileEntity(Chunk instance, int par2, int par3, int i, Operation<TileEntity> original) {
        if (instance != null) {
            return original.call(instance, par2, par3, i);
        }

        return null;
    }

    @WrapOperation(method = "getBlockMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Chunk;getBlockMetadata(III)I"))
    private int forge$safe$getBlockMetadata(Chunk instance, int par2, int par3, int i, Operation<Integer> original) {
        if (instance != null) {
            return original.call(instance, par2, par3, i);
        }

        return 0;
    }
}
