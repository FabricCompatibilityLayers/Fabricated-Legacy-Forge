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
package cpw.mods.fml.common.discovery;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.discovery.asm.ASMModParser;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarDiscoverer implements ITypeDiscoverer {
    public JarDiscoverer() {
    }

    public List<ModContainer> discover(ModCandidate candidate, ASMDataTable table) {
        List<ModContainer> foundMods = Lists.newArrayList();
        FMLLog.fine("Examining file %s for potential mods", new Object[]{candidate.getModContainer().getName()});
        ZipFile jar = null;

        try {
            jar = new ZipFile(candidate.getModContainer());
            ZipEntry modInfo = jar.getEntry("mcmod.info");
            MetadataCollection mc = null;
            if (modInfo != null) {
                FMLLog.finer("Located mcmod.info file in file %s", new Object[]{candidate.getModContainer().getName()});
                mc = MetadataCollection.from(jar.getInputStream(modInfo), candidate.getModContainer().getName());
            } else {
                FMLLog.fine("The mod container %s appears to be missing an mcmod.info file", new Object[]{candidate.getModContainer().getName()});
                mc = MetadataCollection.from(null, "");
            }

            for(ZipEntry ze : Collections.list(jar.entries())) {
                if (ze.getName() == null || !ze.getName().startsWith("__MACOSX")) {
                    Matcher match = classFile.matcher(ze.getName());
                    if (match.matches()) {
                        ASMModParser modParser;
                        try {
                            modParser = new ASMModParser(jar.getInputStream(ze));
                        } catch (LoaderException var21) {
                            FMLLog.log(
                                    Level.SEVERE,
                                    var21,
                                    "There was a problem reading the entry %s in the jar %s - probably a corrupt zip",
                                    new Object[]{ze.getName(), candidate.getModContainer().getPath()}
                            );
                            jar.close();
                            throw var21;
                        }

                        modParser.validate();
                        modParser.sendToTable(table, candidate);
                        ModContainer container = ModContainerFactory.instance().build(modParser, candidate.getModContainer(), candidate);
                        if (container != null) {
                            table.addContainer(container);
                            foundMods.add(container);
                            container.bindMetadata(mc);
                        }
                    }
                }
            }
        } catch (Exception var22) {
            FMLLog.log(Level.WARNING, var22, "Zip file %s failed to read properly, it will be ignored", new Object[]{candidate.getModContainer().getName()});
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (Exception var20) {
                }
            }
        }

        return foundMods;
    }
}
