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

import static net.minecraftforge.common.ChestGenHooks.MINESHAFT_CORRIDOR;

@Mixin(ComponentMineshaftCorridor.class)
public abstract class ComponentMineshaftCorridorMixin extends StructureComponent {
    protected ComponentMineshaftCorridorMixin(int par1) {
        super(par1);
    }

    @Inject(method = "addComponentParts", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false, ordinal = 1))
    private void forge$getChestGenHooks(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox, CallbackInfoReturnable<Boolean> cir,
                                        @Share(value = "info", namespace = "fabricated-forge") LocalRef<ChestGenHooks> infoRef) {
        infoRef.set(ChestGenHooks.getInfo(MINESHAFT_CORRIDOR));
    }

    @Redirect(method = "addComponentParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/StructureMineshaftPieces;func_78816_a()[Lnet/minecraft/src/WeightedRandomChestContent;"))
    private WeightedRandomChestContent[] forge$getItems(@Share(value = "info", namespace = "fabricated-forge") LocalRef<ChestGenHooks> infoRef) {
        return infoRef.get().getItems();
    }

    @WrapOperation(method = "addComponentParts", at = {
            @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false, ordinal = 2),
            @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false, ordinal = 4)
    })
    private int forge$getCount(Random instance, int i, Operation<Integer> original,
                               @Share(value = "info", namespace = "fabricated-forge") LocalRef<ChestGenHooks> infoRef) {
        return infoRef.get().getCount(instance) - 3;
    }
}
