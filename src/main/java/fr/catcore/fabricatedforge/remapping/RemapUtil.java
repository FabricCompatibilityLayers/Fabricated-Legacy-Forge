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
import org.objectweb.asm.Type;

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

        classes.forEach(cl -> MOD_MAPPINGS.put(cl, "net/minecraft/" + cl));

        return files;
    }

    public static void generateModMappings() {
        List<String> mappings = new ArrayList<>();
        mappings.add(toString("v1", "official", "intermediary", "named"));
        MOD_MAPPINGS.forEach((cl, remapped) -> mappings.add(makeLoaderLine(cl, remapped)));
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
            mappings.add(makeLoaderLine("BaseMod", "net/minecraft/BaseMod"));
            mappings.add(makeLoaderLine("EntityRendererProxy", "net/minecraft/EntityRendererProxy"));
            mappings.add(makeLoaderLine("FMLRendererAccessLibrary", "net/minecraft/FMLRendererAccessLibrary"));
            mappings.add(makeLoaderLine("MLProp", "net/minecraft/MLProp"));
            mappings.add(makeLoaderLine("ModLoader", "net/minecraft/ModLoader"));
            mappings.add(makeLoaderLine("ModTextureAnimation", "net/minecraft/ModTextureAnimation"));
            mappings.add(makeLoaderLine("ModTextureStatic", "net/minecraft/ModTextureStatic"));
            mappings.add(makeLoaderLine("TradeEntry", "net/minecraft/TradeEntry"));

            // Forge cursed overwrites mappings
            mappings.add(makeLoaderLine("net/minecraft/class_585", "fr/catcore/fabricatedforge/forged/class_585Forged"));
            mappings.add(makeLoaderLine("net/minecraft/class_586", "fr/catcore/fabricatedforge/forged/class_586Forged"));
            mappings.add(makeLoaderLine("net/minecraft/class_587", "fr/catcore/fabricatedforge/forged/class_587Forged"));
            mappings.add(makeLoaderLine("net/minecraft/class_588", "fr/catcore/fabricatedforge/forged/class_588Forged"));
            mappings.add(makeLoaderLine("net/minecraft/class_589", "fr/catcore/fabricatedforge/forged/class_589Forged"));
            mappings.add(makeLoaderLine("net/minecraft/class_590", "fr/catcore/fabricatedforge/forged/class_590Forged"));
            mappings.add(makeLoaderLine("net/minecraft/class_582", "fr/catcore/fabricatedforge/forged/ClockSpriteForged"));
            mappings.add(makeLoaderLine("net/minecraft/class_583", "fr/catcore/fabricatedforge/forged/CompassSpriteForged"));
            mappings.add(makeLoaderLine("net/minecraft/class_1041", "fr/catcore/fabricatedforge/forged/ItemGroupForged"));

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

    private static String makeLoaderLine(String from, String to) {
        return toString("CLASS", from, to, to);
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
}
