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
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class AccessTransformer implements IClassTransformer {
    private static final boolean DEBUG = false;
    private Multimap<String, AccessTransformer.Modifier> modifiers;

    public AccessTransformer() throws IOException {
        this("fml_at.cfg");
    }

    protected AccessTransformer(String rulesFile) throws IOException {
        this.modifiers = ArrayListMultimap.create();
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
                String line = ((String) Iterables.getFirst(Splitter.on('#').limit(2).split(input), "")).trim();
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
                        if (descriptor.size() == 1) {
                            m.modifyClassVisibility = true;
                        } else {
                            String nameReference = (String)descriptor.get(1);
                            int parenIdx = nameReference.indexOf(40);
                            if (parenIdx > 0) {
                                m.desc = nameReference.substring(parenIdx);
                                m.name = nameReference.substring(0, parenIdx);
                            } else {
                                m.name = nameReference;
                            }
                        }

                        AccessTransformer.this.modifiers.put(((String)descriptor.get(0)).replace('/', '.'), m);
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
            Collection<AccessTransformer.Modifier> mods = this.modifiers.get(name);
            Iterator i$ = mods.iterator();

            while(true) {
                label61:
                while(i$.hasNext()) {
                    AccessTransformer.Modifier m = (AccessTransformer.Modifier)i$.next();
                    if (m.modifyClassVisibility) {
                        classNode.access = this.getFixedAccess(classNode.access, m);
                    } else {
                        Iterator i$;
                        if (m.desc.isEmpty()) {
                            i$ = classNode.fields.iterator();

                            while(true) {
                                FieldNode n;
                                do {
                                    if (!i$.hasNext()) {
                                        continue label61;
                                    }

                                    n = (FieldNode)i$.next();
                                } while(!n.name.equals(m.name) && !m.name.equals("*"));

                                n.access = this.getFixedAccess(n.access, m);
                                if (!m.name.equals("*")) {
                                    break;
                                }
                            }
                        } else {
                            i$ = classNode.methods.iterator();

                            while(true) {
                                MethodNode n;
                                do {
                                    if (!i$.hasNext()) {
                                        continue label61;
                                    }

                                    n = (MethodNode)i$.next();
                                } while((!n.name.equals(m.name) || !n.desc.equals(m.desc)) && !m.name.equals("*"));

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
    }

    private String toBinary(int num) {
        return String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    private int getFixedAccess(int access, AccessTransformer.Modifier target) {
        target.oldAccess = access;
        int t = target.targetAccess;
        int ret = access & -8;
        switch (access & 7) {
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

            while(true) {
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
                            AccessTransformer[] arr$ = transformers;
                            int len$ = transformers.length;

                            for(int i$ = 0; i$ < len$; ++i$) {
                                AccessTransformer trans = arr$[i$];
                                entryData = trans.transform(name, entryData);
                            }
                        }

                        ZipEntry newEntry = new ZipEntry(entryName);
                        outJar.putNextEntry(newEntry);
                        outJar.write(entryData);
                    }
                }

                return;
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
        public String name;
        public String desc;
        public int oldAccess;
        public int newAccess;
        public int targetAccess;
        public boolean changeFinal;
        public boolean markFinal;
        protected boolean modifyClassVisibility;

        private Modifier() {
            this.name = "";
            this.desc = "";
            this.oldAccess = 0;
            this.newAccess = 0;
            this.targetAccess = 0;
            this.changeFinal = false;
            this.markFinal = false;
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
