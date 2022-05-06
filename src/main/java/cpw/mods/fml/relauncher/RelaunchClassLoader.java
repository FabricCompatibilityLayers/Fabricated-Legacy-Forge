package cpw.mods.fml.relauncher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;

public class RelaunchClassLoader extends URLClassLoader {
    private static String[] excludedPackages = new String[0];
    private static String[] transformerExclusions = new String[0];
    private List<URL> sources;
    private ClassLoader parent;
    private List<IClassTransformer> transformers;
    private Map<String, Class> cachedClasses;
    private Set<String> classLoaderExceptions = new HashSet();
    private Set<String> transformerExceptions = new HashSet();

    public RelaunchClassLoader(URL[] sources) {
        super(sources, (ClassLoader) null);
        this.sources = new ArrayList(Arrays.asList(sources));
        this.parent = this.getClass().getClassLoader();
        this.cachedClasses = new HashMap(1000);
        this.transformers = new ArrayList(2);
        Thread.currentThread().setContextClassLoader(this);
        this.addClassLoaderExclusion("java.");
        this.addClassLoaderExclusion("sun.");
        this.addClassLoaderExclusion("");
        this.addClassLoaderExclusion("net.minecraftforge.classloading.");
        this.addTransformerExclusion("javax.");
        this.addTransformerExclusion("org.objectweb.asm.");
        this.addTransformerExclusion("com.google.common.");
        this.addTransformerExclusion("cpw.mods.fml.common.asm.SideOnly");
        this.addTransformerExclusion("cpw.mods.fml.common.Side");
    }

    public void registerTransformer(String transformerClassName) {
        try {
            this.transformers.add((IClassTransformer) this.loadClass(transformerClassName).newInstance());
        } catch (Exception var3) {
            FMLRelaunchLog.log(Level.SEVERE, var3, "A critical problem occured registering the ASM transformer class %s", new Object[]{transformerClassName});
        }

    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (excludedPackages.length != 0) {
            this.classLoaderExceptions.addAll(Arrays.asList(excludedPackages));
            excludedPackages = new String[0];
        }

        if (transformerExclusions.length != 0) {
            this.transformerExceptions.addAll(Arrays.asList(transformerExclusions));
            transformerExclusions = new String[0];
        }

        Iterator i$ = this.classLoaderExceptions.iterator();

        String pkgname;
        do {
            if (!i$.hasNext()) {
                if (this.cachedClasses.containsKey(name)) {
                    return (Class) this.cachedClasses.get(name);
                }

                i$ = this.transformerExceptions.iterator();

                do {
                    if (!i$.hasNext()) {
                        try {
                            int lastDot = name.lastIndexOf(46);
                            if (lastDot > -1) {
                                pkgname = name.substring(0, lastDot);
                                if (this.getPackage(pkgname) == null) {
                                    this.definePackage(pkgname, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (URL) null);
                                }
                            }

                            byte[] basicClass = this.getClassBytes(name);
                            byte[] transformedClass = this.runTransformers(name, basicClass);
                            Class<?> cl = this.defineClass(name, transformedClass, 0, transformedClass.length);
                            this.cachedClasses.put(name, cl);
                            return cl;
                        } catch (Throwable var6) {
                            throw new ClassNotFoundException(name, var6);
                        }
                    }

                    pkgname = (String) i$.next();
                } while (!name.startsWith(pkgname));

                Class<?> cl = super.findClass(name);
                this.cachedClasses.put(name, cl);
                return cl;
            }

            pkgname = (String) i$.next();
        } while (!name.startsWith(pkgname));

        return this.parent.loadClass(name);
    }

    public byte[] getClassBytes(String name) throws IOException {
        InputStream classStream = null;

        byte[] var4;
        try {
            URL classResource = this.findResource(name.replace('.', '/').concat(".class"));
            if (classResource == null) {
                Object var14 = null;
                return (byte[]) var14;
            }

            classStream = classResource.openStream();
            var4 = this.readFully(classStream);
        } finally {
            if (classStream != null) {
                try {
                    classStream.close();
                } catch (IOException var12) {
                }
            }

        }

        return var4;
    }

    private byte[] runTransformers(String name, byte[] basicClass) {
        IClassTransformer transformer;
        for (Iterator i$ = this.transformers.iterator(); i$.hasNext(); basicClass = transformer.transform(name, basicClass)) {
            transformer = (IClassTransformer) i$.next();
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
            while ((r = stream.read()) != -1) {
                bos.write(r);
            }

            return bos.toByteArray();
        } catch (Throwable var4) {
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
