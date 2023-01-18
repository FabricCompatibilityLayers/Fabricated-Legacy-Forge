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
package cpw.mods.fml.common.asm.transformers;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import cpw.mods.fml.relauncher.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MarkerTransformer implements IClassTransformer {
    private ListMultimap<String, String> markers;

    public MarkerTransformer() throws IOException {
        this("fml_marker.cfg");
    }

    protected MarkerTransformer(String rulesFile) throws IOException {
        this.markers = ArrayListMultimap.create();
        this.readMapFile(rulesFile);
    }

    private void readMapFile(String rulesFile) throws IOException {
        File file = new File(rulesFile);
        URL rulesResource;
        if (file.exists())
        {
            rulesResource = file.toURI().toURL();
        }
        else
        {
            rulesResource = Resources.getResource(rulesFile);
        }
        Resources.readLines(rulesResource, Charsets.UTF_8, new LineProcessor<Void>()
        {
            @Override
            public Void getResult()
            {
                return null;
            }

            @Override
            public boolean processLine(String input) throws IOException
            {
                String line = Iterables.getFirst(Splitter.on('#').limit(2).split(input), "").trim();
                if (line.length()==0)
                {
                    return true;
                }
                List<String> parts = Lists.newArrayList(Splitter.on(" ").trimResults().split(line));
                if (parts.size()!=2)
                {
                    throw new RuntimeException("Invalid config file line "+ input);
                }
                List<String> markerInterfaces = Lists.newArrayList(Splitter.on(",").trimResults().split(parts.get(1)));
                for (String marker : markerInterfaces)
                {
                    markers.put(parts.get(0), marker);
                }
                return true;
            }
        });
    }

    public byte[] transform(String name, byte[] bytes) {
        if (!markers.containsKey(name)) { return bytes; }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for (String marker : markers.get(name))
        {
            classNode.interfaces.add(marker);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: MarkerTransformer <JarPath> <MapFile> [MapFile2]... ");
        } else {
            boolean hasTransformer = false;
            MarkerTransformer[] trans = new MarkerTransformer[args.length - 1];

            for(int x = 1; x < args.length; ++x) {
                try {
                    trans[x - 1] = new MarkerTransformer(args[x]);
                    hasTransformer = true;
                } catch (IOException var7) {
                    System.out.println("Could not read Transformer Map: " + args[x]);
                    var7.printStackTrace();
                }
            }

            if (!hasTransformer) {
                System.out.println("Culd not find a valid transformer to perform");
            } else {
                File orig = new File(args[0]);
                File temp = new File(args[0] + ".ATBack");
                if (!orig.exists() && !temp.exists()) {
                    System.out.println("Could not find target jar: " + orig);
                } else if (!orig.renameTo(temp)) {
                    System.out.println("Could not rename file: " + orig + " -> " + temp);
                } else {
                    try {
                        processJar(temp, orig, trans);
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    }

                    if (!temp.delete()) {
                        System.out.println("Could not delete temp file: " + temp);
                    }

                }
            }
        }
    }

    private static void processJar(File inFile, File outFile, MarkerTransformer[] transformers) throws IOException {
        ZipInputStream inJar = null;
        ZipOutputStream outJar = null;

        try
        {
            try
            {
                inJar = new ZipInputStream(new BufferedInputStream(new FileInputStream(inFile)));
            }
            catch (FileNotFoundException e)
            {
                throw new FileNotFoundException("Could not open input file: " + e.getMessage());
            }

            try
            {
                outJar = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            }
            catch (FileNotFoundException e)
            {
                throw new FileNotFoundException("Could not open output file: " + e.getMessage());
            }

            ZipEntry entry;
            while ((entry = inJar.getNextEntry()) != null)
            {
                if (entry.isDirectory())
                {
                    outJar.putNextEntry(entry);
                    continue;
                }

                byte[] data = new byte[4096];
                ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();

                int len;
                do
                {
                    len = inJar.read(data);
                    if (len > 0)
                    {
                        entryBuffer.write(data, 0, len);
                    }
                }
                while (len != -1);

                byte[] entryData = entryBuffer.toByteArray();

                String entryName = entry.getName();

                if (entryName.endsWith(".class") && !entryName.startsWith("."))
                {
                    ClassNode cls = new ClassNode();
                    ClassReader rdr = new ClassReader(entryData);
                    rdr.accept(cls, 0);
                    String name = cls.name.replace('/', '.').replace('\\', '.');

                    for (MarkerTransformer trans : transformers)
                    {
                        entryData = trans.transform(name, entryData);
                    }
                }

                ZipEntry newEntry = new ZipEntry(entryName);
                outJar.putNextEntry(newEntry);
                outJar.write(entryData);
            }
        }
        finally
        {
            if (outJar != null)
            {
                try
                {
                    outJar.close();
                }
                catch (IOException e)
                {
                }
            }

            if (inJar != null)
            {
                try
                {
                    inJar.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }
}
