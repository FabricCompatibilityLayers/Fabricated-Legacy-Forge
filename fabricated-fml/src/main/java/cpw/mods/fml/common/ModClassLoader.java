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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.asm.ASMTransformer;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.common.modloader.BaseModProxy;
import cpw.mods.fml.relauncher.RelaunchClassLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

/**
 * A simple delegating class loader used to load mods into the system
 *
 *
 * @author cpw
 *
 */
public class ModClassLoader extends URLClassLoader
{
    public ModClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addFile(File modFile) throws MalformedURLException
    {
//            URL url = modFile.toURI().toURL();
//        mainClassLoader.addURL(url);

        FabricLauncherBase.getLauncher().addToClassPath(modFile.toPath());
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        return Class.forName(name, false, this.getParent());
//        return mainClassLoader.loadClass(name);
    }

    public File[] getParentSources() {
        ClassLoader loader = this.getClass().getClassLoader();

        while (loader != null && !(loader instanceof URLClassLoader)) {
            loader = loader.getParent();
        }

        URL[] urls = ((URLClassLoader) loader).getURLs();
        List<File> files = new ArrayList<>();

        try {
            for (URL url : urls) {
                try {
                    files.add(new File(url.toURI()));
                } catch (IllegalArgumentException ignored) {
                    System.err.println("Found non file url while getting sources from Knot classloader: " + url.toURI());
                }
            }

            return files.toArray(new File[0]);
        } catch (URISyntaxException e) {
            FMLLog.log(Level.SEVERE, "Unable to process our input to locate the minecraft code", e);
            throw new LoaderException(e);
        }

//        List<URL> urls=mainClassLoader.getSources();
//        File[] sources=new File[urls.size()];
//        try
//        {
//            for (int i = 0; i<urls.size(); i++)
//            {
//                sources[i]=new File(urls.get(i).toURI());
//            }
//            return sources;
//        }
//        catch (URISyntaxException e)
//        {
//            FMLLog.log(Level.SEVERE, "Unable to process our input to locate the minecraft code", e);
//            throw new LoaderException(e);
//        }
    }

    public List<String> getDefaultLibraries()
    {
        return Collections.emptyList();
    }

    public Class<? extends BaseModProxy> loadBaseModClass(String modClazzName) throws Exception
    {
        AccessTransformer transformer = (AccessTransformer)RelaunchClassLoader.transformers.get(0);
        transformer.ensurePublicAccessFor(modClazzName);
        return (Class<? extends BaseModProxy>) Class.forName(modClazzName, true, this);
    }
}
