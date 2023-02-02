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

import cpw.mods.fml.common.FMLLog;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;

public class RelaunchClassLoader extends URLClassLoader {
    private List<URL> sources;
    private ClassLoader parent;
    private List<IClassTransformer> transformers;
    private Map<String, Class> cachedClasses;
    private Set<String> invalidClasses;
    private Set<String> classLoaderExceptions = new HashSet();
    private Set<String> transformerExceptions = new HashSet();
    private Map<Package, Manifest> packageManifests = new HashMap();
    private static Manifest EMPTY = new Manifest();
    private static final String[] RESERVED = new String[]{
            "CON",
            "PRN",
            "AUX",
            "NUL",
            "COM1",
            "COM2",
            "COM3",
            "COM4",
            "COM5",
            "COM6",
            "COM7",
            "COM8",
            "COM9",
            "LPT1",
            "LPT2",
            "LPT3",
            "LPT4",
            "LPT5",
            "LPT6",
            "LPT7",
            "LPT8",
            "LPT9"
    };
    private static final boolean DEBUG_CLASSLOADING = Boolean.parseBoolean(System.getProperty("fml.debugClassLoading", "false"));

    public RelaunchClassLoader() {
        super(new URL[0], FabricLauncherBase.getLauncher().getTargetClassLoader());
        this.sources = new ArrayList<>();
        this.parent = this.getClass().getClassLoader();
        this.cachedClasses = new HashMap<>(1000);
        this.invalidClasses = new HashSet<>(1000);
        this.transformers = new ArrayList<>(2);
        Thread.currentThread().setContextClassLoader(this);
        this.addClassLoaderExclusion("java.");
        this.addClassLoaderExclusion("sun.");
        this.addClassLoaderExclusion("org.lwjgl.");
        this.addClassLoaderExclusion("cpw.mods.fml.relauncher.");
        this.addClassLoaderExclusion("net.minecraftforge.classloading.");
        this.addTransformerExclusion("javax.");
        this.addTransformerExclusion("org.objectweb.asm.");
        this.addTransformerExclusion("com.google.common.");
    }

    public void registerTransformer(String transformerClassName) {
        try {
            this.transformers.add((IClassTransformer)this.loadClass(transformerClassName).newInstance());
        } catch (Exception var3) {
            FMLRelaunchLog.log(Level.SEVERE, var3, "A critical problem occured registering the ASM transformer class %s", new Object[]{transformerClassName});
        }
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (this.invalidClasses.contains(name)) {
            throw new ClassNotFoundException(name);
        } else {
            for(String st : this.classLoaderExceptions) {
                if (name.startsWith(st)) {
                    return this.parent.loadClass(name);
                }
            }

            if (this.cachedClasses.containsKey(name)) {
                return (Class<?>)this.cachedClasses.get(name);
            } else {
                for(String st : this.transformerExceptions) {
                    if (name.startsWith(st)) {
                        try {
                            Class<?> cl = super.findClass(name);
                            this.cachedClasses.put(name, cl);
                            return cl;
                        } catch (ClassNotFoundException var13) {
                            this.invalidClasses.add(name);
                            throw var13;
                        }
                    }
                }

                try {
                    CodeSigner[] signers = null;
                    int lastDot = name.lastIndexOf(46);
                    String pkgname = lastDot == -1 ? "" : name.substring(0, lastDot);
                    String fName = name.replace('.', '/').concat(".class");
                    String pkgPath = pkgname.replace('.', '/');
                    URLConnection urlConnection = this.findCodeSourceConnectionFor(fName);
                    if (urlConnection instanceof JarURLConnection && lastDot > -1) {
                        JarURLConnection jarUrlConn = (JarURLConnection)urlConnection;
                        JarFile jf = jarUrlConn.getJarFile();
                        if (jf != null && jf.getManifest() != null) {
                            Manifest mf = jf.getManifest();
                            JarEntry ent = jf.getJarEntry(fName);
                            Package pkg = this.getPackage(pkgname);
                            this.getClassBytes(name);
                            signers = ent.getCodeSigners();
                            if (pkg == null) {
                                pkg = this.definePackage(pkgname, mf, jarUrlConn.getJarFileURL());
                                this.packageManifests.put(pkg, mf);
                            } else if (pkg.isSealed() && !pkg.isSealed(jarUrlConn.getJarFileURL())) {
                                FMLLog.severe("The jar file %s is trying to seal already secured path %s", new Object[]{jf.getName(), pkgname});
                            } else if (this.isSealed(pkgname, mf)) {
                                FMLLog.severe(
                                        "The jar file %s has a security seal for path %s, but that path is defined and not secure",
                                        new Object[]{jf.getName(), pkgname}
                                );
                            }
                        }
                    } else if (lastDot > -1) {
                        Package pkg = this.getPackage(pkgname);
                        if (pkg == null) {
                            pkg = this.definePackage(pkgname, null, null, null, null, null, null, null);
                            this.packageManifests.put(pkg, EMPTY);
                        } else if (pkg.isSealed()) {
                            FMLLog.severe("The URL %s is defining elements for sealed path %s", new Object[]{urlConnection.getURL(), pkgname});
                        }
                    }

                    byte[] basicClass = this.getClassBytes(name);
                    byte[] transformedClass = this.runTransformers(name, basicClass);
                    Class<?> cl = this.defineClass(name, transformedClass, 0, transformedClass.length, new CodeSource(urlConnection.getURL(), signers));
                    this.cachedClasses.put(name, cl);
                    return cl;
                } catch (Throwable var14) {
                    this.invalidClasses.add(name);
                    if (DEBUG_CLASSLOADING) {
                        FMLLog.log(Level.FINEST, var14, "Exception encountered attempting classloading of %s", new Object[]{name});
                    }

                    throw new ClassNotFoundException(name, var14);
                }
            }
        }
    }

