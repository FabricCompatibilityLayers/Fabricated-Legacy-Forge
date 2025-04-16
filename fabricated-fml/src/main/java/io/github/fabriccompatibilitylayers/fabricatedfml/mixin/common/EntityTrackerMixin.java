package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTracker.class)
public class EntityTrackerMixin {
    @Inject(method = "func_72786_a", at = @At("HEAD"), cancellable = true)
    private void fml$tryTrackingEntity(Entity p_72786_1_, CallbackInfo ci) {
        if (EntityRegistry.instance().tryTrackingEntity((EntityTracker)(Object) this, p_72786_1_))
        {
            ci.cancel();
        }
    }
}
