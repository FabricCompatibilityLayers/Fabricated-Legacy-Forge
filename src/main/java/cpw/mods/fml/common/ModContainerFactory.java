/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common;

import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.discovery.asm.ASMModParser;
import cpw.mods.fml.common.discovery.asm.ModAnnotation;
import cpw.mods.fml.common.modloader.ModLoaderModContainer;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Pattern;

public class ModContainerFactory {
    private static Pattern modClass = Pattern.compile(".*(\\.|)(mod\\_[^\\s$]+)$");
    private static ModContainerFactory INSTANCE = new ModContainerFactory();

    public ModContainerFactory() {
    }

    public static ModContainerFactory instance() {
        return INSTANCE;
    }

    public ModContainer build(ASMModParser modParser, File modSource, ModCandidate container) {
        String className = modParser.getASMType().getClassName();
        if (modParser.isBaseMod(container.getRememberedBaseMods()) && modClass.matcher(className).find()) {
            FMLLog.fine("Identified a BaseMod type mod %s", new Object[]{className});
            return new ModLoaderModContainer(className, modSource, modParser.getBaseModProperties());
        } else {
            if (modClass.matcher(className).find()) {
                FMLLog.fine(
                        "Identified a class %s following modloader naming convention but not directly a BaseMod or currently seen subclass",
                        new Object[]{className}
                );
                container.rememberModCandidateType(modParser);
            } else if (modParser.isBaseMod(container.getRememberedBaseMods())) {
                FMLLog.fine("Found a basemod %s of non-standard naming format", new Object[]{className});
                container.rememberBaseModType(className);
            }

            if (className.startsWith("net.minecraft.src.") && container.isClasspath() && !container.isMinecraftJar()) {
                FMLLog.severe(
                        "FML has detected a mod that is using a package name based on 'net.minecraft.src' : %s. This is generally a severe programming error.  There should be no mod code in the minecraft namespace. MOVE YOUR MOD! If you're in eclipse, select your source code and 'refactor' it into a new package. Go on. DO IT NOW!",
                        new Object[]{className}
                );
            }

            for(ModAnnotation ann : modParser.getAnnotations()) {
                if (ann.getASMType().equals(Type.getType(Mod.class))) {
                    FMLLog.fine("Identified an FMLMod type mod %s", new Object[]{className});
                    return new FMLModContainer(className, modSource, ann.getValues());
                }
            }

            return null;
        }
    }
}
