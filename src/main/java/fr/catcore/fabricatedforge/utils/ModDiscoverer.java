package fr.catcore.fabricatedforge.utils;

import fr.catcore.fabricatedforge.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ModDiscoverer {

    private static final File modDir = new File(FabricLoader.getInstance().getGameDir().toFile(), "/mods/");

    private static final Map<String, BArrayList<String>> EXCLUDED = new HashMap<>();

    protected static void init() {
        RemapUtil.init();

        List<ModEntry> mods = new ArrayList<>();

        for (File file : modDir.listFiles()) {
            String name = file.getName();
            if (file.isDirectory() || (file.isFile() && (name.endsWith(".jar") || name.endsWith(".zip")))) {
                File remappedFile = new File(Constants.REMAPPED_FOLDER, name);

                List<ModEntry> modName = new ArrayList<>();

                if (file.isDirectory()) {
                    remappedFile = new File(Constants.REMAPPED_FOLDER, name + ".zip");
                    for (File subFile : file.listFiles()) {
                        String subName = subFile.getName();
                        if (subFile.isFile()) {
                            if (subName.startsWith("mod_") && subName.endsWith(".class")) {
                                String mName = subName.replace("mod_", "").replace(".class", "");
                                modName.add(new MLModEntry(
                                        mName,
                                        mName.toLowerCase(Locale.ENGLISH),
                                        "net/minecraft/" + subName,
                                        remappedFile,
                                        modName.isEmpty() ? file : null
                                ));
                            } else if (subName.equals("mcmod.info")) {
                                modName.clear();
                                try {
                                    modName.addAll(ForgeModEntry.parseModInfoFile(Files.newInputStream(subFile.toPath()), remappedFile, file));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }

                    if (!modName.isEmpty() && EXCLUDED.containsKey(modName.get(0).modName)) {
                        for (String excluded :
                                EXCLUDED.get(modName.get(0).modName)) {
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
                            String[] ss = s1.split("/");
                            String s2 = ss[ss.length - 1];
                            if (!zipentry.isDirectory()) {
                                if (s2.equals("fabric.mod.json")) {
                                    modName.clear();
                                    break;
                                } else if (s2.startsWith("mod_") && s2.endsWith(".class")) {
                                    String mName = s2.replace("mod_", "").replace(".class", "");
                                    modName.add(new MLModEntry(
                                            mName,
                                            mName.toLowerCase(Locale.ENGLISH),
                                            s1.contains("/") ? s1 : "net/minecraft/" + s1,
                                            remappedFile,
                                            modName.isEmpty() ? file : null
                                    ));
                                } else if (s2.equals("mcmod.info")) {
                                    modName.clear();
                                    try (ZipFile zipFile = new ZipFile(file)) {
                                        modName.addAll(ForgeModEntry.parseModInfoFile(zipFile.getInputStream(zipentry), remappedFile, file));
                                    }
                                    break;
                                }
                            }
                        }

                        if (!modName.isEmpty()) {
                            if (EXCLUDED.containsKey(modName.get(0).modName)) {
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
                                    boolean toBeDeleted = EXCLUDED.get(modName.get(0).modName).contains(zipEntryName);

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

                    while (!modName.isEmpty()) {
                        ModEntry modname = modName.remove(0);
                        mods.add(modname);
                    }
                }
            }
        }

        RemapUtil.generateModMappings();

        for (ModEntry entry : mods) {
            if (entry.original != null) RemapUtil.remapMod(entry.original.toPath(), entry.file.toPath());

            FakeModManager.addModEntry(entry);
        }

        List<String> modFileNames = new ArrayList<>();
        for (ModEntry entry : FakeModManager.getMods()) {
            modFileNames.add(entry.file.getName());
        }

        List<String> modFileCandidates = new ArrayList<>();
        for (File file : Constants.REMAPPED_FOLDER.listFiles()) {
            modFileCandidates.add(file.getName());
        }

        while (modFileCandidates.size() > 0) {
            String fileName = modFileCandidates.remove(0);
            if (!modFileNames.contains(fileName)) {
                File toDelete = new File(Constants.REMAPPED_FOLDER, fileName);
                toDelete.delete();
            }
        }

//        FakeModManager.getMods().forEach(modEntry -> {
//            if (modEntry.original != null) FabricLauncherBase.getLauncher().addToClassPath(modEntry.file.toPath());
//        });
    }

    static {
        EXCLUDED.put("ReiMinimap", new BArrayList<>());
        EXCLUDED.get("ReiMinimap")
                .put("aow.class");
    }
}
