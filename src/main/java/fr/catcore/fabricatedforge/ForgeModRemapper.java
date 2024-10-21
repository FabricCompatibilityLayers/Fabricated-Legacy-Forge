package fr.catcore.fabricatedforge;

import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.MappingBuilder;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.ModRemapper;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.RemapLibrary;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.VisitorInfos;
import net.fabricmc.api.EnvType;

import java.util.List;
import java.util.Optional;

public class ForgeModRemapper implements ModRemapper {

    private static final BArrayList FORGE_EXCLUDED = new BArrayList();
    
    @Override
    public String[] getJarFolders() {
        return new String[] {"coremods"};
    }

    @Override
    public void addRemapLibraries(List<RemapLibrary> list, EnvType envType) {
        list.add(new io.github.fabriccompatibiltylayers.modremappingapi.api.v1.RemapLibrary(
                Constants.FORGE_URL,
                FORGE_EXCLUDED,
                "forge.zip"
        ));
    }

    @Override
    public void registerMappings(MappingBuilder mappingBuilder) {
        // ModLoader mappings
        mappingBuilder.addMapping("BaseMod", "net/minecraft/BaseMod");
        mappingBuilder.addMapping("EntityRendererProxy", "net/minecraft/EntityRendererProxy");
        mappingBuilder.addMapping("FMLRendererAccessLibrary", "net/minecraft/FMLRendererAccessLibrary");
        mappingBuilder.addMapping("MLProp", "net/minecraft/MLProp");
        mappingBuilder.addMapping("ModLoader", "net/minecraft/ModLoader");
        mappingBuilder.addMapping("ModTextureAnimation", "net/minecraft/ModTextureAnimation");
        mappingBuilder.addMapping("ModTextureStatic", "net/minecraft/ModTextureStatic");
        mappingBuilder.addMapping("TradeEntry", "net/minecraft/TradeEntry");
    }

    @Override
    public void registerPreVisitors(VisitorInfos visitorInfos) {

    }

