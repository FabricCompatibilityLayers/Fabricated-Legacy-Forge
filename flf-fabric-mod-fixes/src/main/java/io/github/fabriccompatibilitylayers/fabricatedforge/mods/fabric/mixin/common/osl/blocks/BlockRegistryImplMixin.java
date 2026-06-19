/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.blocks;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.BlockRegistrationHelper;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded("osl-blocks")
@Mixin(BlockRegistryImpl.class)
public class BlockRegistryImplMixin {
    @Shadow
    private static boolean locked;

    @Inject(method = "lock", at = @At("RETURN"))
    private static void fml$dontlock(CallbackInfo ci) {
        locked = false;
        BlockRegistrationHelper.ready = true;
    }
}
