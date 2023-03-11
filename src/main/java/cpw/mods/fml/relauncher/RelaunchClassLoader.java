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

import java.net.*;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import fr.catcore.modremapperapi.ClassTransformer;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.util.UrlUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

public class RelaunchClassLoader extends URLClassLoader {
    // ClassLoaderFixer indication
    public static String FIXER_VERSION = "1";
    // Left behind for CCC/NEI compatibility
    private static String[] excludedPackages = new String[0];
    // Left behind for CCC/NEI compatibility
    private static String[] transformerExclusions = new String[0];
    private List<URL> sources;
    private ClassLoader parent;
    public static List<IClassTransformer> transformers;
    private Map<String, Class> cachedClasses;
    private Set<String> classLoaderExceptions = new HashSet<>();
    private Set<String> transformerExceptions = new HashSet<>();

    public RelaunchClassLoader() {
        super(new URL[0], FabricLauncherBase.getLauncher().getTargetClassLoader());
        this.sources = new ArrayList<>();
        this.parent = this.getClass().getClassLoader();
        this.cachedClasses = new HashMap<>(1000);
        this.transformers = new ArrayList<>(2);
//        ReflectionHelper.setPrivateValue(ClassLoader.class, null, this, "scl");
        Thread.currentThread().setContextClassLoader(this);

        // standard classloader exclusions
        this.addClassLoaderExclusion("java.");
        this.addClassLoaderExclusion("sun.");
        this.addClassLoaderExclusion("");
        this.addClassLoaderExclusion("net.minecraftforge.classloading.");

        // standard transformer exclusions
        this.addTransformerExclusion("javax.");
        this.addTransformerExclusion("org.objectweb.asm.");
        this.addTransformerExclusion("com.google.common.");
        this.addTransformerExclusion("cpw.mods.fml.common.asm.SideOnly");
        this.addTransformerExclusion("cpw.mods.fml.common.Side");
    }

    public void registerTransformer(String transformerClassName) {
        try {
            IClassTransformer classTransformer = (IClassTransformer) this.loadClass(transformerClassName).newInstance();
            ClassTransformer.registerTransformer(classTransformer);
            this.transformers.add(classTransformer);
        } catch (Exception var3) {
            FMLRelaunchLog.log(Level.SEVERE, var3, "A critical problem occurred registering the ASM transformer class %s", transformerClassName);
        }

    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // NEI/CCC compatibility code
        if (excludedPackages.length != 0)
        {
            classLoaderExceptions.addAll(Arrays.asList(excludedPackages));
            excludedPackages = new String[0];
        }
        if (transformerExclusions.length != 0)
        {
            transformerExceptions.addAll(Arrays.asList(transformerExclusions));
            transformerExclusions = new String[0];
        }

        for (String st : classLoaderExceptions)
        {
            if (name.startsWith(st))
            {
                return parent.loadClass(name);
            }
        }

        if (cachedClasses.containsKey(name))
        {
            return cachedClasses.get(name);
        }

        for (String st : transformerExceptions)
        {
            if (name.startsWith(st))
            {
                Class<?> cl = super.findClass(name);
                cachedClasses.put(name, cl);
                return cl;
            }
        }

        try
        {
            CodeSigner[] signers = null;
            int lastDot = name.lastIndexOf('.');
            String pkgname = lastDot == -1 ? "" : name.substring(0, lastDot);
            String fName = name.replace('.', '/').concat(".class");
            URLConnection urlConnection = findCodeSourceConnectionFor(fName);
            if (urlConnection instanceof JarURLConnection && lastDot > -1)
            {
                JarURLConnection jarUrlConn = (JarURLConnection)urlConnection;
                JarFile jf = jarUrlConn.getJarFile();
                if (jf != null && jf.getManifest() != null)
                {
                    Manifest mf = jf.getManifest();
                    JarEntry ent = jf.getJarEntry(fName);
                    Package pkg = getPackage(pkgname);
                    getClassBytes(name);
                    signers = ent.getCodeSigners();
                    if (pkg != null)
                    {
                        if (pkg.isSealed() && !pkg.isSealed(jarUrlConn.getJarFileURL()))
                        {
                            FMLRelaunchLog.severe("The jar file %s is trying to seal already secured path %s", jf.getName(), pkgname);
                        }
                        else if (isSealed(pkgname, mf))
                        {
                            FMLRelaunchLog.severe("The jar file %s has a security seal for path %s, but that path is defined and not secure", jf.getName(), pkgname);
                        }
                    }
                }
            }
            else if (lastDot > -1)
            {
                if (getPackage(pkgname)==null)
                {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            }
            byte[] basicClass = getClassBytes(name);
            byte[] transformedClass = runTransformers(name, basicClass);
            URL url = urlConnection == null ? null : urlConnection.getURL();
            Class<?> cl = defineClass(name, transformedClass, 0, transformedClass.length, new CodeSource(url, signers));
            cachedClasses.put(name, cl);
            return cl;
        }
        catch (Throwable e)
        {
            throw new ClassNotFoundException(name, e);
        }
    }

    private boolean isSealed(String path, Manifest man)
    {
        Attributes attr = man.getAttributes(path);
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }

    private URLConnection findCodeSourceConnectionFor(String name)
    {
        URL res = findResource(name);
        if (res != null)
        {
            try
            {
                return res.openConnection();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return null;
        }
    }

    public byte[] getClassBytes(String name) throws IOException {
        InputStream classStream = null;

        try {
            URL classResource = this.findResource(name.replace('.', '/').concat(".class"));
            if (classResource == null) {
                return null;
            }

            classStream = classResource.openStream();
            return readFully(classStream);
        } finally {
            if (classStream != null) {
                try {
                    classStream.close();
                } catch (IOException var12) {
                    // Swallow the close exception
                }
            }

        }
    }

    private byte[] runTransformers(String name, byte[] basicClass) {
        for (IClassTransformer transformer : transformers)
        {
            basicClass = transformer.transform(name, basicClass);
        }
        return basicClass;
    }

    public void addURL(URL url) {
        super.addURL(url);

        FabricLauncherBase.getLauncher().addToClassPath(UrlUtil.asPath(url));

        this.sources.add(url);
    }

    public List<URL> getSources() {
        return this.sources;
    }

    private byte[] readFully(InputStream stream) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(stream.available());

            int r;
            while ((r = stream.read()) != -1) {
                bos.write(r);
            }

            return bos.toByteArray();
        } catch (Throwable var4) {
            /// HMMM
            return new byte[0];
        }
    }

    public List<IClassTransformer> getTransformers() {
        return Collections.unmodifiableList(this.transformers);
    }

    private void addClassLoaderExclusion(String toExclude) {
        this.classLoaderExceptions.add(toExclude);
    }

    void addTransformerExclusion(String toExclude) {
        this.transformerExceptions.add(toExclude);
    }
}
