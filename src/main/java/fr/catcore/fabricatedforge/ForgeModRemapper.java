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
                .put("aaf")
                .put("aaj")
                .put("aaq")
                .put("aau")
                .put("aav")
                .put("aay")
                .put("abd")
                .put("abh")
                .put("abm")
                .put("abp")
                .put("abq")
                .put("abr")
                .put("abs")
                .put("aby")
                .put("abz")
                .put("aca")
                .put("acb")
                .put("acj")
                .put("adm")
                .put("adn")
                .put("adr")
                .put("adx")
                .put("aeb")
                .put("aeg")
                .put("aex")
                .put("afh")
                .put("afi")
                .put("afl")
                .put("agi")
                .put("agp")
                .put("ahg")
                .put("ahk")
                .put("aho")
                .put("ahq")
                .put("aix")
                .put("aiy")
                .put("aiz")
                .put("ajd")
                .put("ajj")
                .put("ajq")
                .put("aju")
                .put("ajv")
                .put("ajy")
                .put("akc")
                .put("akl")
                .put("akm")
                .put("ako")
                .put("akq")
                .put("akx")
                .put("akz")
                .put("ala")
                .put("alk")
                .put("all")
                .put("alo")
                .put("alr")
                .put("alt")
                .put("amb")
                .put("amf")
                .put("amj")
                .put("amj$1")
                .put("amp")
                .put("amq")
                .put("ams")
                .put("amt")
                .put("amu")
                .put("amw")
                .put("and")
                .put("ane")
                .put("ank")
                .put("anq")
                .put("ans")
                .put("anz")
                .put("arf")
                .put("asl")
                .put("asz")
                .put("atc")
                .put("ati")
                .put("atk")
                .put("aug")
                .put("auq")
                .put("auy")
                .put("avl")
                .put("awt")
                .put("aww")
                .put("axi")
                .put("axs")
                .put("axx")
                .put("axz")
                .put("ayc")
                .put("ayg")
                .put("ayh")
                .put("azf")
                .put("azr")
                .put("azy")
                .put("bac")
                .put("baj")
                .put("bak")
                .put("bao")
                .put("bap")
                .put("baq")
                .put("bbj")
                .put("bbr")
                .put("bbt")
                .put("bcd")
                .put("bci")
                .put("bct")
                .put("bcu")
                .put("bcw")
                .put("bcx")
                .put("bcy")
                .put("bcz")
                .put("bda")
                .put("bdb")
                .put("bdh")
                .put("bdo")
                .put("bdr")
                .put("bek")
                .put("bel")
                .put("bem")
                .put("bn")
                .put("ca")
                .put("cf")
                .put("cg")
                .put("ds")
                .put("du")
                .put("dx")
                .put("eg")
                .put("eh")
                .put("fr")
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
                .put("qx")
                .put("sc")
                .put("sd")
                .put("sp")
                .put("sq")
                .put("su")
                .put("su$1")
                .put("sz")
                .put("tb")
                .put("th")
                .put("tu")
                .put("tw")
                .put("ty")
                .put("uj")
                .put("uk")
                .put("ul")
                .put("ul$1")
                .put("uo")
                .put("up")
                .put("vc")
                .put("vd")
                .put("vl")
                .put("wd")
                .put("wh")
                .put("ww")
                .put("wy")
                .put("x")
                .put("xq")
                .put("xv")
                .put("yg")
                .put("yi")
                .put("yl")
                .put("yr")
                .put("yv")
                .put("yw")
                .put("zs");
    }

    public static class BArrayList extends fr.catcore.modremapperapi.utils.BArrayList<String> {

        @Override
        public BArrayList put(String entry) {
            return (BArrayList) super.put(entry + ".class");
        }
    }
}
