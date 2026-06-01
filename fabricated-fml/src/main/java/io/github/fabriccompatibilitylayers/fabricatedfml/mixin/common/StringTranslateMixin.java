/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.StringTranslate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Properties;

@Mixin(StringTranslate.class)
public class StringTranslateMixin {
    @Inject(method = "func_74812_a", at = @At("RETURN"))
    private void fml$loadLanguageTable(Properties p_74812_1_, String p_74812_2_, CallbackInfo ci) {
        LanguageRegistry.instance().loadLanguageTable(p_74812_1_, p_74812_2_);
    }
}
