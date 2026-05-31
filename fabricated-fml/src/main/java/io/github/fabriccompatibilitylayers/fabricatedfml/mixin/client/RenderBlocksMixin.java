package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.Block;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(RenderBlocks.class)
public class RenderBlocksMixin {
    @Shadow public IBlockAccess field_78669_a;

    @Unique
    private static final List<Integer> VANILLA_MODEL_IDS = Arrays.asList(
            0, 31, 4, 13, 1, 19, 23, 6, 2, 3, 5, 8, 7, 9, 10, 27, 11,
            12, 29, 30, 14, 15, 16, 17, 18, 20, 21,
            24, 25, 26, 28
    );

    @ModifyReturnValue(method = "func_78612_b", at = @At("RETURN"))
    private boolean fml$renderWorldBlock(boolean original, Block p_78612_1_, int p_78612_2_, int p_78612_3_, int p_78612_4_, @Local(ordinal = 3) int var5) {
        if (!original) {
            if (VANILLA_MODEL_IDS.contains(var5)) {
                return false;
            } else {
                return FMLRenderAccessLibrary.renderWorldBlock((RenderBlocks) (Object) this, field_78669_a, p_78612_2_, p_78612_3_, p_78612_4_, p_78612_1_, var5);
            }
        }

        return true;
    }

    @Unique
    private static final List<Integer> VANILLA_ITEM_MODEL_IDS_INVENTORY = Arrays.asList(
            21, 11, 27, 10, 2, 6, 22, 13, 23, 19, 1, 0, 31, 16, 26
    );

    @WrapOperation(method = "func_78600_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;func_71857_b()I", ordinal = 0))
    private int fml$renderInventoryBlock(Block instance, Operation<Integer> original,
                                         @Local(ordinal = 0, argsOnly = true) int p_78600_2_,
                                         @Cancellable CallbackInfo ci) {
        int var14 = original.call(instance);

        if (!VANILLA_ITEM_MODEL_IDS_INVENTORY.contains(var14)) {
            FMLRenderAccessLibrary.renderInventoryBlock((RenderBlocks) (Object) this, instance, p_78600_2_, var14);
            ci.cancel();
        }

        return var14;
    }

    @Unique
    private static final List<Integer> VANILLA_ITEM_MODEL_IDS = Arrays.asList(
            0, 31, 13, 10, 11, 27, 22, 21, 16, 26
    );

    @ModifyReturnValue(method = "func_78597_b", at = @At("RETURN"))
    private static boolean fml$renderItemAsFull3DBlock(boolean original, int p_78597_0_) {
        if (!original && !VANILLA_ITEM_MODEL_IDS.contains(p_78597_0_)) {
            return FMLRenderAccessLibrary.renderItemAsFull3DBlock(p_78597_0_);
        }

        return original;
    }
}
