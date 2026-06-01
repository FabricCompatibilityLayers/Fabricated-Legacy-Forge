/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackBase;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(RenderEngine.class)
public class RenderEngineMixin {

    // Pattern A (@Inject at INVOKE): fires right before the first Buffer.clear() call inside
    // getTexture's try block — 1:1 with the patch insertion point, no local capture needed.
    @Inject(
        method = "getTexture",
        at = @At(value = "INVOKE", target = "Ljava/nio/IntBuffer;clear()Ljava/nio/Buffer;", ordinal = 0)
    )
    private void forge$onTextureLoadPre(String par1Str, CallbackInfoReturnable<Integer> cir) {
        ForgeHooksClient.onTextureLoadPre(par1Str);
    }

    // Pattern A (@Inject at RETURN ordinal=1): fires before the happy-path return in getTexture.
    // ordinal 0 = early return (cached hit, line 103), ordinal 1 = happy path (line 138),
    // ordinal 2 = catch-path return. Captures var6 (TexturePackBase) via @Local — it is the
    // only TexturePackBase local in the method so ordinal=0 is unambiguous.
    @Inject(
        method = "getTexture",
        at = @At(value = "RETURN", ordinal = 1)
    )
    private void forge$onTextureLoad(String par1Str, CallbackInfoReturnable<Integer> cir,
                                     @Local(ordinal = 0) TexturePackBase var6) {
        ForgeHooksClient.onTextureLoad(par1Str, var6);
    }
}