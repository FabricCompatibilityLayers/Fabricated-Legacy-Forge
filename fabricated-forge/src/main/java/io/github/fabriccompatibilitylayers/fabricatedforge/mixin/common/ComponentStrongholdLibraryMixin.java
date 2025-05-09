package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.src.*;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static net.minecraftforge.common.ChestGenHooks.STRONGHOLD_LIBRARY;

@Mixin(ComponentStrongholdLibrary.class)
public class ComponentStrongholdLibraryMixin {
    @Inject(method = "addComponentParts", at = @At(value = "FIELD", target = "Lnet/minecraft/src/ComponentStrongholdLibrary;isLargeRoom:Z", ordinal = 3))
    private void forge$getChestGenHooks(World par2Random, Random par3StructureBoundingBox, StructureBoundingBox par3, CallbackInfoReturnable<Boolean> cir,
                                        @Share(value = "info", namespace = "fabricated-forge") LocalRef<ChestGenHooks> infoRef) {
        infoRef.set(ChestGenHooks.getInfo(STRONGHOLD_LIBRARY));
    }

    @Redirect(method = "addComponentParts", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int forge$getCount(Random instance, int i,
                               @Share(value = "info", namespace = "fabricated-forge") LocalRef<ChestGenHooks> infoRef) {
        return infoRef.get().getCount(instance) - 1;
    }

    @WrapOperation(method = "addComponentParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ComponentStrongholdLibrary;generateStructureChestContents(Lnet/minecraft/src/World;Lnet/minecraft/src/StructureBoundingBox;Ljava/util/Random;III[Lnet/minecraft/src/WeightedRandomChestContent;I)Z"))
    private boolean forge$getItems(ComponentStrongholdLibrary instance, World world, StructureBoundingBox structureBoundingBox, Random random, int i, int j, int k, WeightedRandomChestContent[] weightedRandomChestContents, int l, Operation<Boolean> original,
                                   @Share(value = "info", namespace = "fabricated-forge") LocalRef<ChestGenHooks> infoRef) {
        return original.call(instance, world, structureBoundingBox, random, i, j, k, infoRef.get().getItems(), l);
    }
}
