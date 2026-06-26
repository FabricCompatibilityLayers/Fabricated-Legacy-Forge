/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedfml.utils.WidenedLocal;
import net.minecraft.src.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityRenderer.class, priority = 9999)
public abstract class EntityRendererLateMixin {
    @WidenedLocal(
            modid = "fabricated-forge",
            from = "net/minecraft/src/EntityPlayer",
            to = "net/minecraft/src/EntityLiving"
    )
    @Shadow
    protected abstract float getFOVModifier(float par1, boolean par2);
}
