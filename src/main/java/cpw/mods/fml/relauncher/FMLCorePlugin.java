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
package cpw.mods.fml.relauncher;

import java.util.Map;

public class FMLCorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getLibraryRequestClass()
    {
        return new String[] {"cpw.mods.fml.relauncher.CoreFMLLibraries"};
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"cpw.mods.fml.common.asm.transformers.AccessTransformer",
                "cpw.mods.fml.common.asm.transformers.MarkerTransformer",
                "cpw.mods.fml.common.asm.transformers.SideTransformer"
        };
    }

    @Override
    public String getModContainerClass()
    {
        return "cpw.mods.fml.common.FMLDummyContainer";
    }

    @Override
    public String getSetupClass()
    {
        return "cpw.mods.fml.common.asm.FMLSanityChecker";
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        // don't care about this data
    }
}
