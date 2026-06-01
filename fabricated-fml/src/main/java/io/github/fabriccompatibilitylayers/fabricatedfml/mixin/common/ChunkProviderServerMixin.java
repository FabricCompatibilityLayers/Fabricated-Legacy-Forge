/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.ChunkProviderServer;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkProviderServer.class)
public class ChunkProviderServerMixin {
    @Shadow private WorldServer field_73251_h;

    @Shadow private IChunkProvider field_73246_d;

    @Inject(method = "func_73153_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Chunk;func_76630_e()V"))
    private void fml$generateWorld(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_, CallbackInfo ci) {
        GameRegistry.generateWorld(p_73153_2_, p_73153_3_, field_73251_h, field_73246_d, p_73153_1_);
    }
}
