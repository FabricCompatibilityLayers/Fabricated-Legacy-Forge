package fr.catcore.fabricatedforge;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import fr.catcore.modremapperapi.remapping.VisitorInfos;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ForgeModRemapper implements ModRemapper {

    private static final BArrayList FORGE_EXCLUDED = new BArrayList();
    private static final Map<String, fr.catcore.modremapperapi.utils.BArrayList<String>> EXCLUDED = new HashMap<>();
    
    @Override
    public String[] getJarFolders() {
        return new String[] {"coremods"};
    }

    @Override
    public RemapLibrary[] getRemapLibraries() {
        return new RemapLibrary[] {
                new RemapLibrary(
                        Constants.FORGE_URL,
                        FORGE_EXCLUDED,
                        "forge.zip"
                )
        };
    }

    @Override
    public Map<String, List<String>> getExclusions() {
        return new HashMap<>(EXCLUDED);
    }

    @Override
    public void getMappingList(RemapUtil.MappingList mappings) {
        // ModLoader mappings
        mappings.add("BaseMod", "net/minecraft/BaseMod");
        mappings.add("EntityRendererProxy", "net/minecraft/EntityRendererProxy");
        mappings.add("FMLRendererAccessLibrary", "net/minecraft/FMLRendererAccessLibrary");
        mappings.add("MLProp", "net/minecraft/MLProp");
        mappings.add("ModLoader", "net/minecraft/ModLoader");
        mappings.add("ModTextureAnimation", "net/minecraft/ModTextureAnimation");
        mappings.add("ModTextureStatic", "net/minecraft/ModTextureStatic");
        mappings.add("TradeEntry", "net/minecraft/TradeEntry");
    }

    private String getOfficialClassName(String className) {
        className = className.replace("/", ".");
        className = FabricLoader.getInstance().getMappingResolver().unmapClassName("official", className);
        return className.replace(".", "/");
    }

    @Override
    public void registerVisitors(VisitorInfos infos) {
        Map<VisitorInfos.Type, VisitorInfos.Type> types = new HashMap<>();

        for (Map.Entry<VisitorInfos.Type, VisitorInfos.Type> entry : types.entrySet()) {
            infos.registerSuperType(entry.getKey(), entry.getValue());
            infos.registerMethodTypeIns(entry.getKey(), entry.getValue());

            infos.registerMethodMethodIns(
                    new VisitorInfos.MethodNamed(entry.getKey().type, "<init>"),
                    new VisitorInfos.MethodNamed(entry.getValue().type, "<init>")
            );
        }

        infos.registerMethodMethodIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_197", "setBurnProperties"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "Block_setBurnProperties")
        );

        // Mystcraft
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("xcompwiz/mystcraft/Mystcraft", "registeredDims"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/MystcraftCompat", "registeredDims")
        );

        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_9", "allowedBiomes"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "StrongholdStructure_allowedBiomes")
        );
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_1175", "allowedBiomes"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "LayeredBiomeSource_allowedBiomes")
        );

        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_570", "NAME_TAG_RANGE"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "NAME_TAG_RANGE")
        );
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_570", "NAME_TAG_RANGE_SNEAK"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "NAME_TAG_RANGE_SNEAK")
        );
    }

    @Override
    public Optional<String> getDefaultPackage() {
        return Optional.of("net/minecraft/");
    }
    
    static {
        FORGE_EXCLUDED.put("a")
                .put("aaf")
                .put("aaj")
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
                .put("agi")
                .put("ahg")
                .put("ahk")
                .put("aho")
                .put("ahq")
                .put("aix")
                .put("aiy")
                .put("aiz")
                .put("ajd")
                .put("ajj")
                .put("ajp")
                .put("ajq")
                .put("aju")
                .put("ajv")
                .put("ajy")
                .put("akc")
                .put("akl")
                .put("akm")
                .put("ako")
                .put("akx")
                .put("akz")
                .put("ala")
                .put("alk")
                .put("all")
                .put("alo")
                .put("alr")
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
                .put("kw")
                .put("lm")
                .put("lq")
                .put("lv")
                .put("md")
                .put("nj")
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
                .put("sz")
                .put("tb")
                .put("th")
                .put("tu")
                .put("tw")
                .put("ty")
                .put("uj")
                .put("uk")
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
                .put("yw")
                .put("zs");
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

    public static class BArrayList extends fr.catcore.modremapperapi.utils.BArrayList<String> {

        @Override
        public BArrayList put(String entry) {
            return (BArrayList) super.put(entry + ".class");
        }
    }
}
