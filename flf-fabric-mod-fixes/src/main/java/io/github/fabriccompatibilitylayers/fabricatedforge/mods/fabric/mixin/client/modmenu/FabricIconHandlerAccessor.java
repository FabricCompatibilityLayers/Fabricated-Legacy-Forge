/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.modmenu;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Map;

@Environment(EnvType.CLIENT)
@IfModLoaded("modmenu")
@Mixin(value = FabricIconHandler.class, remap = false)
public interface FabricIconHandlerAccessor {
    @Accessor(value = "modIconCache", remap = false)
    Map<Path, BufferedImage> getCache();
}
