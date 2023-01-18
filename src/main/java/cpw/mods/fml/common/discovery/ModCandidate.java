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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.asm.ASMModParser;

import java.io.File;
import java.util.List;

public class ModCandidate {
    private File classPathRoot;
    private File modContainer;
    private ContainerType sourceType;
    private boolean classpath;
    private List<String> baseModTypes = Lists.newArrayList();
    private boolean isMinecraft;
    private List<ASMModParser> baseModCandidateTypes = Lists.newArrayListWithCapacity(1);

    public ModCandidate(File classPathRoot, File modContainer, ContainerType sourceType) {
        this(classPathRoot, modContainer, sourceType, false, false);
    }

    public ModCandidate(File classPathRoot, File modContainer, ContainerType sourceType, boolean isMinecraft, boolean classpath) {
        this.classPathRoot = classPathRoot;
        this.modContainer = modContainer;
        this.sourceType = sourceType;
        this.isMinecraft = isMinecraft;
        this.classpath = classpath;
    }

    public File getClassPathRoot() {
        return this.classPathRoot;
    }

    public File getModContainer() {
        return this.modContainer;
    }

    public ContainerType getSourceType() {
        return this.sourceType;
    }

    public List<ModContainer> explore(ASMDataTable table) {
        List<ModContainer> mods = this.sourceType.findMods(this, table);
        if (!this.baseModCandidateTypes.isEmpty()) {
            FMLLog.info("Attempting to reparse the mod container %s", new Object[]{this.getModContainer().getName()});
            return this.sourceType.findMods(this, table);
        } else {
            return mods;
        }
    }

    public boolean isClasspath() {
        return this.classpath;
    }

    public void rememberBaseModType(String className) {
        this.baseModTypes.add(className);
    }

    public List<String> getRememberedBaseMods() {
        return this.baseModTypes;
    }

    public boolean isMinecraftJar() {
        return this.isMinecraft;
    }

    public void rememberModCandidateType(ASMModParser modParser) {
        this.baseModCandidateTypes.add(modParser);
    }
}
