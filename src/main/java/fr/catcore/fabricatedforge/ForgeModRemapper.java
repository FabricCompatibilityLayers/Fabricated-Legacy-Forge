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
                .put("aag")
                .put("aal")
                .put("aap")
                .put("aau")
                .put("aax")
                .put("aay")
                .put("aaz")
                .put("aba")
                .put("abg")
                .put("abh")
                .put("abi")
                .put("abj")
                .put("abr")
                .put("acu")
                .put("acv")
                .put("acz")
                .put("adf")
                .put("adj")
                .put("ado")
                .put("aec")
                .put("aem")
                .put("afn")
                .put("agl")
                .put("agp")
                .put("agt")
                .put("agv")
                .put("aht")
                .put("ahu")
                .put("ahv")
                .put("ahz")
                .put("aif")
                .put("ail")
                .put("aim")
                .put("aiq")
                .put("air")
                .put("aiu")
                .put("aiy")
                .put("ajh")
                .put("aji")
                .put("ajk")
                .put("ajt")
                .put("ajv")
                .put("ajw")
                .put("akg")
                .put("akh")
                .put("akk")
                .put("akn")
                .put("akx")
                .put("alb")
                .put("alf")
                .put("alf$1")
                .put("all")
                .put("alm")
                .put("alo")
                .put("alp")
                .put("alq")
                .put("als")
                .put("alz")
                .put("ama")
                .put("amg")
                .put("amm")
                .put("amn")
                .put("amu")
                .put("aqa")
                .put("ard")
                .put("arr")
                .put("aru")
                .put("asa")
                .put("asc")
                .put("asy")
                .put("ati")
                .put("aud")
                .put("avl")
                .put("avo")
                .put("awa")
                .put("awk")
                .put("awp")
                .put("awr")
                .put("awy")
                .put("awz")
                .put("axx")
                .put("ayi")
                .put("ayp")
                .put("ayt")
                .put("ayx")
                .put("ayy")
                .put("azb")
                .put("azc")
                .put("azd")
                .put("azw")
                .put("bae")
                .put("bag")
                .put("baq")
                .put("bav")
                .put("bbg")
                .put("bbh")
                .put("bbj")
                .put("bbk")
                .put("bbl")
                .put("bbm")
                .put("bbn")
                .put("bbo")
                .put("bbu")
                .put("bcb")
                .put("bce")
                .put("bcx")
                .put("bcy")
                .put("bcz")
                .put("be")
                .put("br")
                .put("bw")
                .put("bx")
                .put("dj")
                .put("dl")
                .put("do")
                .put("dx")
                .put("dy")
                .put("fi")
                .put("ge")
                .put("hg")
                .put("hl")
                .put("hr")
                .put("hz")
                .put("ic")
                .put("id")
                .put("ie")
                .put("ih")
                .put("ii")
                .put("ij")
                .put("ik")
                .put("im")
                .put("in")
                .put("jh")
                .put("kh")
                .put("kx")
                .put("lb")
                .put("lf")
                .put("ln")
                .put("mt")
                .put("ol")
                .put("om")
                .put("oo")
                .put("ou")
                .put("oy")
                .put("p")
                .put("pg")
                .put("ph")
                .put("px")
                .put("qd")
                .put("qg")
                .put("rl")
                .put("rm")
                .put("ry")
                .put("si")
                .put("sk")
                .put("sq")
                .put("td")
                .put("tf")
                .put("th")
                .put("ts")
                .put("tt")
                .put("tx")
                .put("ty")
                .put("ul")
                .put("um")
                .put("uu")
                .put("vm")
                .put("vq")
                .put("wf")
                .put("wh")
                .put("wz")
                .put("xe")
                .put("xp")
                .put("xr")
                .put("xt")
                .put("ye")
                .put("za")
                .put("zm")
                .put("zn")
                .put("zr");
    }

    public static class BArrayList extends fr.catcore.modremapperapi.utils.BArrayList<String> {

        @Override
        public BArrayList put(String entry) {
            return (BArrayList) super.put(entry + ".class");
        }
    }
}
