/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.osl.self.fml;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.KeyHandlerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.KeyBinding;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Conditional(modLoaded = @Mod(value = "osl-keybinds", version = ">=0.3.0-"))
@Mixin(KeyBindingRegistry.KeyHandler.class)
public class KeyHandlerMixin implements KeyHandlerAccessor {
    private String modId;

    @Inject(method = "<init>([Lnet/minecraft/src/KeyBinding;)V", at = @At("RETURN"))
    private void osl$comuteModId(KeyBinding[] keyBindings, CallbackInfo ci) {
        ModContainer modContainer = Loader.instance().activeModContainer();

        if (modContainer != null) {
            modId = modContainer.getModId();
        } else {
            modId = "None";
        }
    }

    @Inject(method = "<init>([Lnet/minecraft/src/KeyBinding;[Z)V", at = @At("RETURN"))
    private void osl$comuteModId(KeyBinding[] keyBindings, boolean[] repeatings, CallbackInfo ci) {
        ModContainer modContainer = Loader.instance().activeModContainer();

        if (modContainer != null) {
            modId = modContainer.getModId();
        } else {
            modId = "None";
        }
    }

    @Override
    public String getModId() {
        return modId;
    }
}
