package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.src.ComponentScatteredFeatureJunglePyramid;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.WeightedRandomChestContent;
import net.minecraft.src.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ComponentScatteredFeatureJunglePyramid.class)
public class ComponentScatteredFeatureJunglePyramidMixin {
    @Inject(method = "addComponentParts", at = @At(value = "FIELD", target = "Lnet/minecraft/src/ComponentScatteredFeatureJunglePyramid;field_74945_j:Z", ordinal = 0))
    private void forge$getChestGenHooks(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox, CallbackInfoReturnable<Boolean> cir,
                                        @Share(value = "dispenser", namespace = "fabricated-forge") LocalRef<ChestGenHooks> dispenserRef,
                                        @Share(value = "chest", namespace = "fabricated-forge") LocalRef<ChestGenHooks> chestRef) {
        dispenserRef.set(ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER));
        chestRef.set(ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST));
    }

    @WrapOperation(method = "addComponentParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ComponentScatteredFeatureJunglePyramid;generateStructureDispenserContents(Lnet/minecraft/src/World;Lnet/minecraft/src/StructureBoundingBox;Ljava/util/Random;IIII[Lnet/minecraft/src/WeightedRandomChestContent;I)Z"))
    private boolean forge$dispenser(ComponentScatteredFeatureJunglePyramid instance,
                                    World par1World,
                                    StructureBoundingBox par2StructureBoundingBox,
                                    Random par3Random,
                                    int par4,
                                    int par5,
                                    int par6,
                                    int par7,
                                    WeightedRandomChestContent[] par8ArrayOfWeightedRandomChestContent,
                                    int par9,
                                    Operation<Boolean> original,
                                    @Share(value = "dispenser", namespace = "fabricated-forge") LocalRef<ChestGenHooks> dispenserRef) {
        return original.call(instance, par1World, par2StructureBoundingBox, par3Random, par4, par5, par6, par7, dispenserRef.get().getItems(), dispenserRef.get().getCount(par3Random));
    }

    @Redirect(method = "addComponentParts", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int forge$getCount(Random instance, int i,
                               @Share(value = "chest", namespace = "fabricated-forge") LocalRef<ChestGenHooks> chestRef) {
        return chestRef.get().getCount(instance) - 2;
    }

    @WrapOperation(method = "addComponentParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ComponentScatteredFeatureJunglePyramid;generateStructureChestContents(Lnet/minecraft/src/World;Lnet/minecraft/src/StructureBoundingBox;Ljava/util/Random;III[Lnet/minecraft/src/WeightedRandomChestContent;I)Z"))
    private boolean forge$getItems(ComponentScatteredFeatureJunglePyramid instance,
                                   World par1World,
                                   StructureBoundingBox par2StructureBoundingBox,
                                   Random par3Random,
                                   int par4,
                                   int par5,
                                   int par6,
                                   WeightedRandomChestContent[] par7ArrayOfWeightedRandomChestContent,
                                   int par8,
                                   Operation<Boolean> original,
                                   @Share(value = "chest", namespace = "fabricated-forge") LocalRef<ChestGenHooks> chestRef) {
        return original.call(instance, par1World, par2StructureBoundingBox, par3Random, par4, par5, par6, chestRef.get().getItems(), par8);
    }
}
