/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.classloading;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class FMLForgePlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{ 
                                "net.minecraftforge.transformers.ForgeAccessTransformer",
                                "net.minecraftforge.transformers.EventTransformer"
                           };
    }

    @Override
    public String getModContainerClass()
    {
        return "net.minecraftforge.common.ForgeDummyContainer";
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
    }
}
