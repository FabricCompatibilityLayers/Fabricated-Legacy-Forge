package fr.catcore.fabricatedmodloader.utils;

import fr.catcore.fabricatedmodloader.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MLModDiscoverer {

    private static final File modDir = new File(FabricLoader.getInstance().getGameDir().toFile(), "/mods/");

    protected static void init() {
        RemapUtil.init();

        List<MLModEntry> mods = new ArrayList<>();

        for (File file : modDir.listFiles()) {
            String name = file.getName();
            if (file.isDirectory() || (file.isFile() && (name.endsWith(".jar") || name.endsWith(".zip")))) {
                File remappedFile = new File(Constants.REMAPPED_FOLDER, name);

                String modName = "";

                if (file.isDirectory()) {
                    remappedFile = new File(Constants.REMAPPED_FOLDER, name + ".zip");
                    for (File subFile : file.listFiles()) {
                        String subName = subFile.getName();
                        if (subFile.isFile() && subName.startsWith("mod_") && subName.endsWith(".class")) {
                            modName = subName.replace("mod_", "").replace(".class", "");
                            break;
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
                                    modName = s1.replace("mod_", "").replace(".class", "");
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (!modName.isEmpty()) {
                    List<String> files = RemapUtil.makeModMappings(file.toPath());

                    mods.add(new MLModEntry(modName, modName.toLowerCase(Locale.ENGLISH), "net/minecraft/mod_" + modName + ".class", remappedFile, file));
                }
            }
        }

        RemapUtil.generateModMappings();

        for (MLModEntry entry : mods) {
            RemapUtil.remapMod(entry.original.toPath(), entry.file.toPath());

            FakeModManager.addModEntry(entry);
        }
    }
}
