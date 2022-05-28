package fr.catcore.fabricatedforge.remapping;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import fr.catcore.fabricatedforge.utils.BArrayList;
import fr.catcore.fabricatedforge.utils.Constants;
import fr.catcore.fabricatedforge.utils.FileUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.util.mappings.TinyRemapperMappingsHelper;
import net.fabricmc.mapping.reader.v2.TinyMetadata;
import net.fabricmc.mapping.tree.*;
import net.fabricmc.tinyremapper.*;
import net.fabricmc.tinyremapper.api.TrClass;
import org.objectweb.asm.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class RemapUtil {
    private static TinyTree LOADER_TREE;
    private static TinyTree MINECRAFT_TREE;
    private static TinyTree MODS_TREE;

    private static final Map<String, String> MOD_MAPPINGS = new HashMap<>();

    private static final BArrayList<String> FORGE_EXCLUDED = new BArrayList<>();

    public static void init() {
        downloadForgeAndPatchIt();
        generateMappings();
        LOADER_TREE = makeTree(Constants.MAPPINGS_FILE);
        MINECRAFT_TREE = FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings();
    }

    private static void downloadForgeAndPatchIt() {
        try {
            if (!Constants.FORGE_FILE.exists()) {
                try (BufferedInputStream inputStream = new BufferedInputStream(new URL(Constants.FORGE_URL).openStream())) {
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(Constants.FORGE_FILE.toPath()))) {
                        byte[] buffer = new byte[2048];

                        // Increments file size
                        int length;
                        int downloaded = 0;

                        // Looping until server finishes
                        while ((length = inputStream.read(buffer)) != -1) {
                            // Writing data
                            outputStream.write(buffer, 0, length);
                            downloaded += length;
                            //System.out.println("Downlad Status: " + (downloaded * 100) / (contentLength * 1.0) + "%");
                        }

                        outputStream.close();
                        inputStream.close();
                    }
                }

                File file = Constants.FORGE_FILE;

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
                    boolean toBeDeleted = FORGE_EXCLUDED.contains(zipEntryName.replace(".class", ""));

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void remapMods(Map<Path, Path> pathMap) {
        TinyRemapper remapper = makeRemapper(MINECRAFT_TREE, LOADER_TREE, MODS_TREE);
        remapFiles(remapper, pathMap);
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

            mappings.add("net/minecraftforge/common/ISidedInventory")
                    .method("getStartInventorySide", "(Lnet/minecraftforge/common/ForgeDirection;)I")
                    .method("getSizeInventorySide", "(Lnet/minecraftforge/common/ForgeDirection;)I");

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
                        String finalName = name;
                        return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, finalName, descriptor, signature, exceptions)) {
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
                                            // Forge
                                            case "net/minecraft/class_1041":
                                                type = "fr/catcore/fabricatedforge/forged/ItemGroupForged";
                                                break;
                                            case "net/minecraft/class_847":
                                                type = "fr/catcore/fabricatedforge/forged/WeightedRandomChestContentForged";
                                                break;
                                            case "net/minecraft/class_1196":
                                                type = "fr/catcore/fabricatedforge/forged/ChunkForged";
                                                break;
                                        }

                                        break;
                                }

                                super.visitTypeInsn(opcode, type);
                            }

                            @Override
                            public void visitFieldInsn(int opcode, String fieldOwner, String fieldName, String fieldDescriptor) {
                                switch (fieldOwner) {
                                    // Mystcraft
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
                                        // Inventory Tweaks
                                        case "InvTweaksLocalization":
                                            if (finalName.equals("load") && stringValue.startsWith("invtweaks")) {
                                                value = "/" + stringValue;
                                            }
                                            break;

                                        case "InvTweaksObfuscation":
                                            if (stringValue.equals("b")) value = "field_1386";
                                            else if (stringValue.equals("k")) value = "field_1981";
                                            break;

                                        // Friendssss
                                        case "peterix/friendsss/Friendsss":
                                            if (stringValue.equals("d")) {
                                                value = "field_3919";
                                            }
                                            break;

                                        // Glowstone Seeds
                                        case "mod_GlowstoneSeeds":
                                            if (stringValue.equals("glowstoneseed.png")) {
                                                value = "/glowstone seeds 1.3.2/glowstoneseed.png";
                                            }
                                            break;

                                        // Smart Moving
                                        case "mod_SmartMoving":
                                            if (finalName.equals("<init>") && stringValue.equals("e")) {
                                                value = "field_2897";
                                            }
                                            break;

                                        case "net/minecraft/move/SmartMovingContext":
                                        case "net/smart/moving/SmartMovingContext":
                                        case "net/smart/render/SmartRenderContext":
                                            if (finalName.equals("<clinit>") && stringValue.equals("P")) {
                                                value = "field_3774";
                                            } else if (finalName.equals("registerAnimation") && stringValue.equals("o")) {
                                                value = "field_2108";
                                            } else if (finalName.equals("TranslateIfNecessary") && stringValue.equals("b")) {
                                                value = "field_618";
                                            }
                                            break;

                                        case "net/minecraft/move/render/ModelRotationRenderer":
                                        case "net/smart/moving/render/ModelRotationRenderer":
                                        case "net/smart/render/ModelRotationRenderer":
                                            if (finalName.equals("<clinit>")) {
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
                                            if (finalName.equals("initialize")) {
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
                                            if (finalName.equals("<clinit>")) {
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
                                            if (finalName.equals("<clinit>") && stringValue.equals("c")) {
                                                value = "field_1059";
                                            }
                                            break;

                                        case "net/minecraft/move/config/SmartMovingOptions":
                                        case "net/smart/moving/config/SmartMovingOptions":
                                            if (finalName.equals("<clinit>") && stringValue.equals("k")) {
                                                value = "field_1656";
                                            }
                                            break;

                                        // Portal Gun
                                        case "portalgun/client/core/TickHandlerClient":
                                            if (finalName.equals("renderTick")) {
                                                switch (stringValue) {
                                                    case "equippedProgress":
                                                        value = "field_1878";
                                                        break;
                                                    case "prevEquippedProgress":
                                                        value = "field_1879";
                                                        break;
                                                    case "itemToRender":
                                                        value = "field_1877";
                                                        break;
                                                    case "equippedItemSlot":
                                                        value = "field_1882";
                                                        break;
                                                }
                                            }
                                            break;

                                        case "portalgun/common/core/CommonProxy":
                                            if (finalName.equals("getWorldDir") && stringValue.equals("chunkSaveLocation")) {
                                                value = "field_4782";
                                            }
                                            break;

                                        case "portalgun/client/render/TileRendererPortalMod":
                                            if (finalName.equals("updateTexture")) {
                                                switch (stringValue) {
                                                    case "camRoll":
                                                        value = "field_1826";
                                                        break;
                                                    case "prevCamRoll":
                                                        value = "field_1827";
                                                        break;
                                                    case "fovModifierHand":
                                                        value = "field_1829";
                                                        break;
                                                    case "fovModifierHandPrev":
                                                        value = "field_1830";
                                                        break;
                                                    case "cameraZoom":
                                                        value = "field_1833";
                                                        break;
                                                }
                                            }
                                            break;

                                        // GregTech
                                        case "gregtechmod/common/gui/GT_GUIContainer_AESU":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Centrifuge":
                                        case "gregtechmod/common/gui/GT_GUIContainer_ChargeOMat":
                                        case "gregtechmod/common/gui/GT_GUIContainer_ComputerCube":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Destructopack":
                                        case "gregtechmod/common/gui/GT_GUIContainer_ECraftingtable":
                                        case "gregtechmod/common/gui/GT_GUIContainer_ElectricBufferLarge":
                                        case "gregtechmod/common/gui/GT_GUIContainer_ElectricBufferSmall":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Fusionreactor":
                                        case "gregtechmod/common/gui/GT_GUIContainer_IDSU":
                                        case "gregtechmod/common/gui/GT_GUIContainer_LESU":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Matterfabricator":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Quantumchest":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Sonictron":
                                        case "gregtechmod/common/gui/GT_GUIContainer_Translocator":
                                        case "gregtechmod/common/gui/GT_GUIContainer_UUMAssembler":
                                        case "gregtechmod/common/gui/GT_GUIContainerMetaID_Machine":
                                        case "gregtechmod/common/gui/GT_GUIContainerMetaTile_Machine":
                                            if (stringValue.startsWith("gregtechmod/")) {
                                                value = "/" + stringValue;
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
        remapper.readClassPath((Path) FabricLoader.getInstance().getObjectShare().get("fabric-loader:inputGameJar"), Constants.FORGE_FILE.toPath());
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
     */
    private static void remapFiles(TinyRemapper remapper, Map<Path, Path> paths) {
        try {
            Map<Path, InputTag> tagMap = new HashMap<>();

            for (Path input : paths.keySet()) {
                InputTag tag = remapper.createInputTag();
                tagMap.put(input, tag);
                remapper.readInputs(tag, input);
            }


            for (Map.Entry<Path, Path> entry : paths.entrySet()) {
                OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(entry.getValue()).build();

                outputConsumer.addNonClassFiles(entry.getKey(), NonClassCopyMode.UNCHANGED, remapper);

                remapper.apply(outputConsumer, tagMap.get(entry.getKey()));
                outputConsumer.close();
            }

            remapper.finish();
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
        // Forge
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
                "<init>",
                "(Lnet/minecraft/class_1150;II)V",
                "net/minecraft/class_1196"
        ), new Entry(
                "<init>",
                "(Lnet/minecraft/class_1150;II)V",
                "fr/catcore/fabricatedforge/forged/ChunkForged"
        ));
        METHOD_OVERWRITES.put(new Entry(
                "<init>",
                "(Lnet/minecraft/class_1150;[BII)V",
                "net/minecraft/class_1196"
        ), new Entry(
                "<init>",
                "(Lnet/minecraft/class_1150;[BII)V",
                "fr/catcore/fabricatedforge/forged/ChunkForged"
        ));
        METHOD_OVERWRITES.put(new Entry(
                "<init>",
                "(Lnet/minecraft/class_1150;[B[BII)V",
                "net/minecraft/class_1196"
        ), new Entry(
                "<init>",
                "(Lnet/minecraft/class_1150;[B[BII)V",
                "fr/catcore/fabricatedforge/forged/ChunkForged"
        ));

        // ExtraBiomesXL
//        METHOD_OVERWRITES.put(new Entry(
//                "setBurnProperties",
//                "(III)V",
//                "extrabiomes/module/summa/block/BlockCustomTallGrass"
//        ), new Entry(
//                "Block_setBurnProperties",
//                "(III)V",
//                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
//        ));
//        METHOD_OVERWRITES.put(new Entry(
//                "setBurnProperties",
//                "(III)V",
//                "extrabiomes/module/summa/block/BlockCustomLog"
//        ), new Entry(
//                "Block_setBurnProperties",
//                "(III)V",
//                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
//        ));
//        METHOD_OVERWRITES.put(new Entry(
//                "setBurnProperties",
//                "(III)V",
//                "extrabiomes/module/summa/block/BlockQuarterLog"
//        ), new Entry(
//                "Block_setBurnProperties",
//                "(III)V",
//                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
//        ));
//        METHOD_OVERWRITES.put(new Entry(
//                "setBurnProperties",
//                "(III)V",
//                "extrabiomes/module/fabrica/block/BlockCustomWood"
//        ), new Entry(
//                "Block_setBurnProperties",
//                "(III)V",
//                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
//        ));
//        METHOD_OVERWRITES.put(new Entry(
//                "setBurnProperties",
//                "(III)V",
//                "extrabiomes/module/fabrica/block/BlockCustomWoodSlab"
//        ), new Entry(
//                "Block_setBurnProperties",
//                "(III)V",
//                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
//        ));
//        METHOD_OVERWRITES.put(new Entry(
//                "setBurnProperties",
//                "(III)V",
//                "extrabiomes/module/fabrica/block/BlockWoodStairs"
//        ), new Entry(
//                "Block_setBurnProperties",
//                "(III)V",
//                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
//        ));

        FORGE_EXCLUDED.put("a")
                .put("aad")
                .put("aae")
                .put("aan")
                .put("aar")
                .put("aaw")
                .put("abk")
                .put("abu")
                .put("acv")
                .put("adt")
                .put("adx")
                .put("aeb")
                .put("aed")
                .put("aez")
                .put("afa")
                .put("afb")
                .put("afe")
                .put("afj")
                .put("afp")
                .put("afq")
                .put("afu")
                .put("afv")
                .put("afy")
                .put("agb")
                .put("agj")
                .put("agk")
                .put("agm")
                .put("agv")
                .put("agx")
                .put("agy")
                .put("ahh")
                .put("ahi")
                .put("ahl")
                .put("aho")
                .put("ahy")
                .put("aic")
                .put("aig")
                .put("aig$1")
                .put("ail")
                .put("aim")
                .put("aio")
                .put("aip")
                .put("aiq")
                .put("ais")
                .put("aiy")
                .put("ajd")
                .put("aji")
                .put("ajj")
                .put("ajq")
                .put("ak")
                .put("amx")
                .put("anz")
                .put("aon")
                .put("aoo")
                .put("aou")
                .put("aow")
                .put("app")
                .put("apz")
                .put("aqn")
                .put("art")
                .put("arw")
                .put("ash")
                .put("aso")
                .put("ast")
                .put("asv")
                .put("atc")
                .put("atd")
                .put("aub")
                .put("aum")
                .put("aus")
                .put("auw")
                .put("av")
                .put("ava")
                .put("avb")
                .put("ave")
                .put("avf")
                .put("avg")
                .put("avy")
                .put("awg")
                .put("awh")
                .put("awr")
                .put("awv")
                .put("axc")
                .put("axd")
                .put("axf")
                .put("axg")
                .put("axh")
                .put("axi")
                .put("axj")
                .put("axk")
                .put("axp")
                .put("axs")
                .put("axv")
                .put("axy")
                .put("ayq")
                .put("ayr")
                .put("ays")
                .put("ba")
                .put("bb")
                .put("cn")
                .put("cp")
                .put("cs")
                .put("db")
                .put("dc")
                .put("el")
                .put("et")
                .put("ft")
                .put("fy")
                .put("ge")
                .put("gm")
                .put("gp")
                .put("gq")
                .put("gr")
                .put("gu")
                .put("gv")
                .put("gw")
                .put("gx")
                .put("gz")
                .put("ha")
                .put("hu")
                .put("it")
                .put("jj")
                .put("jn")
                .put("jw")
                .put("lc")
                .put("mr")
                .put("ms")
                .put("mu")
                .put("nd")
                .put("nj")
                .put("nk")
                .put("ny")
                .put("o")
                .put("od")
                .put("og")
                .put("pg")
                .put("ph")
                .put("pq")
                .put("pz")
                .put("qb")
                .put("qg")
                .put("qt")
                .put("qv")
                .put("rg")
                .put("rh")
                .put("rl")
                .put("rm")
                .put("ro")
                .put("rz")
                .put("sa")
                .put("si")
                .put("tb")
                .put("td")
                .put("ts")
                .put("tu")
                .put("um")
                .put("up")
                .put("va")
                .put("vc")
                .put("ve")
                .put("wl")
                .put("wy")
                .put("xc")
                .put("xr")
                .put("xw")
                .put("ya")
                .put("yf")
                .put("yi")
                .put("yj")
                .put("yk")
                .put("yl")
                .put("yr")
                .put("ys")
                .put("yt")
                .put("yu")
                .put("za");
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
