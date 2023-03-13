package fr.catcore.fabricatedforge.mixin.mods.codechickencore;

import codechicken.core.asm.ClassOverrider;
import codechicken.core.asm.ObfuscationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ClassOverrider.class)
public class ClassOverriderMixin {

    static {
        System.out.println("applied");
    }
    @Inject(method = "overrideBytes", at = @At("HEAD"), cancellable = true, remap = false)
    private static void ignoreOverwrite(String name, byte[] bytes, ObfuscationManager.ClassMapping classMapping, File location, CallbackInfoReturnable<byte[]> cir) {
        System.out.println(name + ":" + classMapping.classname);

        if (classMapping.classname.equals(name)) {
            System.out.println("[ClassOverrider] Canceled class overwrite of " + name);
        }

        cir.setReturnValue(bytes);
    }
}
