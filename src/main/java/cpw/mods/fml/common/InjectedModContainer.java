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


import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

import java.io.File;
import java.util.List;
import java.util.Set;

public class InjectedModContainer implements ModContainer {
    private File source;
    private ModContainer wrappedContainer;

    public InjectedModContainer(ModContainer mc, File source) {
        this.source = source;
        this.wrappedContainer = mc;
    }

    public String getModId() {
        return this.wrappedContainer.getModId();
    }

    public String getName() {
        return this.wrappedContainer.getName();
    }

    public String getVersion() {
        return this.wrappedContainer.getVersion();
    }

    public File getSource() {
        return this.source;
    }

    public ModMetadata getMetadata() {
        return this.wrappedContainer.getMetadata();
    }

    public void bindMetadata(MetadataCollection mc) {
        this.wrappedContainer.bindMetadata(mc);
    }

    public void setEnabledState(boolean enabled) {
        this.wrappedContainer.setEnabledState(enabled);
    }

    public Set<ArtifactVersion> getRequirements() {
        return this.wrappedContainer.getRequirements();
    }

    public List<ArtifactVersion> getDependencies() {
        return this.wrappedContainer.getDependencies();
    }

    public List<ArtifactVersion> getDependants() {
        return this.wrappedContainer.getDependants();
    }

    public String getSortingRules() {
        return this.wrappedContainer.getSortingRules();
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        return this.wrappedContainer.registerBus(bus, controller);
    }

    public boolean matches(Object mod) {
        return this.wrappedContainer.matches(mod);
    }

    public Object getMod() {
        return this.wrappedContainer.getMod();
    }

    public ArtifactVersion getProcessedVersion() {
        return this.wrappedContainer.getProcessedVersion();
    }

    public boolean isNetworkMod() {
        return this.wrappedContainer.isNetworkMod();
    }

    public boolean isImmutable() {
        return true;
    }

    public String getDisplayVersion() {
        return this.wrappedContainer.getDisplayVersion();
    }

    public VersionRange acceptableMinecraftVersionRange() {
        return this.wrappedContainer.acceptableMinecraftVersionRange();
    }

    public WorldAccessContainer getWrappedWorldAccessContainer() {
        return this.wrappedContainer instanceof WorldAccessContainer ? (WorldAccessContainer)this.wrappedContainer : null;
    }
}
