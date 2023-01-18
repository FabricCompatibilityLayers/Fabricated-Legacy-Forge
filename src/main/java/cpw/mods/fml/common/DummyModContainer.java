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
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DummyModContainer implements ModContainer {
    private ModMetadata md;
    private ArtifactVersion processedVersion;

    public DummyModContainer(ModMetadata md) {
        this.md = md;
    }

    public DummyModContainer() {
    }

    public void bindMetadata(MetadataCollection mc) {
    }

    public List<ArtifactVersion> getDependants() {
        return Collections.emptyList();
    }

    public List<ArtifactVersion> getDependencies() {
        return Collections.emptyList();
    }

    public Set<ArtifactVersion> getRequirements() {
        return Collections.emptySet();
    }

    public ModMetadata getMetadata() {
        return this.md;
    }

    public Object getMod() {
        return null;
    }

    public String getModId() {
        return this.md.modId;
    }

    public String getName() {
        return this.md.name;
    }

    public String getSortingRules() {
        return "";
    }

    public File getSource() {
        return null;
    }

    public String getVersion() {
        return this.md.version;
    }

    public boolean matches(Object mod) {
        return false;
    }

    public void setEnabledState(boolean enabled) {
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        return false;
    }

    public ArtifactVersion getProcessedVersion() {
        if (this.processedVersion == null) {
            this.processedVersion = new DefaultArtifactVersion(this.getModId(), this.getVersion());
        }

        return this.processedVersion;
    }

    public boolean isImmutable() {
        return false;
    }

    public boolean isNetworkMod() {
        return false;
    }

    public String getDisplayVersion() {
        return this.md.version;
    }

    public VersionRange acceptableMinecraftVersionRange() {
        return Loader.instance().getMinecraftModContainer().getStaticVersionRange();
    }
}
