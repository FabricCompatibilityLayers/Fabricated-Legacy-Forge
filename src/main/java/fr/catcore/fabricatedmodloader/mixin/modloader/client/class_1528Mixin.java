package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import modloader.ModLoader;
import net.minecraft.client.class_1528;
import net.minecraft.client.class_584;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(class_1528.class)
public class class_1528Mixin {

    @Shadow
    private float field_5970;

    @Shadow
    private float field_5972;

    @Shadow
    private float field_5971;

    @Shadow
    private float field_5973;

    @Inject(method = "method_5195", cancellable = true, at = @At("HEAD"))
    private static void modLoaderGetCustomAnimationLogic(String string, CallbackInfoReturnable<class_1528> cir) {
        class_1528 texture = ModLoader.getCustomAnimationLogic(string);
        if (texture != null) {
            cir.setReturnValue(texture);
        }
    }

    @Inject(method = "method_5193", at = @At("RETURN"))
    private void modLoader$method_5193(class_584 list, List i, int j, int k, int l, int bl, boolean par7, CallbackInfo ci) {
        this.field_5970 = this.field_5970 + 1.0E-4F;
        this.field_5971 = this.field_5971 - 1.0E-4F;
        this.field_5972 = this.field_5972 + 1.0E-4F;
        this.field_5973 = this.field_5973 - 1.0E-4F;
    }
}