    @Override
    public void registerPostVisitors(VisitorInfos visitorInfos) {
        // Reflection Remappers
        visitorInfos.registerInstantiation(
                "org/objectweb/asm/tree/FieldInsnNode",
                "fr/catcore/fabricatedforge/compat/asm/BetterFieldInsnNode"
        );
        visitorInfos.registerInstantiation(
                "org/objectweb/asm/tree/MethodInsnNode",
                "fr/catcore/fabricatedforge/compat/asm/BetterMethodInsnNode"
        );
        visitorInfos.registerInstantiation(
                "org/objectweb/asm/ClassWriter",
                "fr/catcore/fabricatedforge/compat/asm/BetterClassWriter"
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getDeclaredMethod",
                "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getDeclaredMethod",
                        "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getDeclaredField",
                "(Ljava/lang/String;)Ljava/lang/reflect/Field;",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getDeclaredField",
                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getMethod",
                "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getMethod",
                        "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "getField",
                "(Ljava/lang/String;)Ljava/lang/reflect/Field;",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getField",
                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "forName",
                "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "forName",
                        "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
                        true
                )
        );
        visitorInfos.registerMethodInvocation(
                "java/lang/Class",
                "forName",
                "(Ljava/lang/String;)Ljava/lang/Class;",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "forName",
                        "(Ljava/lang/String;)Ljava/lang/Class;",
                        true
                )
        );

        // Forge added fields and methods
        visitorInfos.registerFieldRef(
                "net/minecraft/class_197",
                "blockFireSpreadSpeed",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedBlock",
                        "blockFireSpreadSpeed",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_197",
                "blockFlammability",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedBlock",
                        "blockFlammability",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_1150",
                "MAX_ENTITY_RADIUS",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedWorld",
                        "MAX_ENTITY_RADIUS",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_1160",
                "base11Biomes",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedLevelGeneratorType",
                        "base11Biomes",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_1160",
                "base12Biomes",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedLevelGeneratorType",
                        "base12Biomes",
                        null
                )
        );
        visitorInfos.registerMethodInvocation(
                "net/minecraft/class_469",
                "setConnectionCompatibilityLevel",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/Reflectedclass_469",
                        "setConnectionCompatibilityLevel",
                        null
                )
        );
        visitorInfos.registerMethodInvocation(
                "net/minecraft/class_469",
                "getConnectionCompatibilityLevel",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/Reflectedclass_469",
                        "getConnectionCompatibilityLevel",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_9",
                "allowedBiomes",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedStrongholdStructure",
                        "allowedBiomes",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_1175",
                "allowedBiomes",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedLayeredBiomeSource",
                        "allowedBiomes",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_570",
                "NAME_TAG_RANGE",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedPlayerEntityRenderer",
                        "NAME_TAG_RANGE",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_570",
                "NAME_TAG_RANGE_SNEAK",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedPlayerEntityRenderer",
                        "NAME_TAG_RANGE_SNEAK",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_988",
                "PERSISTED_NBT_TAG",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedPlayerEntity",
                        "PERSISTED_NBT_TAG",
                        null
                )
        );

        // Mod specific fixes
    }

    @Override
    public Optional<String> getDefaultPackage() {
        return Optional.of("net/minecraft/");
    }

    @Override
    public void afterRemap() {

    }

    static {
        FORGE_EXCLUDED.put("a")
                .put("aam")
                .put("aaq")
                .put("aax")
                .put("abb")
                .put("abc")
                .put("abe")
                .put("abf")
                .put("abk")
                .put("abo")
                .put("abt")
                .put("abw")
                .put("abx")
                .put("aby")
                .put("abz")
                .put("acf")
                .put("acg")
                .put("ach")
                .put("aci")
                .put("acq")
                .put("adt")
                .put("adu")
                .put("ady")
                .put("aee")
                .put("aei")
                .put("aen")
                .put("afe")
                .put("afo")
                .put("afp")
                .put("afs")
                .put("agp")
                .put("agw")
                .put("ahn")
                .put("ahr")
                .put("ahv")
                .put("ahx")
                .put("aje")
                .put("ajf")
                .put("ajg")
                .put("ajk")
                .put("ajn")
                .put("ajq")
                .put("ajx")
                .put("akb")
                .put("akc")
                .put("akf")
                .put("akj")
                .put("aks")
                .put("akt")
                .put("akv")
                .put("akx")
                .put("ale")
                .put("alg")
                .put("alh")
                .put("alr")
                .put("als")
                .put("alv")
                .put("aly")
                .put("ama")
                .put("amc")
                .put("ami")
                .put("amm")
                .put("amq")
                .put("amq$1")
                .put("amw")
                .put("amx")
                .put("amz")
                .put("ana")
                .put("anb")
                .put("and")
                .put("ank")
                .put("anl")
                .put("anr")
                .put("any")
                .put("aoa")
                .put("aoh")
                .put("arn")
                .put("ast")
                .put("atg")
                .put("atj")
                .put("atp")
                .put("atr")
                .put("aun")
                .put("_aux")
                .put("avf")
                .put("avs")
                .put("avu")
                .put("axa")
                .put("axd")
                .put("axq")
                .put("aya")
                .put("ayf")
                .put("ayh")
                .put("ayk")
                .put("ayn")
                .put("ayo")
                .put("ayp")
                .put("azi")
                .put("azr")
                .put("bac")
                .put("baj")
                .put("ban")
                .put("bau")
                .put("bav")
                .put("baz")
                .put("bba")
                .put("bbb")
                .put("bbu")
                .put("bcc")
                .put("bce")
                .put("bco")
                .put("bct")
                .put("bde")
                .put("bdf")
                .put("bdh")
                .put("bdi")
                .put("bdj")
                .put("bdk")
                .put("bdl")
                .put("bdm")
                .put("bdo")
                .put("bds")
                .put("bdz")
                .put("bec")
                .put("bev")
                .put("bex")
                .put("bey")
                .put("bez")
                .put("bfe")
                .put("bn")
                .put("ca")
                .put("cf")
                .put("cg")
                .put("cv")
                .put("dr")
                .put("dt")
                .put("dw")
                .put("ef")
                .put("eg")
                .put("fq")
                .put("gm")
                .put("ho")
                .put("hu")
                .put("i")
                .put("ia")
                .put("ii")
                .put("il")
                .put("im")
                .put("in")
                .put("iq")
                .put("ir")
                .put("is")
                .put("it")
                .put("iv")
                .put("iw")
                .put("jq")
                .put("jt")
                .put("kw")
                .put("lm")
                .put("lq")
                .put("lv")
                .put("md")
                .put("pb")
                .put("pc")
                .put("pe")
                .put("pl")
                .put("pp")
                .put("px")
                .put("py")
                .put("qo")
                .put("qu")
                .put("qw")
                .put("qx")
                .put("ru")
                .put("sd")
                .put("se")
                .put("sg")
                .put("sq")
                .put("sr")
                .put("sv")
                .put("sv$1")
                .put("tb")
                .put("td")
                .put("tj")
                .put("tw")
                .put("ty")
                .put("ua")
                .put("uo")
                .put("up")
                .put("uq")
                .put("uq$1")
                .put("ut")
                .put("uu")
                .put("vg")
                .put("vh")
                .put("vi")
                .put("vq")
                .put("wh")
                .put("wj")
                .put("wn")
                .put("x")
                .put("xc")
                .put("xe")
                .put("xx")
                .put("yc")
                .put("yn")
                .put("yp")
                .put("ys")
                .put("yy")
                .put("zc")
                .put("zd")
                .put("zz");
    }

    public static class BArrayList extends fr.catcore.modremapperapi.utils.BArrayList<String> {

        @Override
        public BArrayList put(String entry) {
            return (BArrayList) super.put(entry + ".class");
        }
    }
}
