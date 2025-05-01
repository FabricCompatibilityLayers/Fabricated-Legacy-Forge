package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntityFurnace.class)
public class TileEntityFurnaceMixin {
    @ModifyReturnValue(method = "func_70398_a", at = @At(value = "RETURN", ordinal = 10))
    private static int fml$getFuelValue(int original, @Local(argsOnly = true) ItemStack p_70398_0_) {
        if (original == 0) {
            return GameRegistry.getFuelValue(p_70398_0_);
        }

        return original;
    }
}
