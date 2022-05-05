package fr.catcore.fabricatedmodloader.utils;

import fr.catcore.fabricatedmodloader.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MLModDiscoverer {

    private static final File modDir = new File(FabricLoader.getInstance().getGameDir().toFile(), "/mods/");

    private static final Map<String, BArrayList<String>> EXCLUDED = new HashMap<>();

    protected static void init() {
        RemapUtil.init();

        List<MLModEntry> mods = new ArrayList<>();

        for (File file : modDir.listFiles()) {
            String name = file.getName();
            if (file.isDirectory() || (file.isFile() && (name.endsWith(".jar") || name.endsWith(".zip")))) {
                File remappedFile = new File(Constants.REMAPPED_FOLDER, name);

                List<String> modName = new ArrayList<>();

                if (file.isDirectory()) {
                    remappedFile = new File(Constants.REMAPPED_FOLDER, name + ".zip");
                    for (File subFile : file.listFiles()) {
                        String subName = subFile.getName();
                        if (subFile.isFile() && subName.startsWith("mod_") && subName.endsWith(".class")) {
                            modName.add(subName.replace("mod_", "").replace(".class", ""));
                        }
                    }

                    if (!modName.isEmpty() && EXCLUDED.containsKey(modName.get(0))) {
                        for (String excluded :
                                EXCLUDED.get(modName.get(0))) {
                            File excludedFile = new File(file, excluded);
                            excludedFile.delete();
                        }
                    }
                } else {
                    try {
                        FileInputStream fileinputstream = new FileInputStream(file);
                        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
                        while (true) {
                            ZipEntry zipentry = zipinputstream.getNextEntry();
                            if (zipentry == null) {
                                zipinputstream.close();
                                fileinputstream.close();
                                break;
                            }

                            String s1 = zipentry.getName();
                            if (!zipentry.isDirectory()) {
                                if (s1.equals("fabric.mod.json")) {
                                    break;
                                } else if (s1.startsWith("mod_") && s1.endsWith(".class")) {
                                    modName.add(s1.replace("mod_", "").replace(".class", ""));
                                }
                            }
                        }

                        if (!modName.isEmpty()) {
                            if (EXCLUDED.containsKey(modName.get(0))) {
                                File tempFile = new File(file.getAbsolutePath() + ".tmp");
                                tempFile.delete();
                                tempFile.deleteOnExit();

                                boolean renameOk = file.renameTo(tempFile);
                                if (!renameOk) {
                                    throw new RuntimeException("could not rename the file " + file.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
                                }

                                ZipInputStream zin = new ZipInputStream(Files.newInputStream(tempFile.toPath()));
                                ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(file.toPath()));

                                ZipEntry entry = zin.getNextEntry();
                                byte[] buf = new byte[1024];

                                while (entry != null) {
                                    String zipEntryName = entry.getName();
                                    boolean toBeDeleted = EXCLUDED.get(modName.get(0)).contains(zipEntryName);

                                    if (!toBeDeleted) {
                                        zout.putNextEntry(new ZipEntry(zipEntryName));
                                        // Transfer bytes from the ZIP file to the output file
                                        int len;
                                        while ((len = zin.read(buf)) > 0) {
                                            zout.write(buf, 0, len);
                                        }
                                    }

                                    entry = zin.getNextEntry();
                                }

                                // Close the streams
                                zin.close();
                                // Compress the files
                                // Complete the ZIP file
                                zout.close();
                                tempFile.delete();
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (!modName.isEmpty()) {
                    List<String> files = RemapUtil.makeModMappings(file.toPath());
                    String firstModName = modName.remove(0);
                    mods.add(new MLModEntry(firstModName, firstModName.toLowerCase(Locale.ENGLISH), "net/minecraft/mod_" + firstModName + ".class", remappedFile, file));

                    while (!modName.isEmpty()) {
                        String modname = modName.remove(0);
                        mods.add(new MLModEntry(modname, modname.toLowerCase(Locale.ENGLISH), "net/minecraft/mod_" + modname + ".class", remappedFile, null));
                    }
                }
            }
        }

        RemapUtil.generateModMappings();

        for (MLModEntry entry : mods) {
            if (entry.original != null) RemapUtil.remapMod(entry.original.toPath(), entry.file.toPath());

            FakeModManager.addModEntry(entry);
        }

        FakeModManager.getMods().forEach(modEntry -> {
            if (modEntry.original != null) FabricLauncherBase.getLauncher().addToClassPath(modEntry.file.toPath());
        });
    }

    static {
//        EXCLUDED.put("ReiMinimap", new BArrayList<>());
//        EXCLUDED.get("ReiMinimap")
//                .put("aow.class");
    }
}
