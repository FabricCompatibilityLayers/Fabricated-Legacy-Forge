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
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import cpw.mods.fml.relauncher.IClassTransformer;
import fr.catcore.fabricatedforge.Constants;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class AccessTransformer implements IClassTransformer {
    private static final boolean DEBUG = false;
    private Multimap<String, AccessTransformer.Modifier> modifiers = ArrayListMultimap.create();

    public AccessTransformer() throws IOException {
        this("fml_at.cfg");
    }

    protected AccessTransformer(String rulesFile) throws IOException {
        this.readMapFile(rulesFile);
    }

    private void readMapFile(String rulesFile) throws IOException {
        File file = new File(rulesFile);
        URL rulesResource;
        if (file.exists()) {
            rulesResource = file.toURI().toURL();
        } else {
            rulesResource = Resources.getResource(rulesFile);
        }

        Resources.readLines(rulesResource, Charsets.UTF_8, new LineProcessor<Void>() {
            public Void getResult() {
                return null;
            }

            public boolean processLine(String input) throws IOException {
                String line = ((String)Iterables.getFirst(Splitter.on('#').limit(2).split(input), "")).trim();
                if (line.length() == 0) {
                    return true;
                } else {
                    List<String> parts = Lists.newArrayList(Splitter.on(" ").trimResults().split(line));
                    if (parts.size() > 2) {
                        throw new RuntimeException("Invalid config file line " + input);
                    } else {
                        AccessTransformer.Modifier m = AccessTransformer.this.new Modifier();
                        m.setTargetAccess((String)parts.get(0));
                        List<String> descriptor = Lists.newArrayList(Splitter.on(".").trimResults().split((CharSequence)parts.get(1)));

                        String className = descriptor.get(0);
                        String finalClassName = className;
                        try {
                            className = Constants.mapClass(finalClassName);
                        } catch (NullPointerException ignored) {
                            ignored.printStackTrace();
                        }

                        if (className.equals("net")) {
                            className = "net.minecraft.client.Minecraft";
                        }

                        if (descriptor.size() == 1) {
                            m.modifyClassVisibility = true;
                        } else {
                            String nameReference = descriptor.get(1);
                            int parenIdx = nameReference.indexOf(40);
                            if (parenIdx > 0) {
                                MappingUtils.ClassMember o = Constants.mapMethodFromRemappedClass(className,
                                        nameReference.substring(0, parenIdx), nameReference.substring(parenIdx));
                                m.desc = Constants.mapDescriptor(o.desc);
                                m.name = o.name;
                            } else {
                                m.name = Constants.mapFieldFromRemappedClass(className, nameReference, null).name;
                            }
                        }

                        AccessTransformer.this.modifiers.put(className.replace("/", "."), m);
                        return true;
                    }
                }
            }
        });
    }

    public byte[] transform(String name, byte[] bytes) {
        if (!this.modifiers.containsKey(name)) {
            return bytes;
        } else {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, 0);

            for(AccessTransformer.Modifier m : this.modifiers.get(name)) {
                if (m.modifyClassVisibility) {
                    classNode.access = this.getFixedAccess(classNode.access, m);
                } else if (m.desc.isEmpty()) {
                    for(FieldNode n : classNode.fields) {
                        if (n.name.equals(m.name) || m.name.equals("*")) {
                            n.access = this.getFixedAccess(n.access, m);
                            if (!m.name.equals("*")) {
                                break;
                            }
                        }
                    }
                } else {
                    for(MethodNode n : classNode.methods) {
                        if (n.name.equals(m.name) && n.desc.equals(m.desc) || m.name.equals("*")) {
                            n.access = this.getFixedAccess(n.access, m);
                            if (!m.name.equals("*")) {
                                break;
                            }
                        }
                    }
                }
            }

            ClassWriter writer = new ClassWriter(1);
            classNode.accept(writer);
            return writer.toByteArray();
        }
    }

    private String toBinary(int num) {
        return String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    private int getFixedAccess(int access, AccessTransformer.Modifier target) {
        target.oldAccess = access;
        int t = target.targetAccess;
        int ret = access & -8;
        switch(access & 7) {
            case 0:
                ret |= t != 2 ? t : 0;
                break;
            case 1:
                ret |= t != 2 && t != 0 && t != 4 ? t : 1;
                break;
            case 2:
                ret |= t;
                break;
            case 3:
            default:
                throw new RuntimeException("The fuck?");
            case 4:
                ret |= t != 2 && t != 0 ? t : 4;
        }

        if (target.changeFinal && target.desc == "") {
            if (target.markFinal) {
                ret |= 16;
            } else {
                ret &= -17;
            }
        }

        target.newAccess = ret;
        return ret;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: AccessTransformer <JarPath> <MapFile> [MapFile2]... ");
            System.exit(1);
        }

        boolean hasTransformer = false;
        AccessTransformer[] trans = new AccessTransformer[args.length - 1];

        for(int x = 1; x < args.length; ++x) {
            try {
                trans[x - 1] = new AccessTransformer(args[x]);
                hasTransformer = true;
            } catch (IOException var7) {
                System.out.println("Could not read Transformer Map: " + args[x]);
                var7.printStackTrace();
            }
        }

        if (!hasTransformer) {
            System.out.println("Culd not find a valid transformer to perform");
            System.exit(1);
        }

        File orig = new File(args[0]);
        File temp = new File(args[0] + ".ATBack");
        if (!orig.exists() && !temp.exists()) {
            System.out.println("Could not find target jar: " + orig);
            System.exit(1);
        }

        if (!orig.renameTo(temp)) {
            System.out.println("Could not rename file: " + orig + " -> " + temp);
            System.exit(1);
        }

        try {
            processJar(temp, orig, trans);
        } catch (IOException var6) {
            var6.printStackTrace();
            System.exit(1);
        }

        if (!temp.delete()) {
            System.out.println("Could not delete temp file: " + temp);
        }
    }

    private static void processJar(File inFile, File outFile, AccessTransformer[] transformers) throws IOException {
        ZipInputStream inJar = null;
        ZipOutputStream outJar = null;

        try {
            try {
                inJar = new ZipInputStream(new BufferedInputStream(new FileInputStream(inFile)));
            } catch (FileNotFoundException var30) {
                throw new FileNotFoundException("Could not open input file: " + var30.getMessage());
            }

            try {
                outJar = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            } catch (FileNotFoundException var29) {
                throw new FileNotFoundException("Could not open output file: " + var29.getMessage());
            }

            ZipEntry entry;
            while((entry = inJar.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    outJar.putNextEntry(entry);
                } else {
                    byte[] data = new byte[4096];
                    ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();

                    int len;
                    do {
                        len = inJar.read(data);
                        if (len > 0) {
                            entryBuffer.write(data, 0, len);
                        }
                    } while(len != -1);

                    byte[] entryData = entryBuffer.toByteArray();
                    String entryName = entry.getName();
                    if (entryName.endsWith(".class") && !entryName.startsWith(".")) {
                        ClassNode cls = new ClassNode();
                        ClassReader rdr = new ClassReader(entryData);
                        rdr.accept(cls, 0);
                        String name = cls.name.replace('/', '.').replace('\\', '.');

                        for(AccessTransformer trans : transformers) {
                            entryData = trans.transform(name, entryData);
                        }
                    }

                    ZipEntry newEntry = new ZipEntry(entryName);
                    outJar.putNextEntry(newEntry);
                    outJar.write(entryData);
                }
            }
        } finally {
            if (outJar != null) {
                try {
                    outJar.close();
                } catch (IOException var28) {
                }
            }

            if (inJar != null) {
                try {
                    inJar.close();
                } catch (IOException var27) {
                }
            }
        }
    }

    public void ensurePublicAccessFor(String modClazzName) {
        AccessTransformer.Modifier m = new AccessTransformer.Modifier();
        m.setTargetAccess("public");
        m.modifyClassVisibility = true;
        this.modifiers.put(modClazzName, m);
    }

    private class Modifier {
        public String name = "";
        public String desc = "";
        public int oldAccess = 0;
        public int newAccess = 0;
        public int targetAccess = 0;
        public boolean changeFinal = false;
        public boolean markFinal = false;
        protected boolean modifyClassVisibility;

        private Modifier() {
        }

        private void setTargetAccess(String name) {
            if (name.startsWith("public")) {
                this.targetAccess = 1;
            } else if (name.startsWith("private")) {
                this.targetAccess = 2;
            } else if (name.startsWith("protected")) {
                this.targetAccess = 4;
            }

            if (name.endsWith("-f")) {
                this.changeFinal = true;
                this.markFinal = false;
            } else if (name.endsWith("+f")) {
                this.changeFinal = true;
                this.markFinal = true;
            }
        }
    }
}
