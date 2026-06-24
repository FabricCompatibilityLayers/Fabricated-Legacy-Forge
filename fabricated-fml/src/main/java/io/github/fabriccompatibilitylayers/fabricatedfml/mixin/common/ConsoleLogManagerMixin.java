/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.src.ConsoleLogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.logging.Logger;

@Mixin(ConsoleLogManager.class)
public class ConsoleLogManagerMixin {
    @Redirect(method = "func_73699_a", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;setUseParentHandlers(Z)V"))
    private static void fml$setParent(Logger instance, boolean useParentHandlers) {
        instance.setParent(FMLLog.getLogger());
    }
}
