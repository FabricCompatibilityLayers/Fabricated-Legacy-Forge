/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.mixin.codechickencore;

import codechicken.core.ClassDiscoverer;
import codechicken.core.IStringMatcher;
import codechicken.core.asm.CodeChickenCorePlugin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import cpw.mods.fml.common.ModClassLoader;
import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.asm.ClassNodeHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipException;

@Mixin(ClassDiscoverer.class)
public abstract class ClassDiscovererMixin {
    @Shadow(remap = false) public ArrayList classes;
    @Shadow(remap = false) public ModClassLoader modClassLoader;
    @Unique
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

    @WrapMethod(method = "readFromZipFile")
    private void fixLwjgl3Crash(File file, Operation<Void> original) {
        try {
            original.call(file);
        } catch (Exception e) {
            if (e instanceof ZipException) {
                return;
            }
            throw e;
        }
    }

    /**
     * @author ChickenBones
     * Backported from CCC for MC 1.4.7
     */
    private void checkAddClass(String resource) {
        try {
            String classname = resource.replace(".class", "").replace("\\", ".").replace("/", ".");
            byte[] bytes = CodeChickenCorePlugin.cl.getClassBytes(classname);
            if (bytes == null) {
                return;
            }

            ClassNode cnode = ClassNodeHelper.createNode(bytes, 0);
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
