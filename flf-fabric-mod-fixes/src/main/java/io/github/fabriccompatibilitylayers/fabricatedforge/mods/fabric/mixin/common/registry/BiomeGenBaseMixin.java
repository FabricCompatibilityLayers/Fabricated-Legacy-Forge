/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.IdUtils;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.BiomeRegistrationHelper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.BlockRegistrationHelper;
import net.minecraft.src.BiomeGenBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeGenBase.class)
public class BiomeGenBaseMixin {
    @Shadow
    @Final
    public int biomeID;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void osl$registerFMLBiomes(int par1, CallbackInfo ci) {
        if (this.biomeID == 0) return;

        if (BlockRegistrationHelper.ready) {
            if (IdUtils.isForgeModLoading()) {
                BiomeRegistrationHelper.registerBiome((BiomeGenBase) (Object) this);
            }
        }
    }
}
