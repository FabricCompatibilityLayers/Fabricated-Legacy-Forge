package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import modloader.ModLoader;
import net.minecraft.client.class_1528;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1528.class)
public class class_1528Mixin {

    @Inject(method = "method_5195", cancellable = true, at = @At("HEAD"))
    private static void modLoaderGetCustomAnimationLogic(String string, CallbackInfoReturnable<class_1528> cir) {
        class_1528 texture = ModLoader.getCustomAnimationLogic(string);
        if (texture != null) {
            cir.setReturnValue(texture);
        }
    }
}
