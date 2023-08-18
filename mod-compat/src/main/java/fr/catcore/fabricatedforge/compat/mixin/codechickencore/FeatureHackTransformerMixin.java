package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.FeatureHackTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FeatureHackTransformer.class)
public class FeatureHackTransformerMixin {
    @ModifyArg(method = "<init>", index = 2,
            remap = false,
            at = @At(value = "INVOKE",
                    remap = false,
                    target = "Lcodechicken/core/asm/ObfuscationManager$MethodMapping;<init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
    private String fixMethodArg(String arg) {
        return "(Lnet/minecraft/class_1069;)V";
    }
}
