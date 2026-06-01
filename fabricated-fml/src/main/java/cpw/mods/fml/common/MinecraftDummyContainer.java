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

import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class MinecraftDummyContainer extends DummyModContainer
{

    private VersionRange staticRange;
    public MinecraftDummyContainer(String actualMCVersion)
    {
        super(new ModMetadata());
        getMetadata().modId = "Minecraft";
        getMetadata().name = "Minecraft";
        getMetadata().version = actualMCVersion;
        staticRange = VersionParser.parseRange("["+actualMCVersion+"]");
    }


    public VersionRange getStaticVersionRange()
    {
        return staticRange;
    }
}
