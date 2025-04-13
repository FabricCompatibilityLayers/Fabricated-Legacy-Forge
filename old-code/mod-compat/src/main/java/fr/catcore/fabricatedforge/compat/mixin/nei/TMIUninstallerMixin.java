package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.nei.TMIUninstaller;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(TMIUninstaller.class)
public class TMIUninstallerMixin {
    @Inject(method = "getJarFile", remap = false, at = @At("HEAD"), cancellable = true)
    private static void fixGetJar(CallbackInfoReturnable<File> cir) {
        cir.setReturnValue(((ModClassLoader) Loader.instance().getModClassLoader()).getParentSources()[0]);
    }
}
