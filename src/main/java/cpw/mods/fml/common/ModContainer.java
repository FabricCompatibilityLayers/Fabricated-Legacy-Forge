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
import java.security.cert.Certificate;
import java.util.List;
import java.util.Set;

public interface ModContainer {
    String getModId();

    String getName();

    String getVersion();

    File getSource();

    ModMetadata getMetadata();

    void bindMetadata(MetadataCollection metadataCollection);

    void setEnabledState(boolean bl);

    Set<ArtifactVersion> getRequirements();

    List<ArtifactVersion> getDependencies();

    List<ArtifactVersion> getDependants();

    String getSortingRules();

    boolean registerBus(EventBus eventBus, LoadController loadController);

    boolean matches(Object object);

    Object getMod();

    ArtifactVersion getProcessedVersion();

    boolean isImmutable();

    boolean isNetworkMod();

    String getDisplayVersion();

    VersionRange acceptableMinecraftVersionRange();

    Certificate getSigningCertificate();
}
