/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.PlayerInstance;
import net.minecraft.src.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PlayerInstance.class)
public class PlayerInstanceMixin {
    @WrapOperation(method = "sendChunkUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;getAllTileEntityInBox(IIIIII)Ljava/util/List;"))
    private List forge$fixBug(WorldServer instance, int par2, int par3, int par4, int par5, int par6, int i, Operation<List> original) {
        //BugFix: 16 makes it load an extra chunk, which isn't associated with a player, which makes it not unload unless a player walks near it.
        //To_Do: Find a way to efficiently clean abandoned chunks.
        return original.call(instance, par2, par3, par4, par5 - 1, par6, i - 1);
    }
}
