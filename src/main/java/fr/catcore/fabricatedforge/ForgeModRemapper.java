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
    }

    @Override
    public Optional<String> getDefaultPackage() {
        return Optional.of("net/minecraft/");
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
