package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileUtils {

    public static void writeTextFile(Collection<String> lines, File file) {
        file.getParentFile().mkdirs();
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String line : lines) {
                bufferedWriter.append(line);
                bufferedWriter.append("\n");
            }
            bufferedWriter.close();
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readTextSource(String path) {
        List<String> result = new ArrayList<>();
        try {
            InputStream stream = FabricLauncherBase.class.getClassLoader().getResourceAsStream(path);
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader bufferedWriter = new BufferedReader(streamReader);
            String line = bufferedWriter.readLine();
            while (line != null) {
                result.add(line);
                line = bufferedWriter.readLine();
            }
            bufferedWriter.close();
            streamReader.close();
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
