package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.ComponentScatteredFeatureDesertPyramid;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.WeightedRandomChestContent;
import net.minecraft.src.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ComponentScatteredFeatureDesertPyramid.class)
public class ComponentScatteredFeatureDesertPyramidMixin {
    @Redirect(method = "addComponentParts", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int forge$getCount(Random instance, int i) {
        return ChestGenHooks.getCount(ChestGenHooks.PYRAMID_DESERT_CHEST, instance) - 2;
    }

    @WrapOperation(method = "addComponentParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ComponentScatteredFeatureDesertPyramid;generateStructureChestContents(Lnet/minecraft/src/World;Lnet/minecraft/src/StructureBoundingBox;Ljava/util/Random;III[Lnet/minecraft/src/WeightedRandomChestContent;I)Z"))
    private boolean forge$getItems(ComponentScatteredFeatureDesertPyramid instance, World world, StructureBoundingBox structureBoundingBox, Random random, int i, int j, int k, WeightedRandomChestContent[] weightedRandomChestContents, int l, Operation<Boolean> original) {
        return original.call(instance, world, structureBoundingBox, random, i, j, k, ChestGenHooks.getItems(ChestGenHooks.PYRAMID_DESERT_CHEST), l);
    }
}
