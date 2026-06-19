/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.modmenu.self;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.modmenu.ModMenuUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@IfModLoaded("modmenu")
@Mixin(Loader.class)
public class LoaderMixin {
    @Shadow
    private List<ModContainer> mods;

    @Inject(method = "initializeMods", at = @At("RETURN"))
    private void modmenu$addMods(CallbackInfo ci) {
        ModMenuUtils.addFMLMods(mods);
    }
}
