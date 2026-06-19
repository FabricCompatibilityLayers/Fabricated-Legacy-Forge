/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.osl.self.fml;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.KeyHandlerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import net.ornithemc.osl.keybinds.api.KeybindRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Environment(EnvType.CLIENT)
@IfModLoaded(value = "osl-keybinds", minVersion = "0.3.0-alpha.1+mcb1.8-pre1-mc1.6.4")
@Mixin(KeyBindingRegistry.class)
public class KeyBindingRegistryMixin {
    @Shadow
    private Set<KeyBindingRegistry.KeyHandler> keyHandlers;

    /**
     * @author CatCore
     * @reason Register Keybindings through OSL
     */
    @Overwrite
    public void uploadKeyBindingsToGame(GameSettings settings) {
        for (KeyBindingRegistry.KeyHandler key : keyHandlers)
        {
            for (KeyBinding kb : key.getKeyBindings())
            {
                KeybindRegistry.register(kb, ((KeyHandlerAccessor) key).getModId());
            }
        }

        settings.loadOptions();
    }
}
