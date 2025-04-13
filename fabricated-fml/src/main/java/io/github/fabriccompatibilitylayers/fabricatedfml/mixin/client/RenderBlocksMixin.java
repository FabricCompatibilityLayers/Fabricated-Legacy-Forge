package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.Block;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Inject(method = "func_78600_a", at = @At("RETURN"))
    private void fml$renderInventoryBlock(Block p_78600_1_, int p_78600_2_, float p_78600_3_, CallbackInfo ci, @Local(ordinal = 1) int var14) {
        if (!VANILLA_MODEL_IDS.contains(var14)) {
            FMLRenderAccessLibrary.renderInventoryBlock((RenderBlocks) (Object) this, p_78600_1_, p_78600_2_, var14);
        }
    }

    @ModifyReturnValue(method = "func_78597_b", at = @At("RETURN"))
    private static boolean fml$renderItemAsFull3DBlock(boolean original, int p_78597_0_) {
        if (!original && !VANILLA_MODEL_IDS.contains(p_78597_0_)) {
            return FMLRenderAccessLibrary.renderItemAsFull3DBlock(p_78597_0_);
        }

        return original;
    }
}
