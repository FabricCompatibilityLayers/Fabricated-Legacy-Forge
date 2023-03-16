package fr.catcore.fabricatedforge.compat;

import codechicken.core.asm.ObfuscationManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;

import java.io.File;

public class CodeChickenCoreCompat {
    public static byte[] overrideBytes(String name, byte[] bytes, ObfuscationManager.ClassMapping classMapping, File location) {
        if (classMapping.classname.equals(name)) {
            System.out.println("[ClassOverrider] Canceled class overwrite of " + name);
        }

        return bytes;
    }

    public static File getJarFile() {
        return ((ModClassLoader) Loader.instance().getModClassLoader()).getParentSources()[0];
    }
}
