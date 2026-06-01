/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.server;

import io.github.fabriccompatibilitylayers.fabricatedfml.utils.ServerImplementation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Environment(EnvType.SERVER)
@Mixin(EntityLiving.class)
public class EntityLivingMixin {
    @Shadow protected HashMap activePotionsMap;

    // Readd this client-only method to the server
    @ServerImplementation("n")
    public void removePotionEffect(int par1) {
        this.activePotionsMap.remove(par1);
    }
}
