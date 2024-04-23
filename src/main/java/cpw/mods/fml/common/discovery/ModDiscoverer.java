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

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.RelaunchLibraryManager;
import fr.catcore.fabricatedforge.Constants;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModDiscoverer {
    private static Pattern zipJar = Pattern.compile("(.+).(zip|jar)$");
    private List<ModCandidate> candidates = Lists.newArrayList();
    private ASMDataTable dataTable = new ASMDataTable();
    private List<File> nonModLibs = Lists.newArrayList();

    public ModDiscoverer() {
    }

    public void findClasspathMods(ModClassLoader modClassLoader) {
        List<String> knownLibraries = ImmutableList.<String>builder()
                .addAll(modClassLoader.getDefaultLibraries())
                .addAll(RelaunchLibraryManager.getLibraries())
                .build();
        File[] minecraftSources = modClassLoader.getParentSources();
        if (minecraftSources.length == 1 && minecraftSources[0].isFile()) {
            FMLLog.fine("Minecraft is a file at %s, loading", new Object[]{minecraftSources[0].getAbsolutePath()});
            this.candidates.add(new ModCandidate(minecraftSources[0], minecraftSources[0], ContainerType.JAR, true, true));
        } else {
            for(int i = 0; i < minecraftSources.length; ++i) {
                if (minecraftSources[i].isFile()) {
                    if (knownLibraries.contains(minecraftSources[i].getName())) {
                        FMLLog.finer("Skipping known library file %s", new Object[]{minecraftSources[i].getAbsolutePath()});
                    } else {
                        if (!Objects.equals(minecraftSources[i].getParentFile().toString(), Constants.COREMODS_FOLDER.toString())) continue;
                        FMLLog.fine("Found a minecraft related file at %s, examining for mod candidates", new Object[]{minecraftSources[i].getAbsolutePath()});
                        this.candidates.add(new ModCandidate(minecraftSources[i], minecraftSources[i], ContainerType.JAR, i == 0, true));
                    }
                } else if (minecraftSources[i].isDirectory()) {
                    FMLLog.fine("Found a minecraft related directory at %s, examining for mod candidates", new Object[]{minecraftSources[i].getAbsolutePath()});
                    this.candidates.add(new ModCandidate(minecraftSources[i], minecraftSources[i], ContainerType.DIR, i == 0, true));
                }
            }
        }
    }

    public void findModDirMods(File modsDir) {
        File[] modList = modsDir.listFiles();
        Arrays.sort(modList);

        for(File modFile : modList) {
            if (modFile.isDirectory()) {
                FMLLog.fine("Found a candidate mod directory %s", new Object[]{modFile.getName()});
                this.candidates.add(new ModCandidate(modFile, modFile, ContainerType.DIR));
            } else {
                Matcher matcher = zipJar.matcher(modFile.getName());
                if (matcher.matches()) {
                    FMLLog.fine("Found a candidate zip or jar file %s", new Object[]{matcher.group(0)});
                    this.candidates.add(new ModCandidate(modFile, modFile, ContainerType.JAR));
                } else {
                    FMLLog.fine("Ignoring unknown file %s in mods directory", new Object[]{modFile.getName()});
                }
            }
        }
    }

    public List<ModContainer> identifyMods() {
        List<ModContainer> modList = Lists.newArrayList();

        for(ModCandidate candidate : this.candidates) {
            try {
                List<ModContainer> mods = candidate.explore(this.dataTable);
                if (mods.isEmpty() && !candidate.isClasspath()) {
                    this.nonModLibs.add(candidate.getModContainer());
                } else {
                    modList.addAll(mods);
                }
            } catch (LoaderException var5) {
                FMLLog.log(
                        Level.WARNING, var5, "Identified a problem with the mod candidate %s, ignoring this source", new Object[]{candidate.getModContainer()}
                );
            } catch (Throwable var6) {
                Throwables.propagate(var6);
            }
        }

        return modList;
    }

    public ASMDataTable getASMTable() {
        return this.dataTable;
    }

    public List<File> getNonModLibs() {
        return this.nonModLibs;
    }
}
