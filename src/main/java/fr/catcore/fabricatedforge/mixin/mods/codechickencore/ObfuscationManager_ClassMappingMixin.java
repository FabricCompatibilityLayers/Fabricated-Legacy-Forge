package fr.catcore.fabricatedforge.mixin.mods.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import fr.catcore.fabricatedforge.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationManager.ClassMapping.class)
public class ObfuscationManager_ClassMappingMixin {
    @Shadow(remap = false) public String classname;

    static {
        System.out.println("applied");
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void remap(String classname, CallbackInfo ci) {
        this.classname = Constants.getRemappedClassName(this.classname);
    }
}
