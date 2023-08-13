package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.ClassDiscoverer;
import fr.catcore.fabricatedforge.Constants;
import net.minecraft.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@Mixin(ClassDiscoverer.class)
public abstract class ClassDiscovererMixin {
    @Shadow(remap = false) protected abstract void readFromDirectory(File directory, File basedirectory);

    @Shadow(remap = false) protected abstract void readFromZipFile(File file) throws IOException;

    @Shadow(remap = false) protected abstract void findModDirMods() throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException;

    @Inject(method = "findModDirMods", cancellable = true, at = @At("HEAD"), remap = false)
    private void fixFindModDirMods(CallbackInfo ci) throws IOException {
        for(File child : new File[]{Constants.COREMODS_FOLDER, Constants.MODS_FOLDER}) {
            if (!child.isFile() || !child.getName().endsWith(".jar") && !child.getName().endsWith(".zip")) {
                if (child.isDirectory()) {
                    this.readFromDirectory(child, child);
                }
            } else {
                this.readFromZipFile(child);
            }
        }

        ci.cancel();
    }

    @Inject(method = "findClasses", at = @At("RETURN"), remap = false)
    private void lookForModsToo(CallbackInfoReturnable<ArrayList> cir) {
        try {
            this.findModDirMods();
        } catch (Exception var2) {
            ModLoader.throwException("Code Chicken Core", var2);
        }
    }
}
