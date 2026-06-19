/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.translations;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.Locale;
import net.ornithemc.osl.localization.impl.Localization;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@IfModLoaded("osl-localization")
@Mixin(Locale.class)
public abstract class LocaleMixin {
    @Shadow
    protected abstract void set(String key, String translation);

    @Inject(method = "init", at = @At("RETURN"))
    private void fml$initLanguage(Map<String, String> map, Properties properties, CallbackInfo ci) {
        if (properties != null) {
            Language language = Localization.getLanguageManager().getSelectedLanguage();
            LanguageRegistry.instance().loadLanguageTable(properties, language != null ? language.code() : "en_US");
        }
    }

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/localization/impl/Locale;setLastUpdateTime()V", remap = false), remap = false)
    private void fml$loadLanguage(ResourceManager resourceManager, List<String> languages, CallbackInfo ci) {
        for (String language : languages) {
            Properties properties = new Properties();
            LanguageRegistry.instance().loadLanguageTable(properties, language);

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                this.set(key, value);
            }
        }
    }
}
