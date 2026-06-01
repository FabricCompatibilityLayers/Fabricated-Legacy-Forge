/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(StructureVillagePieces.class)
public class StructureVillagePiecesMixin {
    @Inject(method = "func_75084_a", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;iterator()Ljava/util/Iterator;", remap = false))
    private static void fml$addExtraVillageComponents(Random p_75084_0_, int p_75084_1_, CallbackInfoReturnable<ArrayList> cir, @Local ArrayList var2) {
        VillagerRegistry.addExtraVillageComponents(var2, p_75084_0_, p_75084_1_);
    }

    @ModifyReturnValue(method = "func_75083_a", at = @At("RETURN"))
    private static ComponentVillage fml$getVillageComponent(ComponentVillage original, ComponentVillageStartPiece p_75083_0_,
                                                            @Local(argsOnly = true) StructureVillagePieceWeight p_75083_1_,
                                                            @Local(argsOnly = true) List p_75083_2_,
                                                            @Local(argsOnly = true) Random p_75083_3_,
                                                            @Local(ordinal = 0, argsOnly = true) int p_75083_4_,
                                                            @Local(ordinal = 1, argsOnly = true) int p_75083_5_,
                                                            @Local(ordinal = 2, argsOnly = true) int p_75083_6_,
                                                            @Local(ordinal = 3, argsOnly = true) int p_75083_7_,
                                                            @Local(ordinal = 4, argsOnly = true) int p_75083_8_,
                                                            @Local Class var9) {
        if (original == null
                && var9 != ComponentVillageHouse4_Garden.class
                && var9 != ComponentVillageChurch.class
                && var9 != ComponentVillageHouse1.class
                && var9 != ComponentVillageWoodHut.class
                && var9 != ComponentVillageHall.class
                && var9 != ComponentVillageField.class
                && var9 != ComponentVillageField2.class
                && var9 != ComponentVillageHouse2.class
                && var9 != ComponentVillageHouse3.class
        ) {
            return (ComponentVillage) VillagerRegistry.getVillageComponent(p_75083_1_, p_75083_0_ , p_75083_2_, p_75083_3_, p_75083_4_, p_75083_5_, p_75083_6_, p_75083_7_, p_75083_8_);
        }

        return original;
    }
}
