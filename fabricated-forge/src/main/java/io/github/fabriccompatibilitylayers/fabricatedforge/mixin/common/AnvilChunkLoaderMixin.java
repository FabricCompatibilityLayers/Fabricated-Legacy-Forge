/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.AnvilChunkLoader;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilChunkLoader.class)
public class AnvilChunkLoaderMixin {
    @ModifyReturnValue(method = "checkedReadChunkFromNBT", at = @At(value = "RETURN", ordinal = 2))
    private Chunk fml$postChunkDataEventLoad(Chunk var5, @Local(argsOnly = true) NBTTagCompound par4NBTTagCompound) {
        MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(var5, par4NBTTagCompound));
        return var5;
    }

    @WrapOperation(method = "saveChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/AnvilChunkLoader;func_75824_a(Lnet/minecraft/src/ChunkCoordIntPair;Lnet/minecraft/src/NBTTagCompound;)V"))
    private void fml$postChunkDataEventSave(AnvilChunkLoader instance, ChunkCoordIntPair par2NBTTagCompound, NBTTagCompound var3, Operation<Void> original, @Local(argsOnly = true) Chunk par2Chunk) {
        original.call(instance, par2NBTTagCompound, var3);
        MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Save(par2Chunk, var3));
    }
}
