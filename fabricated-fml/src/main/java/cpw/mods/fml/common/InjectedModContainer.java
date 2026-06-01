/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

import java.io.File;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

public class InjectedModContainer implements ModContainer
{
    private File source;
    private ModContainer wrappedContainer;

    public InjectedModContainer(ModContainer mc, File source)
    {
        this.source = source;
        this.wrappedContainer = mc;
    }

    public String getModId()
    {
        return wrappedContainer.getModId();
    }

    public String getName()
    {
        return wrappedContainer.getName();
    }

    public String getVersion()
    {
        return wrappedContainer.getVersion();
    }

    public File getSource()
    {
        return source;
    }

    public ModMetadata getMetadata()
    {
        return wrappedContainer.getMetadata();
    }

    public void bindMetadata(MetadataCollection mc)
    {
        wrappedContainer.bindMetadata(mc);
    }

    public void setEnabledState(boolean enabled)
    {
        wrappedContainer.setEnabledState(enabled);
    }

    public Set<ArtifactVersion> getRequirements()
    {
        return wrappedContainer.getRequirements();
    }

    public List<ArtifactVersion> getDependencies()
    {
        return wrappedContainer.getDependencies();
    }

    public List<ArtifactVersion> getDependants()
    {
        return wrappedContainer.getDependants();
    }

    public String getSortingRules()
    {
        return wrappedContainer.getSortingRules();
    }

    public boolean registerBus(EventBus bus, LoadController controller)
    {
        return wrappedContainer.registerBus(bus, controller);
    }

    public boolean matches(Object mod)
    {
        return wrappedContainer.matches(mod);
    }

    public Object getMod()
    {
        return wrappedContainer.getMod();
    }

    public ArtifactVersion getProcessedVersion()
    {
        return wrappedContainer.getProcessedVersion();
    }

    @Override
    public boolean isNetworkMod()
    {
        return wrappedContainer.isNetworkMod();
    }
    @Override
    public boolean isImmutable()
    {
        return true;
    }

    @Override
    public String getDisplayVersion()
    {
        return wrappedContainer.getDisplayVersion();
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return wrappedContainer.acceptableMinecraftVersionRange();
    }

    public WorldAccessContainer getWrappedWorldAccessContainer()
    {
        if (wrappedContainer instanceof WorldAccessContainer)
        {
            return (WorldAccessContainer) wrappedContainer;
        }
        else
        {
            return null;
        }
    }
}
