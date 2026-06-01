/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public class EntityItemMixin {
    @Inject(method = "func_70100_b_", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;func_72956_a(Lnet/minecraft/src/Entity;Ljava/lang/String;FF)V"))
    private void fml$onPickupNotification(EntityPlayer p_70100_1_, CallbackInfo ci) {
        GameRegistry.onPickupNotification(p_70100_1_, (EntityItem)(Object) this);
    }
}
