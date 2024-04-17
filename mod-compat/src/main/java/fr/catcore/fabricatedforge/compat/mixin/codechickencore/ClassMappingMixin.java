package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ObfuscationMappings;
import fr.catcore.fabricatedforge.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationMappings.ClassMapping.class)
public class ClassMappingMixin {
    @Shadow(remap = false) public String s_class;

    @Inject(method = "<init>", remap = false, at = @At("RETURN"))
    private void remapClassName(String classname, CallbackInfo ci) {
        this.s_class = Constants.mapClass(this.s_class);
    }
}
