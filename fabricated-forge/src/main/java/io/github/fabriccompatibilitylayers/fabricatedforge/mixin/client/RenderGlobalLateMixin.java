/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedfml.utils.WidenedOverload;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.RenderGlobalExtension;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = RenderGlobal.class, priority = 9999)
public abstract class RenderGlobalLateMixin implements RenderGlobalExtension {
    @WidenedOverload(name = "a", desc = "(Lave;Log;F)V")
    @Override
    public void drawBlockDamageTexture(Tessellator par1Tessellator, EntityLiving par2EntityLiving, float par3) {
        // placeholder body — replaced at post-apply with the narrow method's instructions
    }
}
