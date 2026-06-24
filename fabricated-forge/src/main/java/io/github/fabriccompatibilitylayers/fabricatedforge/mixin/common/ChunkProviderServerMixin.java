/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldServerExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(ChunkProviderServer.class)
public abstract class ChunkProviderServerMixin implements IChunkProvider {
    @Shadow private WorldServer currentServer;

    @Shadow private Set chunksToUnload;

    @Shadow private List loadedChunks;

    @Shadow private IChunkProvider currentChunkProvider;

    @ModifyExpressionValue(method = "unloadChunksIfNotNearSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldProvider;canRespawnHere()Z"))
    private boolean forge$shouldLoadSpawn(boolean original) {
        return original && DimensionManager.shouldLoadSpawn(currentServer.provider.dimensionId);
    }

    @WrapOperation(method = "loadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ChunkProviderServer;safeLoadChunk(II)Lnet/minecraft/src/Chunk;"))
    private Chunk forge$fetchDormantChunk(ChunkProviderServer instance, int par1, int par2, Operation<Chunk> original,
                                          @Local long var3) {
        Chunk var5 = ForgeChunkManager.fetchDormantChunk(var3, currentServer);
        if (var5 == null)
        {
            var5 = original.call(instance, par1, par2);
        }

        return var5;
    }

    @Inject(method = "unload100OldestChunks", at = @At("HEAD"))
    private void forge$excludePersistantChunks(CallbackInfoReturnable<Boolean> cir) {
        if (!this.currentServer.canNotSave) {
            for (ChunkCoordIntPair forced : ((WorldServerExtension) currentServer).getPersistentChunks().keySet())
            {
                this.chunksToUnload.remove(ChunkCoordIntPair.chunkXZ2Int(forced.chunkXPos, forced.chunkZPos));
            }
        }
    }

    @WrapOperation(method = "unload100OldestChunks", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"))
    private boolean forge$dormantChunks(List instance, Object o, Operation<Boolean> original) {
        Chunk var3 = (Chunk) o;
        boolean result = original.call(instance, var3);

        ForgeChunkManager.putDormantChunk(ChunkCoordIntPair.chunkXZ2Int(var3.xPosition, var3.zPosition), var3);
        if(loadedChunks.isEmpty() && ForgeChunkManager.getPersistentChunksFor(currentServer).isEmpty() && !DimensionManager.shouldLoadSpawn(currentServer.provider.dimensionId)) {
            DimensionManager.unloadWorld(currentServer.provider.dimensionId);
            return currentChunkProvider.unload100OldestChunks();
        }

        return result;
    }
}
