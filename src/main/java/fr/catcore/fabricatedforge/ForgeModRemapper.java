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
        visitorInfos.registerMethodInvocation(
                "net/minecraft/class_197",
                "setBurnProperties",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedWorld",
                        "setBurnProperties",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_197",
                "blockFireSpreadSpeed",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedWorld",
                        "blockFireSpreadSpeed",
                        null
                )
        );
        visitorInfos.registerFieldRef(
                "net/minecraft/class_197",
                "blockFlammability",
                "",
                new VisitorInfos.FullClassMember(
                        "fr/catcore/fabricatedforge/forged/reflection/ReflectedWorld",
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

//        // Mystcraft
//        infos.registerMethodFieldIns(
//                new VisitorInfos.MethodNamed("xcompwiz/mystcraft/Mystcraft", "registeredDims"),
//                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/MystcraftCompat", "registeredDims")
//        );
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

    public static class BArrayList extends fr.catcore.modremapperapi.utils.BArrayList<String> {

        @Override
        public BArrayList put(String entry) {
            return (BArrayList) super.put(entry + ".class");
        }
    }
}
