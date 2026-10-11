/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blockentity.fml;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.blockentity.BlockEntityRegistrationHelper;
import net.minecraft.src.TileEntity;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Conditional(modLoaded = @Mod("osl-blockentities"))
@Mixin(GameRegistry.class)
public class GameRegistryMixin {
    @Inject(method = "registerTileEntity", at = @At("HEAD"), cancellable = true)
    private static void osl$registerBO(Class<? extends TileEntity> tileEntityClass, String id, CallbackInfo ci) {
        BlockEntityRegistrationHelper.register(tileEntityClass, id);
        ci.cancel();
    }
}
