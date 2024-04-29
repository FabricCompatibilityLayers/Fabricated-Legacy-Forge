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

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.common.modloader.BaseModProxy;
import cpw.mods.fml.relauncher.RelaunchClassLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.util.UrlUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

public class ModClassLoader extends URLClassLoader {
    private static final List<String> STANDARD_LIBRARIES = ImmutableList.of("jinput.jar", "lwjgl.jar", "lwjgl_util.jar");

    private URL[] localUrls;
    public ModClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
        localUrls = new URL[0];
    }

    public List<String> getDefaultLibraries() {
        return Collections.emptyList();
    }

    @Override
    protected void addURL(URL url) {
        FabricLauncherBase.getLauncher().addToClassPath(UrlUtil.asPath(url));

        URL[] newLocalUrls = new URL[localUrls.length + 1];
        System.arraycopy(localUrls, 0, newLocalUrls, 0, localUrls.length);
        newLocalUrls[localUrls.length] = url;
        localUrls = newLocalUrls;
    }

    @Override
    public URL[] getURLs() {
        return localUrls;
    }

    @Override
    public URL findResource(final String name) {
        return getParent().getResource(name);
    }

    @Override
    public Enumeration<URL> findResources(final String name) throws IOException {
        return getParent().getResources(name);
    }

    /**
     * This is used to add mods to the classpath.
     * @param file The mod file.
     * @throws MalformedURLException If the File->URL transformation fails.
     */
    public void addFile(File file) throws MalformedURLException {
        try {
            addURL(UrlUtil.asUrl(file));
        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.getMessage());
        }
    }

    /**
     * This is used to find the Minecraft .JAR location.
     *
     * @return The "parent source" file.
     */
    public File getParentSource() {
        return ((MinecraftGameProvider) FabricLoaderImpl.INSTANCE.getGameProvider()).getGameJar().toFile();
    }

    /**
     * @return The "parent source" files array.
     */
    public File[] getParentSources() {
        ClassLoader loader = this.getClass().getClassLoader();

        while (loader != null && !(loader instanceof URLClassLoader)) {
            loader = loader.getParent();
        }

        if (loader != null) {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            List<File> files = new ArrayList<>();

            try {
                for (URL url : urls) {
                    files.add(new File(url.toURI()));
                }

                return files.toArray(new File[0]);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        System.err.println("Failed to get sources from Knot classloader");

        return new File[] { getParentSource() };
    }

    public Class<? extends BaseModProxy> loadBaseModClass(String modClazzName) throws Exception {
//        AccessTransformer transformer = (AccessTransformer)this.mainClassLoader.getTransformers().get(0);
//        transformer.ensurePublicAccessFor(modClazzName);
        return (Class<? extends BaseModProxy>) Class.forName(modClazzName, true, this);
    }
}