    private boolean isSealed(String path, Manifest man) {
        Attributes attr = man.getAttributes(path);
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }

        if (sealed == null && (attr = man.getMainAttributes()) != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }

        return "true".equalsIgnoreCase(sealed);
    }

    private URLConnection findCodeSourceConnectionFor(String name) {
        URL res = this.findResource(name);
        if (res != null) {
            try {
                return res.openConnection();
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        } else {
            return null;
        }
    }

    private byte[] runTransformers(String name, byte[] basicClass) {
        for(IClassTransformer transformer : this.transformers) {
            basicClass = transformer.transform(name, basicClass);
        }

        return basicClass;
    }

    public void addURL(URL url) {
        super.addURL(url);
        this.sources.add(url);
    }

    public List<URL> getSources() {
        return this.sources;
    }

    private byte[] readFully(InputStream stream) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(stream.available());

            int r;
            while((r = stream.read()) != -1) {
                bos.write(r);
            }

            return bos.toByteArray();
        } catch (Throwable var4) {
            FMLLog.log(Level.WARNING, var4, "Problem loading class", new Object[0]);
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

    public byte[] getClassBytes(String name) throws IOException {
        if (name.indexOf(46) == -1) {
            for(String res : RESERVED) {
                if (name.toUpperCase(Locale.ENGLISH).startsWith(res)) {
                    byte[] data = this.getClassBytes("_" + name);
                    if (data != null) {
                        return data;
                    }
                }
            }
        }

        InputStream classStream = null;

        Object i$;
        try {
            URL classResource = this.findResource(name.replace('.', '/').concat(".class"));
            if (classResource != null) {
                classStream = classResource.openStream();
                if (DEBUG_CLASSLOADING) {
                    FMLLog.finest("Loading class %s from resource %s", new Object[]{name, classResource.toString()});
                }

                return this.readFully(classStream);
            }

            if (DEBUG_CLASSLOADING) {
                FMLLog.finest("Failed to find class resource %s", new Object[]{name.replace('.', '/').concat(".class")});
            }

            i$ = null;
        } finally {
            if (classStream != null) {
                try {
                    classStream.close();
                } catch (IOException var13) {
                }
            }
        }

        return (byte[])i$;
    }
}
