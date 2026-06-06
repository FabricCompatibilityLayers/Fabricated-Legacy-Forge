/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import cpw.mods.fml.relauncher.FMLRelauncher;
import net.minecraft.client.MinecraftApplet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftApplet.class)
public abstract class MinecraftAppletMixin {
    @WrapMethod(method = "init")
    public void fml$init(Operation<Void> original) {
        if (FMLRelauncher.appletEntry((MinecraftApplet)(Object)this)) {
            original.call();
            FMLRelauncher.appletStart((MinecraftApplet)(Object) this);
        }
    }
}
