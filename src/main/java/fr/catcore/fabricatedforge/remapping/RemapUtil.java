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
        MappingList mappings = new MappingList();
        MOD_MAPPINGS.forEach(mappings::add);

        List<String> lines = new ArrayList<>();
        lines.add(toString("v1", "official", "intermediary", "named"));
        mappings.forEach(mappingBuilder -> lines.addAll(mappingBuilder.build()));

        FileUtils.writeTextFile(lines, Constants.MOD_MAPPINGS_FILE);

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

    protected static class MappingList extends ArrayList<MappingBuilder> {
        public MappingList() {
            super();
        }

        public MappingBuilder add(String obfuscated, String intermediary) {
            MappingBuilder builder = MappingBuilder.create(obfuscated, intermediary);
            this.add(builder);
            return builder;
        }

        public MappingBuilder add(String name) {
            MappingBuilder builder = MappingBuilder.create(name);
            this.add(builder);
            return builder;
        }
    }

    private static void generateMappings() {
        if (!Constants.MAPPINGS_FILE.exists()) {
            MappingList mappings = new MappingList();

            // ModLoader mappings
            mappings.add("BaseMod", "net/minecraft/BaseMod");
            mappings.add("EntityRendererProxy", "net/minecraft/EntityRendererProxy");
            mappings.add("FMLRendererAccessLibrary", "net/minecraft/FMLRendererAccessLibrary");
            mappings.add("MLProp", "net/minecraft/MLProp");
            mappings.add("ModLoader", "net/minecraft/ModLoader");
            mappings.add("ModTextureAnimation", "net/minecraft/ModTextureAnimation");
            mappings.add("ModTextureStatic", "net/minecraft/ModTextureStatic");
            mappings.add("TradeEntry", "net/minecraft/TradeEntry");

            // Forge cursed overwrites mappings
            mappings.add("axf", "fr/catcore/fabricatedforge/forged/class_585Forged")
                    .field("g", "field_2158", "[F")
                    .field("h", "field_2159", "[F");

            mappings.add("axg", "fr/catcore/fabricatedforge/forged/class_586Forged")
                    .field("g", "field_2160", "[F")
                    .field("h", "field_2161", "[F")
                    .field("i", "field_2162", "[F")
                    .field("j", "field_2163", "[F")
                    .field("k", "field_2164", "I");

            mappings.add("axh", "fr/catcore/fabricatedforge/forged/class_587Forged")
                    .field("g", "field_2165", "[F")
                    .field("h", "field_2166", "[F")
                    .field("i", "field_2167", "[F")
                    .field("j", "field_2168", "[F");

            mappings.add("axi", "fr/catcore/fabricatedforge/forged/class_588Forged")
                    .field("g", "field_2169", "I")
                    .field("h", "field_2170", "[[B");

            mappings.add("axj", "fr/catcore/fabricatedforge/forged/class_589Forged")
                    .field("g", "field_2171", "[F")
                    .field("h", "field_2172", "[F")
                    .field("i", "field_2173", "[F")
                    .field("j", "field_2174", "[F")
                    .field("k", "field_2175", "I");

            mappings.add("axk", "fr/catcore/fabricatedforge/forged/class_590Forged")
                    .field("g", "field_2176", "[F")
                    .field("h", "field_2177", "[F")
                    .field("i", "field_2178", "[F")
                    .field("j", "field_2179", "[F")
                    .field("k", "field_2180", "I");

            mappings.add("axc", "fr/catcore/fabricatedforge/forged/ClockSpriteForged")
                    .field("g", "field_2143", "Lnet/minecraft/client/Minecraft;")
                    .field("h", "field_2144", "[I")
                    .field("i", "field_2145", "[I")
                    .field("j", "field_2146", "D")
                    .field("k", "field_2147", "D");

            mappings.add("axd", "fr/catcore/fabricatedforge/forged/CompassSpriteForged")
                    .field("g", "field_2148", "Lnet/minecraft/client/Minecraft;")
                    .field("h", "field_2149", "[I")
                    .field("i", "field_2150", "D")
                    .field("j", "field_2151", "D");

            // Player API
            mappings.add("PlayerAPI", "net/minecraft/PlayerAPI");
            mappings.add("PlayerBase", "net/minecraft/PlayerBase");
            mappings.add("PlayerBaseSorter", "net/minecraft/PlayerBaseSorter");
            mappings.add("PlayerBaseSorting", "net/minecraft/PlayerBaseSorting");
            mappings.add("ServerPlayerAPI", "net/minecraft/ServerPlayerAPI");
            mappings.add("ServerPlayerBase", "net/minecraft/ServerPlayerBase");
            mappings.add("ServerPlayerBaseSorter", "net/minecraft/ServerPlayerBaseSorter");
            mappings.add("ServerPlayerBaseSorting", "net/minecraft/ServerPlayerBaseSorting");

            mappings.add("cpw/mods/fml/client/ITextureFX")
                    .method("setErrored", "(Z)V")
                    .method("getErrored", "()Z")
                    .method("onTexturePackChanged", "(Lavf;Layi;Lxc;)V")
                    .method("onTextureDimensionsUpdate", "(II)V");

            mappings.add("cpw/mods/fml/client/FMLTextureFX")
                    .field("tileSizeBase", "I")
                    .field("tileSizeSquare", "I")
                    .field("tileSizeMask", "I")
                    .field("tileSizeSquareMask", "I")
                    .field("errored", "Z")
                    .field("log", "Ljava/util/logging/Logger;")
                    .method("<init>", "(I)V")
                    .method("setup", "()V")
                    .method("unregister", "(Lavf;Ljava/util/List;)Z");

            List<String> lines = new ArrayList<>();

            lines.add(toString("v1", "official", "intermediary", "named"));

            mappings.forEach(mappingBuilder -> lines.addAll(mappingBuilder.build()));

            FileUtils.writeTextFile(lines, Constants.MAPPINGS_FILE);
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
                            public void visitTypeInsn(int opcode, String type) {
                                switch (opcode) {
                                    case Opcodes.NEW:

                                        switch (type) {
                                            case "net/minecraft/class_1041":
                                                type = "fr/catcore/fabricatedforge/forged/ItemGroupForged";
                                                break;
                                            case "net/minecraft/class_847":
                                                type = "fr/catcore/fabricatedforge/forged/WeightedRandomChestContentForged";
                                                break;
                                        }

                                        break;
                                }

                                super.visitTypeInsn(opcode, type);
                            }

                            @Override
                            public void visitFieldInsn(int opcode, String fieldOwner, String fieldName, String fieldDescriptor) {
                                switch (fieldOwner) {
                                    case "forestry/core/render/TextureHabitatLocatorFX":
                                        if (fieldName.equals("f")) fieldName = "field_2157";
                                        else if (fieldName.equals("b")) fieldName = "field_2153";
                                        break;
                                    case "powercrystals/minefactoryreloaded/client/TextureFrameAnimFX":
                                        if (fieldName.equals("a")) fieldName = "field_2152";
                                        break;
                                    case "powercrystals/minefactoryreloaded/client/TextureLiquidFX":
                                        if (fieldName.equals("e")) fieldName = "field_2156";
                                        else if (fieldName.equals("a")) fieldName = "field_2152";
                                        break;
                                    case "buildcraft/energy/render/TextureOilFlowFX":
                                        if (fieldName.equals("e")) fieldName = "field_2156";
                                        break;
                                    case "xcompwiz/mystcraft/Mystcraft":
                                        if (fieldName.equals("registeredDims")) {
                                            fieldOwner = "fr/catcore/fabricatedforge/compat/MystcraftCompat";
                                        }
                                        break;
                                }

                                super.visitFieldInsn(opcode, fieldOwner, fieldName, fieldDescriptor);
                            }

                            @Override
                            public void visitLdcInsn(Object value) {
                                if (value instanceof String) {
                                    String stringValue = (String) value;

                                    switch (cls.getName()) {
                                        case "InvTweaksLocalization":
                                            if (name.equals("load") && stringValue.startsWith("invtweaks")) {
                                                value = "/" + stringValue;
                                            }
                                            break;

                                        case "InvTweaksObfuscation":
                                            if (stringValue.equals("b")) value = "field_1386";
                                            else if (stringValue.equals("k")) value = "field_1981";
                                            break;

                                        case "peterix/friendsss/Friendsss":
                                            if (stringValue.equals("d")) {
                                                value = "field_3919";
                                            }
                                            break;

                                        case "mod_GlowstoneSeeds":
                                            if (stringValue.equals("glowstoneseed.png")) {
                                                value = "/glowstone seeds 1.3.2/glowstoneseed.png";
                                            }
                                            break;

                                        case "mod_SmartMoving":
                                            if (name.equals("<init>") && stringValue.equals("e")) {
                                                value = "field_2897";
                                            }
                                            break;

                                        case "net/minecraft/move/SmartMovingContext":
                                        case "net/smart/moving/SmartMovingContext":
                                        case "net/smart/render/SmartRenderContext":
                                            if (name.equals("<clinit>") && stringValue.equals("P")) {
                                                value = "field_3774";
                                            } else if (name.equals("registerAnimation") && stringValue.equals("o")) {
                                                value = "field_2108";
                                            } else if (name.equals("TranslateIfNecessary") && stringValue.equals("b")) {
                                                value = "field_618";
                                            }
                                            break;

                                        case "net/minecraft/move/render/ModelRotationRenderer":
                                        case "net/smart/moving/render/ModelRotationRenderer":
                                        case "net/smart/render/ModelRotationRenderer":
                                            if (name.equals("<clinit>")) {
                                                switch (stringValue) {
                                                    case "q":
                                                        value = "field_1611";
                                                        break;
                                                    case "d":
                                                        value = "method_1196";
                                                        break;
                                                    case "r":
                                                        value = "field_1612";
                                                        break;
                                                }
                                            }
                                            break;

                                        case "net/minecraft/move/render/RenderPlayer":
                                        case "net/smart/moving/render/RenderPlayer":
                                            if (name.equals("initialize")) {
                                                switch (stringValue) {
                                                    case "a":
                                                        value = "field_2133";
                                                        break;
                                                    case "b":
                                                        value = "field_2134";
                                                        break;
                                                    case "i":
                                                        value = "field_2135";
                                                        break;
                                                }
                                            }
                                            break;

                                        case "net/minecraft/move/playerapi/NetServerHandler":
                                        case "net/smart/moving/playerapi/NetServerHandler":
                                            if (name.equals("<clinit>")) {
                                                switch (stringValue) {
                                                    case "e":
                                                        value = "field_2897";
                                                        break;
                                                    case "d":
                                                        value = "field_2896";
                                                        break;
                                                    case "connections":
                                                        value = "field_2923";
                                                        break;
                                                }
                                            }
                                            break;

                                        case "net/minecraft/move/SmartMovingSelf":
                                        case "net/smart/moving/SmartMovingSelf":
                                            if (name.equals("<clinit>") && stringValue.equals("c")) {
                                                value = "field_1059";
                                            }
                                            break;

                                        case "net/minecraft/move/config/SmartMovingOptions":
                                        case "net/smart/moving/config/SmartMovingOptions":
                                            if (name.equals("<clinit>") && stringValue.equals("k")) {
                                                value = "field_1656";
                                            }
                                            break;
                                    }
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

        METHOD_OVERWRITES.put(new Entry(
                "<init>",
                "(Ljava/lang/String;)V",
                "net/minecraft/class_1041"
        ), new Entry(
                "<init>",
                "(Ljava/lang/String;)V",
                "fr/catcore/fabricatedforge/forged/ItemGroupForged"
        ));
        METHOD_OVERWRITES.put(new Entry(
                "<init>",
                "(ILjava/lang/String;)V",
                "net/minecraft/class_1041"
        ), new Entry(
                "<init>",
                "(ILjava/lang/String;)V",
                "fr/catcore/fabricatedforge/forged/ItemGroupForged"
        ));

        METHOD_OVERWRITES.put(new Entry(
                "<init>",
                "(Lnet/minecraft/class_1071;III)V",
                "net/minecraft/class_847"
        ), new Entry(
                "<init>",
                "(Lnet/minecraft/class_1071;III)V",
                "fr/catcore/fabricatedforge/forged/WeightedRandomChestContentForged"
        ));
        METHOD_OVERWRITES.put(new Entry(
                "<init>",
                "(IIIII)V",
                "net/minecraft/class_847"
        ), new Entry(
                "<init>",
                "(IIIII)V",
                "fr/catcore/fabricatedforge/forged/WeightedRandomChestContentForged"
        ));

        METHOD_OVERWRITES.put(new Entry(
                "c",
                "(F)Lnet/minecraft/class_197;",
                "gregtechmod/common/blocks/BlockFixedITNT"
        ), new Entry(
                "method_442",
                "(F)Lnet/minecraft/class_197;",
                "gregtechmod/common/blocks/BlockFixedITNT"
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
