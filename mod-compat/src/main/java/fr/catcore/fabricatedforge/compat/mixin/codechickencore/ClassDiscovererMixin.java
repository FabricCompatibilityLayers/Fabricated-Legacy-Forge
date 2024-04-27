package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.ClassDiscoverer;
import codechicken.core.IStringMatcher;
import codechicken.core.asm.CodeChickenCorePlugin;
import cpw.mods.fml.common.ModClassLoader;
import fr.catcore.fabricatedforge.compat.CompatUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;

@Mixin(ClassDiscoverer.class)
public abstract class ClassDiscovererMixin {
    @Shadow public ArrayList classes;
    @Shadow public ModClassLoader modClassLoader;
    public String[] superclassesString;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void getStringSupers(IStringMatcher matcher, Class[] superclasses, CallbackInfo ci) {
        superclassesString = new String[superclasses.length];
        for (int i = 0; i < superclasses.length; i++) {
            superclassesString[i] = superclasses[i].getName().replace('.', '/');
        }
    }

    @Inject(method = "findModDirMods", cancellable = true, at = @At("HEAD"), remap = false)
    private void fixFindModDirMods(CallbackInfo ci) throws IOException {
        ci.cancel();
    }

    @Redirect(method = {"readFromZipFile", "readFromDirectory"}, at = @At(value = "INVOKE", target = "Lcodechicken/core/ClassDiscoverer;addClass(Ljava/lang/String;)V"), remap = false)
    private void checkClassBeforeLoadingIt(ClassDiscoverer instance, String resource) {
        checkAddClass(resource);
    }

    private void checkAddClass(String resource) {
        try {
            String classname = resource.replace(".class", "").replace("\\", ".").replace("/", ".");
            byte[] bytes = CodeChickenCorePlugin.cl.getClassBytes(classname);
            if (bytes == null) {
                return;
            }

            ClassNode cnode = CompatUtils.createNode(bytes, 0);
            String[] var8;
            int var7 = (var8 = this.superclassesString).length;

            for(int var6 = 0; var6 < var7; ++var6) {
                String superclass = var8[var6];
                if (!cnode.interfaces.contains(superclass) && !cnode.superName.equals(superclass)) {
                    return;
                }
            }

            this.addClass(classname);
        } catch (IOException var9) {
            IOException e = var9;
            System.err.println("Unable to load class: " + resource);
            e.printStackTrace();
        }

    }

    /**
     * @author ChickenBones
     * @reason more recent version of this method do less shit
     */
    @Overwrite(remap = false)
    private void addClass(String classname) {
        try {
            Class class1 = Class.forName(classname, true, this.modClassLoader);
            this.classes.add(class1);
        } catch (Exception var3) {
            Exception cnfe = var3;
            System.err.println("Unable to load class: " + classname);
            cnfe.printStackTrace();
        }

    }
}
