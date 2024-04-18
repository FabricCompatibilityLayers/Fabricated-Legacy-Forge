package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.DelegatedTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.jar.JarFile;

@Mixin(DelegatedTransformer.class)
public class DelegatedTransformerMixin {
    @Inject(method = "addTransformer", at = @At("HEAD"), remap = false)
    private static void wtfDoesThisDo(String transformer, JarFile jar, File jarFile, CallbackInfo ci) {
        System.out.println(transformer + " -> " + jar.getName() + " -> " + jarFile.getName());
    }
}
