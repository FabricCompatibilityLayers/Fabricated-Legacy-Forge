package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.FMLWorldTypeExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class WorldMixin {
    @Shadow protected WorldInfo field_72986_A;

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "func_72919_O", at = @At("RETURN"))
    private double fml$getHorizon(double original) {
        if (original == 0.0D || original == 63.0D) {
            return ((FMLWorldTypeExtension) this.field_72986_A.func_76067_t()).getHorizon((World)(Object) this);
        }

        return original;
    }
}
