/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.osl.keybinds.self.fml;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.keybinds.KeyHandlerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.keybinds.api.KeybindRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Environment(EnvType.CLIENT)
@Conditional(modLoaded = @Mod(value = "osl-keybinds", version = ">=0.3.0-"))
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
