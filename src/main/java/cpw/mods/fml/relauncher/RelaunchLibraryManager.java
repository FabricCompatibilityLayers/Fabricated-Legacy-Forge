package cpw.mods.fml.relauncher;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class RelaunchLibraryManager {
    private static String[] rootPlugins = new String[]{"cpw.mods.fml.relauncher.FMLCorePlugin", "net.minecraftforge.classloading.FMLForgePlugin"};
    private static List<String> loadedLibraries = new ArrayList();
    private static Map<IFMLLoadingPlugin, File> pluginLocations;
    private static List<IFMLLoadingPlugin> loadPlugins;
    private static List<ILibrarySet> libraries;
    private static final String HEXES = "0123456789abcdef";
    private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(4194304);
    static IDownloadDisplay downloadMonitor;

    public RelaunchLibraryManager() {
    }

    public static void handleLaunch(File mcDir, RelaunchClassLoader actualClassLoader) {
        pluginLocations = new HashMap();
        loadPlugins = new ArrayList();
        libraries = new ArrayList();
        String[] arr$ = rootPlugins;
        int len$1 = arr$.length;

        IFMLLoadingPlugin plugin;
        int i$;
        String targFileName;
        int len$;
        for (len$ = 0; len$ < len$1; ++len$) {
            String s = arr$[len$];

            try {
                plugin = (IFMLLoadingPlugin) Class.forName(s, true, actualClassLoader).newInstance();
                loadPlugins.add(plugin);
                String[] arr$1 = plugin.getLibraryRequestClass();
                i$ = arr$1.length;

                for (len$ = 0; len$ < i$; ++len$) {
                    targFileName = arr$1[len$];
                    libraries.add((ILibrarySet) Class.forName(targFileName, true, actualClassLoader).newInstance());
                }
            } catch (Exception var42) {
            }
        }

        if (loadPlugins.isEmpty()) {
            throw new RuntimeException("A fatal error has occured - no valid fml load plugin was found - this is a completely corrupt FML installation.");
        } else {
            downloadMonitor.updateProgressString("All core mods are successfully located", new Object[0]);
            String commandLineCoremods = System.getProperty("fml.coreMods.load", "");
            String[] arr$1 = commandLineCoremods.split(",");
            len$ = arr$1.length;

            for (i$ = 0; i$ < len$; ++i$) {
                String s = arr$1[i$];
                if (!s.isEmpty()) {
                    FMLRelaunchLog.info("Found a command line coremod : %s", new Object[]{s});

                    try {
                        actualClassLoader.addTransformerExclusion(s);
                        Class<?> coreModClass = Class.forName(s, true, actualClassLoader);
                        IFMLLoadingPlugin.TransformerExclusions trExclusions = (IFMLLoadingPlugin.TransformerExclusions) coreModClass.getAnnotation(IFMLLoadingPlugin.TransformerExclusions.class);

                        if (trExclusions != null) {
                            arr$ = trExclusions.value();
                            i$ = arr$.length;

                            for (len$ = 0; len$ < i$; ++len$) {
                                String st = arr$[len$];
                                actualClassLoader.addTransformerExclusion(st);
                            }
                        }

                        plugin = (IFMLLoadingPlugin) coreModClass.newInstance();
                        loadPlugins.add(plugin);
                        if (plugin.getLibraryRequestClass() != null) {
                            arr$ = plugin.getLibraryRequestClass();
                            len$ = arr$.length;

                            for (i$ = 0; i$ < len$; ++i$) {
                                String libName = arr$[i$];
                                libraries.add((ILibrarySet) Class.forName(libName, true, actualClassLoader).newInstance());
                            }
                        }
                    } catch (Throwable var41) {
                        FMLRelaunchLog.log(Level.SEVERE, var41, "Exception occured trying to load coremod %s", new Object[]{s});
                        throw new RuntimeException(var41);
                    }
                }
            }

            discoverCoreMods(mcDir, actualClassLoader, loadPlugins, libraries);
            ArrayList caughtErrors = new ArrayList();
            boolean var33 = false;

            Iterator it$;
            String xformClass;
            label674:
            {
                String checksum;
                try {
                    label675:
                    {
                        File libDir;
                        try {
                            var33 = true;
                            libDir = setupLibDir(mcDir);
                        } catch (Exception var39) {
                            caughtErrors.add(var39);
                            var33 = false;
                            break label675;
                        }

                        it$ = libraries.iterator();

                        while (it$.hasNext()) {
                            ILibrarySet lib = (ILibrarySet) it$.next();

                            for (len$ = 0; len$ < lib.getLibraries().length; ++len$) {
                                boolean download = false;
                                xformClass = lib.getLibraries()[len$];
                                targFileName = xformClass.lastIndexOf(47) >= 0 ? xformClass.substring(xformClass.lastIndexOf(47)) : xformClass;
                                checksum = lib.getHashes()[len$];
                                File libFile = new File(libDir, targFileName);
                                if (!libFile.exists()) {
                                    try {
                                        downloadFile(libFile, lib.getRootURL(), xformClass, checksum);
                                        download = true;
                                    } catch (Throwable var38) {
                                        caughtErrors.add(var38);
                                        continue;
                                    }
                                }

                                if (libFile.exists() && !libFile.isFile()) {
                                    caughtErrors.add(new RuntimeException(String.format("Found a file %s that is not a normal file - you should clear this out of the way", xformClass)));
                                } else {
                                    if (!download) {
                                        try {
                                            FileInputStream fis = new FileInputStream(libFile);
                                            FileChannel chan = fis.getChannel();
                                            MappedByteBuffer mappedFile = chan.map(FileChannel.MapMode.READ_ONLY, 0L, libFile.length());
                                            String fileChecksum = generateChecksum(mappedFile);
                                            fis.close();
                                            if (!checksum.equals(fileChecksum)) {
                                                caughtErrors.add(new RuntimeException(String.format("The file %s was found in your lib directory and has an invalid checksum %s (expecting %s) - it is unlikely to be the correct download, please move it out of the way and try again.", xformClass, fileChecksum, checksum)));
                                                continue;
                                            }
                                        } catch (Exception var37) {
                                            FMLRelaunchLog.log(Level.SEVERE, var37, "The library file %s could not be validated", new Object[]{libFile.getName()});
                                            caughtErrors.add(new RuntimeException(String.format("The library file %s could not be validated", libFile.getName()), var37));
                                            continue;
                                        }
                                    }

                                    if (!download) {
                                        downloadMonitor.updateProgressString("Found library file %s present and correct in lib dir\n", new Object[]{xformClass});
                                    } else {
                                        downloadMonitor.updateProgressString("Library file %s was downloaded and verified successfully\n", new Object[]{xformClass});
                                    }

                                    try {
                                        actualClassLoader.addURL(libFile.toURI().toURL());
                                        loadedLibraries.add(xformClass);
                                    } catch (MalformedURLException var36) {
                                        caughtErrors.add(new RuntimeException(String.format("Should never happen - %s is broken - probably a somehow corrupted download. Delete it and try again.", libFile.getName()), var36));
                                    }
                                }
                            }
                        }

                        var33 = false;
                        break label674;
                    }
                } finally {
                    if (var33) {
                        if (downloadMonitor.shouldStopIt()) {
                            return;
                        }

                        if (!caughtErrors.isEmpty()) {
                            FMLRelaunchLog.severe("There were errors during initial FML setup. Some files failed to download or were otherwise corrupted. You will need to manually obtain the following files from these download links and ensure your lib directory is clean. ", new Object[0]);
                            Iterator it$2 = libraries.iterator();

                            while (it$2.hasNext()) {
                                ILibrarySet set = (ILibrarySet) it$2.next();
                                String[] arr$2 = set.getLibraries();
                                int len$2 = arr$2.length;

                                for (int i$3 = 0; i$3 < len$2; ++i$3) {
                                    String file = arr$2[i$3];
                                    FMLRelaunchLog.severe("*** Download " + set.getRootURL(), new Object[]{file});
                                }
                            }

                            FMLRelaunchLog.severe("<===========>", new Object[0]);
                            FMLRelaunchLog.severe("The following is the errors that caused the setup to fail. They may help you diagnose and resolve the issue", new Object[0]);
                            it$2 = caughtErrors.iterator();

                            Throwable t;
                            while (it$2.hasNext()) {
                                t = (Throwable) it$2.next();
                                if (t.getMessage() != null) {
                                    FMLRelaunchLog.severe(t.getMessage(), new Object[0]);
                                }
                            }

                            FMLRelaunchLog.severe("<<< ==== >>>", new Object[0]);
                            FMLRelaunchLog.severe("The following is diagnostic information for developers to review.", new Object[0]);
                            it$2 = caughtErrors.iterator();

                            while (it$2.hasNext()) {
                                t = (Throwable) it$2.next();
                                FMLRelaunchLog.log(Level.SEVERE, t, "Error details", new Object[0]);
                            }

                            throw new RuntimeException("A fatal error occured and FML cannot continue");
                        }

                    }
                }

                if (downloadMonitor.shouldStopIt()) {
                    return;
                }

                if (caughtErrors.isEmpty()) {
                    return;
                }

                FMLRelaunchLog.severe("There were errors during initial FML setup. Some files failed to download or were otherwise corrupted. You will need to manually obtain the following files from these download links and ensure your lib directory is clean. ", new Object[0]);
                Iterator i$5 = libraries.iterator();

                while (i$5.hasNext()) {
                    ILibrarySet set = (ILibrarySet) i$5.next();
                    String[] arr$6 = set.getLibraries();
                    len$ = arr$6.length;

                    for (int i$6 = 0; i$6 < len$; ++i$6) {
                        checksum = arr$6[i$6];
                        FMLRelaunchLog.severe("*** Download " + set.getRootURL(), new Object[]{checksum});
                    }
                }

                FMLRelaunchLog.severe("<===========>", new Object[0]);
                FMLRelaunchLog.severe("The following is the errors that caused the setup to fail. They may help you diagnose and resolve the issue", new Object[0]);
                i$5 = caughtErrors.iterator();

                Throwable t;
                while (i$5.hasNext()) {
                    t = (Throwable) i$5.next();
                    if (t.getMessage() != null) {
                        FMLRelaunchLog.severe(t.getMessage(), new Object[0]);
                    }
                }

                FMLRelaunchLog.severe("<<< ==== >>>", new Object[0]);
                FMLRelaunchLog.severe("The following is diagnostic information for developers to review.", new Object[0]);
                i$5 = caughtErrors.iterator();

                while (i$5.hasNext()) {
                    t = (Throwable) i$5.next();
                    FMLRelaunchLog.log(Level.SEVERE, t, "Error details", new Object[0]);
                }

                throw new RuntimeException("A fatal error occured and FML cannot continue");
            }

            if (!downloadMonitor.shouldStopIt()) {
                Iterator i$7;
                String[] arr$8;
                if (!caughtErrors.isEmpty()) {
                    FMLRelaunchLog.severe("There were errors during initial FML setup. Some files failed to download or were otherwise corrupted. You will need to manually obtain the following files from these download links and ensure your lib directory is clean. ", new Object[0]);
                    i$7 = libraries.iterator();

                    while (i$7.hasNext()) {
                        ILibrarySet set = (ILibrarySet) i$7.next();
                        arr$8 = set.getLibraries();
                        len$ = arr$8.length;

                        for (int i$9 = 0; i$9 < len$; ++i$9) {
                            xformClass = arr$8[i$9];
                            FMLRelaunchLog.severe("*** Download " + set.getRootURL(), new Object[]{xformClass});
                        }
                    }

                    FMLRelaunchLog.severe("<===========>", new Object[0]);
                    FMLRelaunchLog.severe("The following is the errors that caused the setup to fail. They may help you diagnose and resolve the issue", new Object[0]);
                    i$7 = caughtErrors.iterator();

                    Throwable t;
                    while (i$7.hasNext()) {
                        t = (Throwable) i$7.next();
                        if (t.getMessage() != null) {
                            FMLRelaunchLog.severe(t.getMessage(), new Object[0]);
                        }
                    }

                    FMLRelaunchLog.severe("<<< ==== >>>", new Object[0]);
                    FMLRelaunchLog.severe("The following is diagnostic information for developers to review.", new Object[0]);
                    i$7 = caughtErrors.iterator();

                    while (i$7.hasNext()) {
                        t = (Throwable) i$7.next();
                        FMLRelaunchLog.log(Level.SEVERE, t, "Error details", new Object[0]);
                    }

                    throw new RuntimeException("A fatal error occured and FML cannot continue");
                } else {
                    i$7 = loadPlugins.iterator();

                    while (true) {
                        IFMLLoadingPlugin plug;
                        do {
                            if (!i$7.hasNext()) {
                                downloadMonitor.updateProgressString("Running coremod plugins", new Object[0]);
                                Map<String, Object> data = new HashMap();
                                data.put("mcLocation", mcDir);
                                data.put("coremodList", loadPlugins);
                                i$7 = loadPlugins.iterator();

                                while (i$7.hasNext()) {
                                    plugin = (IFMLLoadingPlugin) i$7.next();
                                    downloadMonitor.updateProgressString("Running coremod plugin %s", new Object[]{plugin.getClass().getSimpleName()});
                                    data.put("coremodLocation", pluginLocations.get(plugin));
                                    plugin.injectData(data);
                                    String setupClass = plugin.getSetupClass();
                                    if (setupClass != null) {
                                        try {
                                            IFMLCallHook call = (IFMLCallHook) Class.forName(setupClass, true, actualClassLoader).newInstance();
                                            Map<String, Object> callData = new HashMap();
                                            callData.put("classLoader", actualClassLoader);
                                            call.injectData(callData);
                                            call.call();
                                        } catch (Exception var35) {
                                            throw new RuntimeException(var35);
                                        }
                                    }

                                    downloadMonitor.updateProgressString("Coremod plugin %s run successfully", new Object[]{plugin.getClass().getSimpleName()});
                                    String modContainer = plugin.getModContainerClass();
                                    if (modContainer != null) {
                                        FMLInjectionData.containers.add(modContainer);
                                    }
                                }

                                try {
                                    downloadMonitor.updateProgressString("Validating minecraft", new Object[0]);
                                    Class<?> loaderClazz = Class.forName("cpw.mods.fml.common.Loader", true, actualClassLoader);
                                    Method m = loaderClazz.getMethod("injectData", Object[].class);
                                    m.invoke((Object) null, (Object) FMLInjectionData.data());
                                    m = loaderClazz.getMethod("instance");
                                    m.invoke((Object) null);
                                    downloadMonitor.updateProgressString("Minecraft validated, launching...", new Object[0]);
                                    downloadBuffer = null;
                                    return;
                                } catch (Exception var34) {
                                    System.out.println("A CRITICAL PROBLEM OCCURED INITIALIZING MINECRAFT - LIKELY YOU HAVE AN INCORRECT VERSION FOR THIS FML");
                                    throw new RuntimeException(var34);
                                }
                            }

                            plug = (IFMLLoadingPlugin) i$7.next();
                        } while (plug.getASMTransformerClass() == null);

                        arr$8 = plug.getASMTransformerClass();
                        len$ = arr$8.length;

                        for (int i$10 = 0; i$10 < len$; ++i$10) {
                            xformClass = arr$8[i$10];
                            actualClassLoader.registerTransformer(xformClass);
                        }
                    }
                }
            }
        }
    }

    private static void discoverCoreMods(File mcDir, RelaunchClassLoader classLoader, List<IFMLLoadingPlugin> loadPlugins, List<ILibrarySet> libraries) {
        downloadMonitor.updateProgressString("Discovering coremods", new Object[0]);
        File coreMods = setupCoreModDir(mcDir);
        FilenameFilter ff = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };
        File[] coreModList = coreMods.listFiles(ff);
        Arrays.sort(coreModList);
        File[] arr$ = coreModList;
        int len$ = coreModList.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            File coreMod = arr$[i$];
            downloadMonitor.updateProgressString("Found a candidate coremod %s", new Object[]{coreMod.getName()});

            Attributes mfAttributes;
            try {
                JarFile jar = new JarFile(coreMod);
                mfAttributes = jar.getManifest().getMainAttributes();
            } catch (IOException var26) {
                FMLRelaunchLog.log(Level.SEVERE, var26, "Unable to read the coremod jar file %s - ignoring", new Object[]{coreMod.getName()});
                continue;
            }

            String fmlCorePlugin = mfAttributes.getValue("FMLCorePlugin");
            if (fmlCorePlugin == null) {
                FMLRelaunchLog.severe("The coremod %s does not contain a valid jar manifest- it will be ignored", new Object[]{coreMod.getName()});
            } else {
                try {
                    classLoader.addURL(coreMod.toURI().toURL());
                } catch (MalformedURLException var25) {
                    FMLRelaunchLog.log(Level.SEVERE, var25, "Unable to convert file into a URL. weird", new Object[0]);
                    continue;
                }

                try {
                    downloadMonitor.updateProgressString("Loading coremod %s", new Object[]{coreMod.getName()});
                    classLoader.addTransformerExclusion(fmlCorePlugin);
                    Class<?> coreModClass = Class.forName(fmlCorePlugin, true, classLoader);
                    IFMLLoadingPlugin.TransformerExclusions trExclusions = (IFMLLoadingPlugin.TransformerExclusions) coreModClass.getAnnotation(IFMLLoadingPlugin.TransformerExclusions.class);

                    if (trExclusions != null) {
                        String[] arr$12 = trExclusions.value();
                        int len$11 = arr$12.length;

                        for (int len$12 = 0; len$12 < len$11; ++len$12) {
                            String st = arr$12[len$12];
                            classLoader.addTransformerExclusion(st);
                        }
                    }

                    IFMLLoadingPlugin plugin = (IFMLLoadingPlugin) coreModClass.newInstance();
                    loadPlugins.add(plugin);
                    pluginLocations.put(plugin, coreMod);
                    if (plugin.getLibraryRequestClass() != null) {
                        String[] arr$13 = plugin.getLibraryRequestClass();
                        int len$11 = arr$13.length;

                        for (int i$14 = 0; i$14 < len$11; ++i$14) {
                            String libName = arr$13[i$14];
                            libraries.add((ILibrarySet) Class.forName(libName, true, classLoader).newInstance());
                        }
                    }

                    downloadMonitor.updateProgressString("Loaded coremod %s", new Object[]{coreMod.getName()});
                } catch (ClassNotFoundException var21) {
                    FMLRelaunchLog.log(Level.SEVERE, var21, "Coremod %s: Unable to class load the plugin %s", new Object[]{coreMod.getName(), fmlCorePlugin});
                } catch (ClassCastException var22) {
                    FMLRelaunchLog.log(Level.SEVERE, var22, "Coremod %s: The plugin %s is not an implementor of IFMLLoadingPlugin", new Object[]{coreMod.getName(), fmlCorePlugin});
                } catch (InstantiationException var23) {
                    FMLRelaunchLog.log(Level.SEVERE, var23, "Coremod %s: The plugin class %s was not instantiable", new Object[]{coreMod.getName(), fmlCorePlugin});
                } catch (IllegalAccessException var24) {
                    FMLRelaunchLog.log(Level.SEVERE, var24, "Coremod %s: The plugin class %s was not accessible", new Object[]{coreMod.getName(), fmlCorePlugin});
                }
            }
        }

    }

    private static File setupLibDir(File mcDir) {
        File libDir = new File(mcDir, "lib");

        try {
            libDir = libDir.getCanonicalFile();
        } catch (IOException var3) {
            throw new RuntimeException(String.format("Unable to canonicalize the lib dir at %s", mcDir.getName()), var3);
        }

        if (!libDir.exists()) {
            libDir.mkdir();
        } else if (libDir.exists() && !libDir.isDirectory()) {
            throw new RuntimeException(String.format("Found a lib file in %s that's not a directory", mcDir.getName()));
        }

        return libDir;
    }

    private static File setupCoreModDir(File mcDir) {
        File coreModDir = new File(mcDir, "coremods");

        try {
            coreModDir = coreModDir.getCanonicalFile();
        } catch (IOException var3) {
            throw new RuntimeException(String.format("Unable to canonicalize the coremod dir at %s", mcDir.getName()), var3);
        }

        if (!coreModDir.exists()) {
            coreModDir.mkdir();
        } else if (coreModDir.exists() && !coreModDir.isDirectory()) {
            throw new RuntimeException(String.format("Found a coremod file in %s that's not a directory", mcDir.getName()));
        }

        return coreModDir;
    }

    private static void downloadFile(File libFile, String rootUrl, String realFilePath, String hash) {
        try {
            URL libDownload = new URL(String.format(rootUrl, realFilePath));
            System.out.println("Downloading file: " + libDownload.getHost() + libDownload.getPath());
            System.out.println("The libFile's path is " + libFile.getAbsolutePath());
            String infoString = String.format("Downloading file %s", libDownload.toString());
            downloadMonitor.updateProgressString(infoString, new Object[0]);
            FMLRelaunchLog.info(infoString, new Object[0]);
            URLConnection connection = libDownload.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "FML Relaunch Downloader");
            int sizeGuess = connection.getContentLength();
            performDownload(connection.getInputStream(), sizeGuess, hash, libFile);
            downloadMonitor.updateProgressString("Download complete", new Object[0]);
            FMLRelaunchLog.info("Download complete", new Object[0]);
        } catch (Exception var8) {
            if (downloadMonitor.shouldStopIt()) {
                FMLRelaunchLog.warning("You have stopped the downloading operation before it could complete", new Object[0]);
            } else if (var8 instanceof RuntimeException) {
                throw (RuntimeException) var8;
            } else {
                FMLRelaunchLog.severe("There was a problem downloading the file %s automatically. Perhaps you have an environment without internet access. You will need to download the file manually or restart and let it try again\n", new Object[]{libFile.getName()});
                libFile.delete();
                throw new RuntimeException("A download error occured", var8);
            }
        }
    }

    public static List<String> getLibraries() {
        return loadedLibraries;
    }

    private static void performDownload(InputStream is, int sizeGuess, String validationHash, File target) {
        if (sizeGuess > downloadBuffer.capacity()) {
            throw new RuntimeException(String.format("The file %s is too large to be downloaded by FML - the coremod is invalid", target.getName()));
        } else {
            downloadBuffer.clear();
            int fullLength = 0;
            downloadMonitor.resetProgress(sizeGuess);

            try {
                downloadMonitor.setPokeThread(Thread.currentThread());
                byte[] smallBuffer = new byte[1024];

                while (true) {
                    int bytesRead;
                    if ((bytesRead = is.read(smallBuffer)) >= 0) {
                        downloadBuffer.put(smallBuffer, 0, bytesRead);
                        fullLength += bytesRead;
                        if (!downloadMonitor.shouldStopIt()) {
                            downloadMonitor.updateProgress(fullLength);
                            continue;
                        }
                    }

                    is.close();
                    downloadMonitor.setPokeThread((Thread) null);
                    downloadBuffer.limit(fullLength);
                    downloadBuffer.position(0);
                    break;
                }
            } catch (InterruptedIOException var9) {
                Thread.interrupted();
                return;
            } catch (IOException var10) {
                throw new RuntimeException(var10);
            }

            try {
                String cksum = generateChecksum(downloadBuffer);
                if (cksum.equals(validationHash)) {
                    downloadBuffer.position(0);
                    FileOutputStream fos = new FileOutputStream(target);
                    fos.getChannel().write(downloadBuffer);
                    fos.close();
                } else {
                    throw new RuntimeException(String.format("The downloaded file %s has an invalid checksum %s (expecting %s). The download did not succeed correctly and the file has been deleted. Please try launching again.", target.getName(), cksum, validationHash));
                }
            } catch (Exception var8) {
                if (var8 instanceof RuntimeException) {
                    throw (RuntimeException) var8;
                } else {
                    throw new RuntimeException(var8);
                }
            }
        }
    }

    private static String generateChecksum(ByteBuffer buffer) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(buffer);
            byte[] chksum = digest.digest();
            StringBuilder hex = new StringBuilder(2 * chksum.length);
            byte[] arr$ = chksum;
            int len$ = chksum.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                byte b = arr$[i$];
                hex.append("0123456789abcdef".charAt((b & 240) >> 4)).append("0123456789abcdef".charAt(b & 15));
            }

            return hex.toString();
        } catch (Exception var8) {
            return null;
        }
    }
}
