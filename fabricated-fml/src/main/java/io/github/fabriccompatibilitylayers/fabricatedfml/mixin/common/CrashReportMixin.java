/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.src.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Inject(method = "func_71504_g", at = @At("RETURN"))
    private void fml$enhanceCrashReport(CallbackInfo ci) {
        FMLCommonHandler.instance().enhanceCrashReport((CrashReport)(Object) this);
    }
}
