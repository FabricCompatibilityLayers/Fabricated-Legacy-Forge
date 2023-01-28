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
        types.put(new VisitorInfos.Type("net/minecraft/class_1041"), new VisitorInfos.Type("fr/catcore/fabricatedforge/forged/ItemGroupForged"));
        types.put(new VisitorInfos.Type("net/minecraft/class_847"), new VisitorInfos.Type("fr/catcore/fabricatedforge/forged/WeightedRandomChestContentForged"));
        types.put(new VisitorInfos.Type("net/minecraft/class_1196"), new VisitorInfos.Type("fr/catcore/fabricatedforge/forged/ChunkForged"));
        types.put(new VisitorInfos.Type("net/minecraft/class_965"), new VisitorInfos.Type("fr/catcore/fabricatedforge/forged/AbstractMinecartEntityForged"));
        types.put(new VisitorInfos.Type("net/minecraft/class_1239"), new VisitorInfos.Type("fr/catcore/fabricatedforge/forged/OreFeatureForged"));

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
                .put("aae")
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
                .put("asi")
                .put("asw")
                .put("asz")
                .put("atf")
                .put("ath")
                .put("aud")
                .put("aun")
                .put("avi")
                .put("awq")
                .put("awt")
                .put("axf")
                .put("axp")
                .put("axu")
                .put("axw")
                .put("ayd")
                .put("aye")
                .put("azc")
                .put("azn")
                .put("azu")
                .put("azy")
                .put("bac")
                .put("bad")
                .put("bah")
                .put("bai")
                .put("baj")
                .put("bbc")
                .put("bbk")
                .put("bbm")
                .put("bbw")
                .put("bcb")
                .put("bcm")
                .put("bcn")
                .put("bcp")
                .put("bcq")
                .put("bcr")
                .put("bcs")
                .put("bct")
                .put("bcu")
                .put("bda")
                .put("bdh")
                .put("bdk")
                .put("bed")
                .put("bee")
                .put("bef")
                .put("bm")
                .put("bz")
                .put("ce")
                .put("cf")
                .put("dr")
                .put("dt")
                .put("dw")
                .put("ef")
                .put("eg")
                .put("fq")
                .put("gl")
                .put("hn")
                .put("ht")
                .put("hz")
                .put("ih")
                .put("ik")
                .put("il")
                .put("im")
                .put("ip")
                .put("iq")
                .put("ir")
                .put("is")
                .put("iu")
                .put("iv")
                .put("jp")
                .put("kv")
                .put("ll")
                .put("lp")
                .put("lu")
                .put("mc")
                .put("ni")
                .put("pc")
                .put("pd")
                .put("pf")
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
