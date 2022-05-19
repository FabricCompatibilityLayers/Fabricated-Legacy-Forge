package fr.catcore.fabricatedforge.remapping;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import fr.catcore.fabricatedforge.utils.Constants;
import fr.catcore.fabricatedforge.utils.FileUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.mappings.TinyRemapperMappingsHelper;
import net.fabricmc.mapping.reader.v2.TinyMetadata;
import net.fabricmc.mapping.tree.*;
import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import net.fabricmc.tinyremapper.api.TrClass;
import org.objectweb.asm.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RemapUtil {
    private static TinyTree LOADER_TREE;
    private static TinyTree MINECRAFT_TREE;
    private static TinyTree MODS_TREE;

    private static final Map<String, String> MOD_MAPPINGS = new HashMap<>();

    public static void init() {
        generateMappings();
        LOADER_TREE = makeTree(Constants.MAPPINGS_FILE);
        MINECRAFT_TREE = FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings();
    }

    public static void remapMod(Path from, Path to) {
        if (to.toFile().exists()) return;
        Log.info(Constants.LOG_CATEGORY, "Remapping mod " + from.getFileName());
        TinyRemapper remapper = makeRemapper(MINECRAFT_TREE, LOADER_TREE, MODS_TREE);
        remapFile(remapper, from, to);
        Log.info(Constants.LOG_CATEGORY, "Remapped mod " + from.getFileName());
    }

    public static List<String> makeModMappings(Path modPath) {
        File path = modPath.toFile();
        List<String> files = new ArrayList<>();
        if (path.isFile()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(path);
                ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
                Object obj = null;

                while (true) {
                    ZipEntry zipentry = zipinputstream.getNextEntry();
                    if (zipentry == null) {
                        zipinputstream.close();
                        fileinputstream.close();
                        break;
                    }

                    String s1 = zipentry.getName();
                    if (!zipentry.isDirectory()) {
                        files.add(s1);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (path.isDirectory()) {
            files.addAll(generateFolderMappings(path.listFiles()));
        }

        List<String> classes = new ArrayList<>();

        for (String file : files) {
            if (file.endsWith(".class")) classes.add(file.replace(".class", ""));
        }

        classes.forEach(cl -> MOD_MAPPINGS.put(cl, (cl.contains("/") ? "" : "net/minecraft/") + cl));

        return files;
    }

    public static void generateModMappings() {
        List<String> mappings = new ArrayList<>();
        mappings.add(toString("v1", "official", "intermediary", "named"));
        MOD_MAPPINGS.forEach((cl, remapped) -> mappings.add(makeLoaderLineClass(cl, remapped)));
        FileUtils.writeTextFile(mappings, Constants.MOD_MAPPINGS_FILE);

        MODS_TREE = makeTree(Constants.MOD_MAPPINGS_FILE);
    }

    private static List<String> generateFolderMappings(File[] files) {
        List<String> list = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) list.add(file.getName());
            else if (file.isDirectory()) {
                String name = file.getName();

                for (String fileName : generateFolderMappings(file.listFiles())) {
                    list.add(name + "/" + fileName);
                }
            }
        }

        return list;
    }

    private static void generateMappings() {
        if (!Constants.MAPPINGS_FILE.exists()) {
            List<String> mappings = new ArrayList<>();

            mappings.add(toString("v1", "official", "intermediary", "named"));

            // ModLoader mappings
            mappings.add(makeLoaderLineClass("BaseMod", "net/minecraft/BaseMod"));
            mappings.add(makeLoaderLineClass("EntityRendererProxy", "net/minecraft/EntityRendererProxy"));
            mappings.add(makeLoaderLineClass("FMLRendererAccessLibrary", "net/minecraft/FMLRendererAccessLibrary"));
            mappings.add(makeLoaderLineClass("MLProp", "net/minecraft/MLProp"));
            mappings.add(makeLoaderLineClass("ModLoader", "net/minecraft/ModLoader"));
            mappings.add(makeLoaderLineClass("ModTextureAnimation", "net/minecraft/ModTextureAnimation"));
            mappings.add(makeLoaderLineClass("ModTextureStatic", "net/minecraft/ModTextureStatic"));
            mappings.add(makeLoaderLineClass("TradeEntry", "net/minecraft/TradeEntry"));

            // Forge cursed overwrites mappings
            mappings.add(makeLoaderLineClass("axf", "fr/catcore/fabricatedforge/forged/class_585Forged"));
            mappings.add(makeLoaderLineField("g", "field_2158", "axf", "[F"));
            mappings.add(makeLoaderLineField("h", "field_2159", "axf", "[F"));

            mappings.add(makeLoaderLineClass("axg", "fr/catcore/fabricatedforge/forged/class_586Forged"));
            mappings.add(makeLoaderLineField("g", "field_2160", "axg", "[F"));
            mappings.add(makeLoaderLineField("h", "field_2161", "axg", "[F"));
            mappings.add(makeLoaderLineField("i", "field_2162", "axg", "[F"));
            mappings.add(makeLoaderLineField("j", "field_2163", "axg", "[F"));
            mappings.add(makeLoaderLineField("k", "field_2164", "axg", "I"));

            mappings.add(makeLoaderLineClass("axh", "fr/catcore/fabricatedforge/forged/class_587Forged"));
            mappings.add(makeLoaderLineField("g", "field_2165", "axh", "[F"));
            mappings.add(makeLoaderLineField("h", "field_2166", "axh", "[F"));
            mappings.add(makeLoaderLineField("i", "field_2167", "axh", "[F"));
            mappings.add(makeLoaderLineField("j", "field_2168", "axh", "[F"));

            mappings.add(makeLoaderLineClass("axi", "fr/catcore/fabricatedforge/forged/class_588Forged"));
            mappings.add(makeLoaderLineField("g", "field_2169", "axi", "I"));
            mappings.add(makeLoaderLineField("h", "field_2170", "axi", "[[B"));

            mappings.add(makeLoaderLineClass("axj", "fr/catcore/fabricatedforge/forged/class_589Forged"));
            mappings.add(makeLoaderLineField("g", "field_2171", "axj", "[F"));
            mappings.add(makeLoaderLineField("h", "field_2172", "axj", "[F"));
            mappings.add(makeLoaderLineField("i", "field_2173", "axj", "[F"));
            mappings.add(makeLoaderLineField("j", "field_2174", "axj", "[F"));
            mappings.add(makeLoaderLineField("k", "field_2175", "axj", "I"));

            mappings.add(makeLoaderLineClass("axk", "fr/catcore/fabricatedforge/forged/class_590Forged"));
            mappings.add(makeLoaderLineField("g", "field_2176", "axk", "[F"));
            mappings.add(makeLoaderLineField("h", "field_2177", "axk", "[F"));
            mappings.add(makeLoaderLineField("i", "field_2178", "axk", "[F"));
            mappings.add(makeLoaderLineField("j", "field_2179", "axk", "[F"));
            mappings.add(makeLoaderLineField("k", "field_2180", "axk", "I"));

            mappings.add(makeLoaderLineClass("axc", "fr/catcore/fabricatedforge/forged/ClockSpriteForged"));
            mappings.add(makeLoaderLineField("g", "field_2143", "axc", "Lnet/minecraft/client/Minecraft;"));
            mappings.add(makeLoaderLineField("h", "field_2144", "axc", "[I"));
            mappings.add(makeLoaderLineField("i", "field_2145", "axc", "[I"));
            mappings.add(makeLoaderLineField("j", "field_2146", "axc", "D"));
            mappings.add(makeLoaderLineField("k", "field_2147", "axc", "D"));

            mappings.add(makeLoaderLineClass("axd", "fr/catcore/fabricatedforge/forged/CompassSpriteForged"));
            mappings.add(makeLoaderLineField("g", "field_2148", "axd", "Lnet/minecraft/client/Minecraft;"));
            mappings.add(makeLoaderLineField("h", "field_2149", "axd", "[I"));
            mappings.add(makeLoaderLineField("i", "field_2150", "axd", "D"));
            mappings.add(makeLoaderLineField("j", "field_2151", "axd", "D"));
//            mappings.add(makeLoaderLineClass("net/minecraft/class_1041", "fr/catcore/fabricatedforge/forged/ItemGroupForged"));

            FileUtils.writeTextFile(mappings, Constants.MAPPINGS_FILE);
        }
    }

    /**
     * Will convert array to mapping-like string (with tab separator).
     *
     * @param line array of {@link String} that represents mappings line.
     */
    private static String toString(String... line) {
        StringBuilder builder = new StringBuilder(line[0]);
        for (int j = 1; j < line.length; j++) {
            builder.append('\t');
            builder.append(line[j]);
        }
        return builder.toString();
    }

    private static String makeLoaderLineClass(String from, String to) {
        return toString("CLASS", from, to, to);
    }

    private static String makeLoaderLineField(String from, String to, String clas, String desc) {
        return toString("FIELD", clas, desc, from, to, to);
    }

    /**
     * Will make tree for specified mappings file.
     *
     * @param file mappings {@link File} in tiny format.
     */
    private static TinyTree makeTree(File file) {
        TinyTree tree = null;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            tree = TinyMappingFactory.loadWithDetection(bufferedReader);
            tree = wrapTree(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tree;
    }

    /**
     * Will create remapper with specified trees.
     */
    private static TinyRemapper makeRemapper(TinyTree... trees) {
        TinyRemapper.Builder builder = TinyRemapper
                .newRemapper()
                .renameInvalidLocals(true)
                .ignoreFieldDesc(false)
                .propagatePrivate(true)
                .ignoreConflicts(true);

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            builder.fixPackageAccess(true);
        }

        for (TinyTree tree : trees) {
            builder.withMappings(createProvider(tree));
        }

        builder.extraPostApplyVisitor(new TinyRemapper.ApplyVisitorProvider() {
            @Override
            public ClassVisitor insertApplyVisitor(TrClass cls, ClassVisitor next) {
                return new ClassVisitor(Opcodes.ASM9, next) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                        return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                            @Override
                            public void visitMethodInsn(int opcode, String methodOwner, String methodName, String methodDescriptor, boolean isInterface) {
                                for (Map.Entry<Entry, Entry> entry : METHOD_OVERWRITES.entrySet()) {
                                    Entry original = entry.getKey();
                                    Entry newEntry = entry.getValue();

                                    if (original.name.equals(methodName) && original.descriptor.equals(methodDescriptor) && original.owner.equals(methodOwner)) {
                                        methodName = newEntry.name;
                                        methodDescriptor = newEntry.descriptor;
                                        methodOwner = newEntry.owner;
                                        break;
                                    }
                                }

                                super.visitMethodInsn(opcode, methodOwner, methodName, methodDescriptor, isInterface);
                            }

                            @Override
                            public void visitLdcInsn(Object value) {
                                if (cls.getName().equals("InvTweaksLocalization")
                                        && name.equals("load")
                                        && value instanceof String
                                        && ((String)value).startsWith("invtweaks")
                                ) {
                                    value = "/" + ((String) value);
                                } else if (cls.getName().equals("InvTweaksObfuscation") && value instanceof String) {
                                    String string = (String) value;
                                    if (string.equals("b")) value = "field_1386";
                                    else if (string.equals("k")) value = "field_1981";
                                }
                                super.visitLdcInsn(value);
                            }
                        };
                    }
                };
            }
        });

        TinyRemapper remapper = builder.build();
        remapper.readClassPath((Path) FabricLoader.getInstance().getObjectShare().get("fabric-loader:inputGameJar"));
        return remapper;
    }

    /**
     * Will create a mapping provider for specified tree.
     */
    private static IMappingProvider createProvider(TinyTree tree) {
        FabricLauncher launcher = FabricLauncherBase.getLauncher();
        return TinyRemapperMappingsHelper.create(tree, "official", launcher.getTargetNamespace());
    }

    /**
     * Will remap file with specified remapper and store it into output.
     *
     * @param remapper {@link TinyRemapper} to remap with.
     * @param input    {@link Path} for the input file.
     * @param output   {@link Path} for the output file.
     */
    private static void remapFile(TinyRemapper remapper, Path input, Path output) {
        try {
            OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(output).assumeArchive(true).build();
            outputConsumer.addNonClassFiles(input);

            remapper.readInputs(input);
            remapper.apply(outputConsumer);
            remapper.finish();

            outputConsumer.close();
        } catch (Exception e) {
            remapper.finish();
            throw new RuntimeException("Failed to remap jar", e);
        }
    }

    /**
     * Function from Fabric Loader (was with private access). Will wrap mapping tree into another one.
     */
    private static TinyTree wrapTree(TinyTree mappings) {
        return new TinyTree() {
            final String primaryNamespace = getMetadata().getNamespaces().get(0); //If the namespaces are empty we shouldn't exist

            private Optional<String> remap(String name, String namespace) {
                return Optional.ofNullable(getDefaultNamespaceClassMap().get(name)).map(mapping -> mapping.getRawName(namespace)).map(
                        Strings::emptyToNull);
            }

            String remapDesc(String desc, String namespace) {
                Type type = Type.getType(desc);

                switch (type.getSort()) {
                    case Type.ARRAY: {

                        return desc.substring(0, type.getDimensions()) + remapDesc(type.getElementType().getDescriptor(), namespace);
                    }

                    case Type.OBJECT:
                        return remap(type.getInternalName(), namespace).map(name -> 'L' + name + ';').orElse(desc);

                    case Type.METHOD: {
                        if ("()V".equals(desc)) return desc;

                        StringBuilder stringBuilder = new StringBuilder("(");
                        for (Type argumentType : type.getArgumentTypes()) {
                            stringBuilder.append(remapDesc(argumentType.getDescriptor(), namespace));
                        }

                        Type returnType = type.getReturnType();
                        if (returnType == Type.VOID_TYPE) {
                            stringBuilder.append(")V");
                        } else {
                            stringBuilder.append(')').append(remapDesc(returnType.getDescriptor(), namespace));
                        }

                        return stringBuilder.toString();
                    }

                    default:
                        return desc;
                }
            }

            private ClassDef wrap(ClassDef mapping) {
                return new ClassDef() {
                    private final boolean common = getMetadata().getNamespaces().stream().skip(1).map(this::getRawName).allMatch(Strings::isNullOrEmpty);

                    @Override
                    public String getRawName(String namespace) {
                        try {
                            return mapping.getRawName(common ? primaryNamespace : namespace);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            return ""; //No name for the namespace
                        }
                    }

                    @Override
                    public String getName(String namespace) {
                        return mapping.getName(namespace);
                    }

                    @Override
                    public String getComment() {
                        return mapping.getComment();
                    }

                    @Override
                    public Collection<MethodDef> getMethods() {
                        return Collections2.transform(mapping.getMethods(), method -> new MethodDef() {
                            @Override
                            public String getRawName(String namespace) {
                                try {
                                    return method.getRawName(namespace);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    return ""; //No name for the namespace
                                }
                            }

                            @Override
                            public String getName(String namespace) {
                                return method.getName(namespace);
                            }

                            @Override
                            public String getComment() {
                                return method.getComment();
                            }

                            @Override
                            public String getDescriptor(String namespace) {
                                String desc = method.getDescriptor(primaryNamespace);
                                return primaryNamespace.equals(namespace) ? desc : remapDesc(desc, namespace);
                            }

                            @Override
                            public Collection<ParameterDef> getParameters() {
                                return method.getParameters();
                            }

                            @Override
                            public Collection<LocalVariableDef> getLocalVariables() {
                                return method.getLocalVariables();
                            }
                        });
                    }

                    @Override
                    public Collection<FieldDef> getFields() {
                        return Collections2.transform(mapping.getFields(), field -> new FieldDef() {
                            @Override
                            public String getRawName(String namespace) {
                                try {
                                    return field.getRawName(namespace);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    return ""; //No name for the namespace
                                }
                            }

                            @Override
                            public String getName(String namespace) {
                                return field.getName(namespace);
                            }

                            @Override
                            public String getComment() {
                                return field.getComment();
                            }

                            @Override
                            public String getDescriptor(String namespace) {
                                String desc = field.getDescriptor(primaryNamespace);
                                return primaryNamespace.equals(namespace) ? desc : remapDesc(desc, namespace);
                            }
                        });
                    }
                };
            }

            @Override
            public TinyMetadata getMetadata() {
                return mappings.getMetadata();
            }

            @Override
            public Map<String, ClassDef> getDefaultNamespaceClassMap() {
                return Maps.transformValues(mappings.getDefaultNamespaceClassMap(), this::wrap);
            }

            @Override
            public Collection<ClassDef> getClasses() {
                return Collections2.transform(mappings.getClasses(), this::wrap);
            }
        };
    }

    private static final Map<Entry, Entry> METHOD_OVERWRITES = new HashMap<>();

    static {
        METHOD_OVERWRITES.put(new Entry(
                "setBurnProperties",
                "(III)V",
                "net/minecraft/class_197"
        ), new Entry(
                "Block_setBurnProperties",
                "(III)V",
                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
        ));
    }

    public static class Entry {
        public final String name;
        public final String descriptor;
        public final String owner;

        public Entry(String name, String descriptor, String owner) {
            this.name = name;
            this.descriptor = descriptor;
            this.owner = owner;
        }
    }
}
