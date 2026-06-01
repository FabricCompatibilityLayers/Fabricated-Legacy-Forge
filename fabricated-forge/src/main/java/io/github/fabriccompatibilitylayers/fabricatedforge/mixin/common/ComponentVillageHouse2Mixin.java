/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.*;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ComponentVillageHouse2.class)
public class ComponentVillageHouse2Mixin {
    @Redirect(method = "addComponentParts", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int forge$getCount(Random instance, int i) {
        return ChestGenHooks.getCount(ChestGenHooks.VILLAGE_BLACKSMITH, instance) - 3;
    }

    @WrapOperation(method = "addComponentParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ComponentVillageHouse2;generateStructureChestContents(Lnet/minecraft/src/World;Lnet/minecraft/src/StructureBoundingBox;Ljava/util/Random;III[Lnet/minecraft/src/WeightedRandomChestContent;I)Z"))
    private boolean forge$getItems(ComponentVillageHouse2 instance, World world, StructureBoundingBox structureBoundingBox, Random random, int i, int j, int k, WeightedRandomChestContent[] weightedRandomChestContents, int l, Operation<Boolean> original) {
        return original.call(instance, world, structureBoundingBox, random, i, j, k, ChestGenHooks.getItems(ChestGenHooks.VILLAGE_BLACKSMITH), l);
    }
}
