/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry.fml;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import cpw.mods.fml.common.registry.EntityRegistry;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.EntityRegistrationHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRegistry.class)
public class EntityRegistryMixin {
    @WrapWithCondition(method = "registerGlobalEntityID(Ljava/lang/Class;Ljava/lang/String;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityList;addMapping(Ljava/lang/Class;Ljava/lang/String;I)V"))
    private static boolean osl$registerEntitySimple(Class aClass, String s, int i) {
        return EntityRegistrationHelper.register(aClass, s, i);
    }

    @WrapWithCondition(method = "registerGlobalEntityID(Ljava/lang/Class;Ljava/lang/String;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityList;addMapping(Ljava/lang/Class;Ljava/lang/String;III)V"))
    private static boolean osl$registerEntityWithEgg(Class aClass, String s, int i, int j, int k) {
        return EntityRegistrationHelper.register(aClass, s, i, j, k);
    }
}
