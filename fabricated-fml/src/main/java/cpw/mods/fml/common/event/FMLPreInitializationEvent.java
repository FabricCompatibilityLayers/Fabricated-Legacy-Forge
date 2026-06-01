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
package cpw.mods.fml.common.event;

import java.io.File;
import java.util.Properties;

import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.discovery.ASMDataTable;

public class FMLPreInitializationEvent extends FMLStateEvent
{
    private ModMetadata modMetadata;
    private File sourceFile;
    private File configurationDir;
    private File suggestedConfigFile;
    private ASMDataTable asmData;
    private ModContainer modContainer;

    public FMLPreInitializationEvent(Object... data)
    {
        super(data);
        this.asmData = (ASMDataTable)data[0];
        this.configurationDir = (File)data[1];
    }

    @Override
    public ModState getModState()
    {
        return ModState.PREINITIALIZED;
    }

    @Override
    public void applyModContainer(ModContainer activeContainer)
    {
        this.modContainer = activeContainer;
        this.modMetadata = activeContainer.getMetadata();
        this.sourceFile = activeContainer.getSource();
        this.suggestedConfigFile = new File(configurationDir, activeContainer.getModId()+".cfg");
    }

    public File getSourceFile()
    {
        return sourceFile;
    }

    public ModMetadata getModMetadata()
    {
        return modMetadata;
    }

    public File getModConfigurationDirectory()
    {
        return configurationDir;
    }

    public File getSuggestedConfigurationFile()
    {
        return suggestedConfigFile;
    }

    public ASMDataTable getAsmData()
    {
        return asmData;
    }

    public Properties getVersionProperties()
    {
        if (this.modContainer instanceof FMLModContainer)
        {
            return ((FMLModContainer)this.modContainer).searchForVersionProperties();
        }

        return null;
    }
}
