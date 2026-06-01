/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.src.EntityAIVillagerMate;
import net.minecraft.src.EntityVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(EntityAIVillagerMate.class)
public class EntityAIVillagerMateMixin {
    @Shadow private EntityVillager field_75450_b;

    @Redirect(method = "func_75447_i", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityVillager;func_70938_b(I)V"))
    private void fml$applyRandomTrade(EntityVillager instance, int i) {
        VillagerRegistry.applyRandomTrade(instance, this.field_75450_b.func_70681_au());
    }

    @Redirect(method = "func_75447_i", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int fml$cancelRandomUsage(Random instance, int i) {
        return 0;
    }
}
